package dsm.project.findapple.error.exceptions;

import dsm.project.findapple.error.exception.ErrorCode;
import dsm.project.findapple.error.exception.FindAppleException;

public class RecommentNotFoundException extends FindAppleException {
    public RecommentNotFoundException() {
        super(ErrorCode.RECOMMENT_NOT_FOUND);
    }
}
