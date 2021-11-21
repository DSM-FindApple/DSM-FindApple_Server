package dsm.project.findapple.payload.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BanUserResponse {
    private Long kakaoId;
    private String banUserName;
}
