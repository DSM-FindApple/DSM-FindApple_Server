package dsm.project.findapple.error.exceptions;

import dsm.project.findapple.error.exception.ErrorCode;
import dsm.project.findapple.error.exception.FindAppleException;

public class FileNotFoundException extends FindAppleException {
    public FileNotFoundException() {
        super(ErrorCode.FILE_NOT_FOUND);
    }
}
