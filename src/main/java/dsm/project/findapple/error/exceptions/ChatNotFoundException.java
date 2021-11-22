package dsm.project.findapple.error.exceptions;

import dsm.project.findapple.error.exception.ErrorCode;
import dsm.project.findapple.error.exception.FindAppleException;

public class ChatNotFoundException extends FindAppleException {
    public ChatNotFoundException() {
        super(ErrorCode.CHAT_NOT_FOUND);
    }
}
