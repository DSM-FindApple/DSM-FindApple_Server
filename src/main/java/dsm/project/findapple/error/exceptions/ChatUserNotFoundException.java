package dsm.project.findapple.error.exceptions;

import dsm.project.findapple.error.exception.ErrorCode;
import dsm.project.findapple.error.exception.FindAppleException;

public class ChatUserNotFoundException extends FindAppleException {
    public ChatUserNotFoundException() {
        super(ErrorCode.CHAT_USER_NOT_FOUND);
    }
}
