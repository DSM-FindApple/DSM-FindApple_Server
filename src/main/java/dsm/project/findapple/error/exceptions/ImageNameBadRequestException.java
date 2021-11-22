package dsm.project.findapple.error.exceptions;

import dsm.project.findapple.error.exception.ErrorCode;
import dsm.project.findapple.error.exception.FindAppleException;

public class ImageNameBadRequestException extends FindAppleException {
    public ImageNameBadRequestException() {
        super(ErrorCode.IMAGE_NAME_BAD_REQUEST);
    }
}
