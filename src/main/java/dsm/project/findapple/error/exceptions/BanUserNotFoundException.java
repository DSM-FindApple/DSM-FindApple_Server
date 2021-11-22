package dsm.project.findapple.error.exceptions;

import dsm.project.findapple.error.exception.ErrorCode;
import dsm.project.findapple.error.exception.FindAppleException;

public class BanUserNotFoundException extends FindAppleException {
    public BanUserNotFoundException() {
        super(ErrorCode.BAN_USER_NOT_FOUND);
    }
}
