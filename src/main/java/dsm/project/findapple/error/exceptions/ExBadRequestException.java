package dsm.project.findapple.error.exceptions;

import dsm.project.findapple.error.exception.ErrorCode;
import dsm.project.findapple.error.exception.FindAppleException;

public class ExBadRequestException extends FindAppleException {
    public ExBadRequestException() {
        super(ErrorCode.EX_BAD_REQUEST);
    }
}
