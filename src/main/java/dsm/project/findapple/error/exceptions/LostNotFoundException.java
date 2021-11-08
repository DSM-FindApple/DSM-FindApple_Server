package dsm.project.findapple.error.exceptions;

import dsm.project.findapple.error.exception.ErrorCode;
import dsm.project.findapple.error.exception.FindAppleException;

public class LostNotFoundException extends FindAppleException {
    public LostNotFoundException() {
        super(ErrorCode.LOST_NOT_FOUND);
    }
}
