package Exceptions;

public class FileCreationException extends RuntimeException {
    public FileCreationException(final String message) {
        super(message);
    }
}
