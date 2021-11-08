package dsm.project.findapple.error.exceptions;

import dsm.project.findapple.error.exception.ErrorCode;
import dsm.project.findapple.error.exception.FindAppleException;

public class InvalidTokenException extends FindAppleException {
    public InvalidTokenException() {
        super(ErrorCode.INVALID_TOKEN);
    }
}
