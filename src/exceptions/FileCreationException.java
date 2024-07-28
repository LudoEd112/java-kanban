package exceptions;

public class FileCreationException extends RuntimeException {
    public FileCreationException(final String message) {
        super(message);
    }
}