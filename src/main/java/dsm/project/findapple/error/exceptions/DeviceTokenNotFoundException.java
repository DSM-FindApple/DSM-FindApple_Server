package dsm.project.findapple.error.exceptions;

import dsm.project.findapple.error.exception.ErrorCode;
import dsm.project.findapple.error.exception.FindAppleException;

public class DeviceTokenNotFoundException extends FindAppleException {
    public DeviceTokenNotFoundException() {
        super(ErrorCode.DEVICE_TOKEN_NOT_FOUND);
    }
}
