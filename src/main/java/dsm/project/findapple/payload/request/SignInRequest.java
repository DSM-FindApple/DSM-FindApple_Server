package dsm.project.findapple.payload.request;

import lombok.Getter;

@Getter
public class SignInRequest {
    private Long kakaoId;
    private String kakaoNickName;
    private String profileUrl;
    private String deviceToken;
    private Double longitude;
    private Double latitude;
}
