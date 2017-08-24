package cn.edu.swpu.cins.exception;

public class ProductNotExitedException extends HappyMallException {
    public ProductNotExitedException() {
    }

    public ProductNotExitedException(String message) {
        super(message);
    }

    public ProductNotExitedException(String message, Throwable cause) {
        super(message, cause);
    }

    public ProductNotExitedException(Throwable cause) {
        super(cause);
    }

    public ProductNotExitedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
