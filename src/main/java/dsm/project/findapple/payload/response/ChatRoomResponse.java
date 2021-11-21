package dsm.project.findapple.payload.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChatRoomResponse {
    private String chatId;
    private String title;
    private String topMessage;
    private String targetProfileUrl;
    private Boolean isBan;
}
