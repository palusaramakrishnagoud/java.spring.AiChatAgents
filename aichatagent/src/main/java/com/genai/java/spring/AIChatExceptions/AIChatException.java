package com.genai.java.spring.AIChatExceptions;

public class AIChatException extends RuntimeException {
    public AIChatException(String message) {
        super(message);
    }
    public AIChatException(String message, Throwable cause) {
        super(message, cause);
    }

}
