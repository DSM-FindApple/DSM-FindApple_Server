package dsm.project.findapple.error.exceptions;

import dsm.project.findapple.error.exception.ErrorCode;
import dsm.project.findapple.error.exception.FindAppleException;

public class AlreadyBanUserException extends FindAppleException {
    public AlreadyBanUserException() {
        super(ErrorCode.ALREADY_BAN);
    }
}
