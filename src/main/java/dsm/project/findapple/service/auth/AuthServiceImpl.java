package dsm.project.findapple.service.auth;

import dsm.project.findapple.entity.area.Area;
import dsm.project.findapple.entity.area.AreaRepository;
import dsm.project.findapple.entity.deviceToken.DeviceToken;
import dsm.project.findapple.entity.deviceToken.DeviceTokenRepository;
import dsm.project.findapple.entity.refresh_token.RefreshToken;
import dsm.project.findapple.entity.refresh_token.RefreshTokenRepository;
import dsm.project.findapple.entity.user.User;
import dsm.project.findapple.entity.user.UserRepository;
import dsm.project.findapple.payload.request.SignInRequest;
import dsm.project.findapple.payload.response.TokenResponse;
import dsm.project.findapple.utils.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final DeviceTokenRepository deviceTokenRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final AreaRepository areaRepository;

    private final JwtProvider jwtProvider;

    private TokenResponse getTokens(Long kakaoId, String kakaoNickName, String deviceToken) {
        return userRepository.findByKakaoId(kakaoId)
                .map(user -> {
                    String accessToken = jwtProvider.generateAccessToken(user.getKakaoId());
                    String refreshToken = jwtProvider.generateRefreshToken(accessToken);

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
                signInRequest.getDeviceToken()
        );

        User user = null;
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
                    .build()
            );
        }

        if(user == null)
            throw new RuntimeException("sign up failed");

        return getTokens(
                user.getKakaoId(),
                user.getKakaoNickName(),
                signInRequest.getDeviceToken()
        );
    }

    @Override
    public TokenResponse refreshToken(String accessToken, String refreshToken) {
        return refreshTokenRepository.findByRefreshToken(refreshToken)
                .filter(refreshToken1 -> {
                    if (jwtProvider.isRefreshToken(refreshToken)) {
                        jwtProvider.getKakaoId(refreshToken);
                    }
                    return false;
                })
                .map(refreshToken1 -> {
                    String newAccessToken = jwtProvider.generateAccessToken(refreshToken1.getKakaoId());
                    String newRefreshToken = jwtProvider.generateRefreshToken(accessToken);

                    refreshTokenRepository.save(
                            refreshToken1.updateRefreshToken(newRefreshToken)
                    );

                    return TokenResponse.builder()
                            .accessToken(newAccessToken)
                            .refreshToken(newRefreshToken)
                            .build();
                }).orElseThrow(RuntimeException::new);
    }

    @Override
    @Transactional
    public void logout(String token, String deviceToken) {
        User user = userRepository.findByKakaoId(jwtProvider.getKakaoId(token))
                .orElseThrow(RuntimeException::new);

        deviceTokenRepository.findByDeviceTokenAndUser(deviceToken, user)
                .orElseThrow(RuntimeException::new);

        refreshTokenRepository.deleteByKakaoId(user.getKakaoId());
        deviceTokenRepository.deleteByDeviceTokenAndUser(deviceToken, user);
    }
}
