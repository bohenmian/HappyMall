package cn.edu.swpu.cins.exception;

public class AddressNotExitedException extends HappyMallException {
    public AddressNotExitedException() {
    }

    public AddressNotExitedException(String message) {
        super(message);
    }

    public AddressNotExitedException(String message, Throwable cause) {
        super(message, cause);
    }

    public AddressNotExitedException(Throwable cause) {
        super(cause);
    }

    public AddressNotExitedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
