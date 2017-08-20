package cn.edu.swpu.cins.exception;

public class UserNoExitedException extends HappyMallException {

    public UserNoExitedException() {
    }

    public UserNoExitedException(String message) {
        super(message);
    }

    public UserNoExitedException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserNoExitedException(Throwable cause) {
        super(cause);
    }

    public UserNoExitedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
