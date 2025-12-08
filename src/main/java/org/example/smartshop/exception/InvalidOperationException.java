package org.example.smartshop.exception;

public class InvalidOperationException extends RuntimeException {
    public InvalidOperationException(String message) {
    super(message);
}

    public InvalidOperationException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidOperationException(Throwable cause) {
        super(cause);
    }
}
