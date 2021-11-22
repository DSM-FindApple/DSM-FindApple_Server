package dsm.project.findapple.error.exceptions;

import dsm.project.findapple.error.exception.ErrorCode;
import dsm.project.findapple.error.exception.FindAppleException;

public class FindNotFoundException extends FindAppleException {
    public FindNotFoundException() {
        super(ErrorCode.FIND_NOT_FOUND);
    }
}
