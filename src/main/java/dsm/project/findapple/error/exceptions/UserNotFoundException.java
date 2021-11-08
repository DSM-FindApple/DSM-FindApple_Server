package dsm.project.findapple.error.exceptions;

import dsm.project.findapple.error.exception.ErrorCode;
import dsm.project.findapple.error.exception.FindAppleException;

public class UserNotFoundException extends FindAppleException {
    public UserNotFoundException() {
        super(ErrorCode.USER_NOT_FOUND);
    }
}
