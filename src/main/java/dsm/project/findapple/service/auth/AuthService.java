package dsm.project.findapple.service.auth;

import dsm.project.findapple.payload.request.SignInRequest;
import dsm.project.findapple.payload.response.TokenResponse;

public interface AuthService {
    TokenResponse signIn(SignInRequest signInRequest);
    TokenResponse refreshToken(String refreshToken);
    void logout(String token, String deviceToken);
}
