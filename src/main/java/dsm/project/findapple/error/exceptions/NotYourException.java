package dsm.project.findapple.error.exceptions;

import dsm.project.findapple.error.exception.ErrorCode;
import dsm.project.findapple.error.exception.FindAppleException;

public class NotYourException extends FindAppleException {
    public NotYourException() {
        super(ErrorCode.NOT_YOUR);
    }
}
