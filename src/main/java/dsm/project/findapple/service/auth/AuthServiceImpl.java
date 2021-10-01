package dsm.project.findapple.service.auth;

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

    private final JwtProvider jwtProvider;

    @Override
    public TokenResponse signIn(SignInRequest signInRequest) {
        return userRepository.findByKakaoIdAndKakaoNickName(signInRequest.getKakaoId(), signInRequest.getKakaoNickName())
                .map(user -> {
                    String accessToken = jwtProvider.generateAccessToken(user.getKakaoId());
                    String refreshToken = jwtProvider.generateRefreshToken(accessToken);

                    refreshTokenRepository.save(
                            RefreshToken.builder()
                                    .kakaoId(user.getKakaoId())
                                    .refreshToken(refreshToken)
                                    .build()
                    );

                    if(!deviceTokenRepository.existsByUserAndDeviceToken(user, signInRequest.getDeviceToken())) {
                        deviceTokenRepository.save(
                                DeviceToken.builder()
                                        .deviceToken(signInRequest.getDeviceToken())
                                        .user(user)
                                        .build()
                        );
                    }

                    return TokenResponse.builder()
                            .accessToken(accessToken)
                            .refreshToken(refreshToken)
                            .build();
                }).orElseThrow(RuntimeException::new);
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
