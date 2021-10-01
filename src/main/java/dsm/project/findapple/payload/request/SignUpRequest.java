package dsm.project.findapple.payload.request;

import lombok.Getter;

@Getter
public class SignUpRequest {
    private Long kakaoId;
    private String kakaoNickName;
    private Double longitude;
    private Double latitude;
}
