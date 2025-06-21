package pe.com.graduate.insights.api.domain.exception;
 
public class InvalidCredentialsException extends RuntimeException {
    public InvalidCredentialsException(String message) {
        super(message);
    }
} 