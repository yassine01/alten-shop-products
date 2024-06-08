package alten.backend.productapi.exception;

public class ProductServiceException extends RuntimeException {

    public ProductServiceException (String msg, Throwable cause) {
        super(msg, cause);
    }

    public ProductServiceException (String msg) {
        super(msg);
    }
}
