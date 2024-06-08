package alten.backend.productapi.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ProductControllerException extends RuntimeException {

    private final HttpStatus statusCode;

    public ProductControllerException(String msg, HttpStatus statusCode) {
        super(msg);
        this.statusCode = statusCode;
    }

    public ProductControllerException(String msg, Throwable cause, HttpStatus statusCode) {
        super(msg, cause);
        this.statusCode = statusCode;
    }

}
