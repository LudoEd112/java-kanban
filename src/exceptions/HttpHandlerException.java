package exceptions;

public class HttpHandlerException extends RuntimeException {
    public HttpHandlerException(String message) {
        super(message);
    }

    public HttpHandlerException(String message, Throwable cause) {
        super(message, cause);
    }
}
