package se.jensen.mikael.springboot.exception;

/**
 * Exception som kastas när man försöker skapa en användare
 * som redan finns i databasen (samma username eller email).
 */
public class UserAlreadyExistsException extends RuntimeException {


    public UserAlreadyExistsException(String message) {
        super(message); // skickar meddelandet till RuntimeException
    }
}
