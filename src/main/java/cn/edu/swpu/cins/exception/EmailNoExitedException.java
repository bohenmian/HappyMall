package cn.edu.swpu.cins.exception;

public class EmailNoExitedException extends HappyMallException {

    public EmailNoExitedException() {
    }

    public EmailNoExitedException(String message) {
        super(message);
    }

    public EmailNoExitedException(String message, Throwable cause) {
        super(message, cause);
    }

    public EmailNoExitedException(Throwable cause) {
        super(cause);
    }

    public EmailNoExitedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
