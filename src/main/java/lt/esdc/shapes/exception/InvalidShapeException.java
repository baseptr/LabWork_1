package lt.esdc.shapes.exception;

public class InvalidShapeException extends Exception {

    public InvalidShapeException(String message) {
        super(message);
    }

    public InvalidShapeException(String message, Throwable cause) {
        super(message, cause);
    }
}