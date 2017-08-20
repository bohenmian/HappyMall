package cn.edu.swpu.cins.exception;

public class HappyMallException extends RuntimeException {

    public HappyMallException() {
    }

    public HappyMallException(String message) {
        super(message);
    }

    public HappyMallException(String message, Throwable cause) {
        super(message, cause);
    }

    public HappyMallException(Throwable cause) {
        super(cause);
    }

    public HappyMallException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
