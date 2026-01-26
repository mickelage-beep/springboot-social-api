package se.jensen.mikael.springboot.exception;

/**
 * Exception som kastas när en användare inte hittas i databasen.
 * Kan skapas med antingen användarens ID eller username.
 */
public class UserNotFoundException extends RuntimeException {


    public UserNotFoundException(Long id) {
        super("Ingen user med id: " + id); // skickar meddelandet till RuntimeException
    }


    public UserNotFoundException(String username) {
        super("Ingen user med username: " + username); // skickar meddelandet till RuntimeException
    }
}
