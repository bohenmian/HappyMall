package cn.edu.swpu.cins.exception;

public class ProductNoExitedException extends HappyMallException {
    public ProductNoExitedException() {
    }

    public ProductNoExitedException(String message) {
        super(message);
    }

    public ProductNoExitedException(String message, Throwable cause) {
        super(message, cause);
    }

    public ProductNoExitedException(Throwable cause) {
        super(cause);
    }

    public ProductNoExitedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
