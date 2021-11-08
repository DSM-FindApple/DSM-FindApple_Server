package dsm.project.findapple.error.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    INVALID_TOKEN("Invalid Token", 403),
    USER_NOT_FOUND("user not found", 404),
    DEVICE_TOKEN_NOT_FOUND("device token not found", 404),
    LOST_NOT_FOUND("lost not found", 404),
    FILE_NOT_FOUND("file not found", 404);

    private String message;
    private int status;
}
