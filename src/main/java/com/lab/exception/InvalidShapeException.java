package com.lab.exception;

public class InvalidShapeException extends Exception {

    public InvalidShapeException(String message) {
        super(message);
    }

    public InvalidShapeException(String message, NumberFormatException n) {
        super(message);
        System.out.println(n.getMessage());
    }
}
