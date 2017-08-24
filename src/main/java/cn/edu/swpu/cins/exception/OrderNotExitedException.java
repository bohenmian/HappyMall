package cn.edu.swpu.cins.exception;

public class OrderNotExitedException extends HappyMallException {
    public OrderNotExitedException() {
    }

    public OrderNotExitedException(String message) {
        super(message);
    }

    public OrderNotExitedException(String message, Throwable cause) {
        super(message, cause);
    }

    public OrderNotExitedException(Throwable cause) {
        super(cause);
    }

    public OrderNotExitedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
