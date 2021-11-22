package dsm.project.findapple.service.auth;

import dsm.project.findapple.entity.area.Area;
import dsm.project.findapple.entity.area.AreaRepository;
import dsm.project.findapple.entity.deviceToken.DeviceToken;
import dsm.project.findapple.entity.deviceToken.DeviceTokenRepository;
import dsm.project.findapple.entity.refresh_token.RefreshToken;
import dsm.project.findapple.entity.refresh_token.RefreshTokenRepository;
import dsm.project.findapple.entity.user.User;
import dsm.project.findapple.entity.user.UserRepository;
import dsm.project.findapple.error.exceptions.InvalidTokenException;
import dsm.project.findapple.error.exceptions.UserNotFoundException;
import dsm.project.findapple.payload.request.SignInRequest;
import dsm.project.findapple.payload.response.TokenResponse;
import dsm.project.findapple.utils.JwtProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final DeviceTokenRepository deviceTokenRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final AreaRepository areaRepository;

    private final JwtProvider jwtProvider;

    private TokenResponse getTokens(Long kakaoId, String kakaoNickName, String profileUrl, String deviceToken) {
        return userRepository.findByKakaoId(kakaoId)
                .map(user -> {
                    String accessToken = jwtProvider.generateAccessToken(user.getKakaoId());
                    String refreshToken = jwtProvider.generateRefreshToken(user.getKakaoId());

                    refreshTokenRepository.save(
                            RefreshToken.builder()
                                    .refreshToken(refreshToken)
                                    .kakaoId(user.getKakaoId())
                                    .build()
                    );

                    if(!user.getKakaoNickName().equals(kakaoNickName)) {
                        userRepository.save(
                                user.updateUserName(kakaoNickName)
                        );
                    }

                    if(!user.getProfileUrl().equals(profileUrl)) {
                        userRepository.save(
                                user.updateProfileUrl(profileUrl)
                        );
                    }

                    if(!deviceTokenRepository.existsByUserAndDeviceToken(user, deviceToken)) {
                        deviceTokenRepository.save(
                                DeviceToken.builder()
                                        .deviceToken(deviceToken)
                                        .user(user)
                                        .build()
                        );
                    }

                    return TokenResponse.builder()
                            .accessToken(accessToken)
                            .refreshToken(refreshToken)
                            .build();
                }).orElse(null);
    }

    @Override
    public TokenResponse signIn(SignInRequest signInRequest) {
        TokenResponse tokenResponse = getTokens(
                signInRequest.getKakaoId(),
                signInRequest.getKakaoNickName(),
                signInRequest.getProfileUrl(),
                signInRequest.getDeviceToken()
        );

        User user;
        Area area;

        if(tokenResponse == null) {
            area = areaRepository.save(Area.builder()
                    .latitude(signInRequest.getLatitude())
                    .longitude(signInRequest.getLongitude())
                    .build()
            );

            user = userRepository.save(User.builder()
                    .kakaoId(signInRequest.getKakaoId())
                    .kakaoNickName(signInRequest.getKakaoNickName())
                    .point(0)
                    .area(area)
                    .profileUrl(signInRequest.getProfileUrl())
                    .build()
            );

            return getTokens(
                    user.getKakaoId(),
                    user.getKakaoNickName(),
                    user.getProfileUrl(),
                    signInRequest.getDeviceToken()
            );
        }else {
            return tokenResponse;
        }
    }

    @Override
    public TokenResponse refreshToken(String refreshToken) {
        if(!jwtProvider.validateToken(refreshToken))
            throw new InvalidTokenException();

        if(!jwtProvider.isRefreshToken(refreshToken))
            throw new InvalidTokenException();

        return refreshTokenRepository.findByRefreshToken(refreshToken)
                .map(refreshToken1 -> {
                    String newAccessToken = jwtProvider.generateAccessToken(refreshToken1.getKakaoId());
                    String newRefreshToken = jwtProvider.generateRefreshToken(refreshToken1.getKakaoId());

                    refreshTokenRepository.save(
                            refreshToken1.updateRefreshToken(newRefreshToken)
                    );

                    return TokenResponse.builder()
                            .accessToken(newAccessToken)
                            .refreshToken(newRefreshToken)
                            .build();
                })
                .orElseThrow(InvalidTokenException::new);
    }

    @Override
    @Transactional
    public void logout(String token, String deviceToken) {
        User user = userRepository.findByKakaoId(jwtProvider.getKakaoId(token))
                .orElseThrow(UserNotFoundException::new);

        deviceTokenRepository.findByDeviceTokenAndUser(deviceToken, user)
                .orElseThrow(RuntimeException::new);

        refreshTokenRepository.deleteByKakaoId(user.getKakaoId());
        deviceTokenRepository.deleteByDeviceTokenAndUser(deviceToken, user);
    }
}
