package dsm.project.findapple.payload.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class UserResponse {
    private Long kakaoId;
    private String nickName;
    private String profileUrl;
    private Integer lostNum;
    private Integer findNum;
    private Integer findapplePoint;
    private List<LostResponse> myLosts;
}
