package dsm.project.findapple.error.exceptions;

import dsm.project.findapple.error.exception.ErrorCode;
import dsm.project.findapple.error.exception.FindAppleException;

public class DoNotHaveAuthorityException extends FindAppleException {
    public DoNotHaveAuthorityException() {
        super(ErrorCode.DO_NOT_HAVE_AUTHORITY);
    }
}
