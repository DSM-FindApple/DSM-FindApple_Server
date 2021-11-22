package dsm.project.findapple.error.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    EX_BAD_REQUEST("Bad Request Extension", 400),
    IMAGE_NAME_BAD_REQUEST("Bad Request Image Name", 400),
    INVALID_TOKEN("Invalid Token", 403),
    ALREADY_BAN("Already ban user", 403),
    DO_NOT_HAVE_AUTHORITY("You can't touch system message", 403),
    USER_NOT_FOUND("user not found", 404),
    DEVICE_TOKEN_NOT_FOUND("device token not found", 404),
    LOST_NOT_FOUND("lost not found", 404),
    NOT_YOUR("not your thing", 403),
    FILE_NOT_FOUND("file not found", 404),
    CHAT_NOT_FOUND("Chat Not Found", 404),
    CHAT_USER_NOT_FOUND("Chat User Not Found", 404),
    PROMISE_NOT_FOUND("promise not found", 404),
    MESSAGE_NOT_FOUND("Message Not Found", 404),
    MESSAGE_IMAGE_NOT_FOUND("Message Image Not Found", 404),
    BAN_USER_NOT_FOUND("ban user not found", 404),
    FIND_NOT_FOUND("find not found", 404),
    COMMENT_NOT_FOUND("comment not found", 404),
    RECOMMENT_NOT_FOUND("recomment not found", 404);

    private String message;
    private int status;
}
