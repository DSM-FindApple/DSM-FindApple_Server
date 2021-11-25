package dsm.project.findapple.payload.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PromiseResponse {
    private Long promiseId;
    private String chatId;
    private Long kakaoId;
    private String script;
    private String meetAt;
    private Double longitude;
    private Double latitude;
    private Boolean isAccept;
    private String targetName;
    private String targetProfileUrl;
}
