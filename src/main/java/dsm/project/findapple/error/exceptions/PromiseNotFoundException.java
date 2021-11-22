package dsm.project.findapple.error.exceptions;

import dsm.project.findapple.error.exception.ErrorCode;
import dsm.project.findapple.error.exception.FindAppleException;

public class PromiseNotFoundException extends FindAppleException {
    public PromiseNotFoundException() {
        super(ErrorCode.PROMISE_NOT_FOUND);
    }
}
