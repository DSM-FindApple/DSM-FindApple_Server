package dsm.project.findapple.error.exceptions;

import dsm.project.findapple.error.exception.ErrorCode;
import dsm.project.findapple.error.exception.FindAppleException;

public class MessageImageNotFoundException extends FindAppleException {
    public MessageImageNotFoundException() {
        super(ErrorCode.MESSAGE_IMAGE_NOT_FOUND);
    }
}
