package dsm.project.findapple.payload.response;

import dsm.project.findapple.payload.enums.MessageType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PromiseResponse {
    private Long promiseId;
    private String chatId;
    private String script;
    private String meetAt;
    private Double longitude;
    private Double latitude;
    private Boolean isAccept;
    private String targetName;
    private String targetProfileUrl;
}
