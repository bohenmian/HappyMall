package cn.edu.swpu.cins.exception;

public class EmailNotExitedException extends HappyMallException {

    public EmailNotExitedException() {
    }

    public EmailNotExitedException(String message) {
        super(message);
    }

    public EmailNotExitedException(String message, Throwable cause) {
        super(message, cause);
    }

    public EmailNotExitedException(Throwable cause) {
        super(cause);
    }

    public EmailNotExitedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
