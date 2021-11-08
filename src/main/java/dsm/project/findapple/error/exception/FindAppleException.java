package dsm.project.findapple.error.exception;

import lombok.Getter;

@Getter
public class FindAppleException extends RuntimeException {

    private final ErrorCode errorCode;

    public FindAppleException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public FindAppleException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}
