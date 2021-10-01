package dsm.project.findapple.controller;

import dsm.project.findapple.payload.request.SignInRequest;
import dsm.project.findapple.payload.response.TokenResponse;
import dsm.project.findapple.service.auth.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping
    public TokenResponse login(@RequestBody SignInRequest signInRequest) {
        return authService.signIn(signInRequest);
    }

    @PutMapping
    public TokenResponse refreshToken(@RequestHeader("Authorization") String accessToken,
                                      @RequestHeader("X-Refresh-Token") String refreshToken) {
        return authService.refreshToken(accessToken, refreshToken);
    }

    @DeleteMapping("/logout")
    public void logout(@RequestHeader("X-Device-Token") String deviceToken,
                       @RequestHeader("Authorization") String token) {
        authService.logout(token, deviceToken);
    }
}
