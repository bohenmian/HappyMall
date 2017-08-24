package cn.edu.swpu.cins.exception;

public class UserNotExitedException extends HappyMallException {

    public UserNotExitedException() {
    }

    public UserNotExitedException(String message) {
        super(message);
    }

    public UserNotExitedException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserNotExitedException(Throwable cause) {
        super(cause);
    }

    public UserNotExitedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
