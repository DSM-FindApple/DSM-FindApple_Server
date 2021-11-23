package dsm.project.findapple.payload.response;

import dsm.project.findapple.payload.enums.MessageType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MessageResponse {
    private Long messageId;
    private Long promiseId;
    private String chatId;
    private String sendUserName;
    private String message;
    private String profileUrl;
    private String sendDate;
    private String sendTime;
    private String messageImageName;
    private MessageType messageType;
}
