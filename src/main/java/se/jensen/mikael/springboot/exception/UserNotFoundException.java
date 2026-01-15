package se.jensen.mikael.springboot.exception;

// -----------------------------------------------------------
// EGEN EXCEPTION: UserNotFoundException
// -----------------------------------------------------------

/*
 * Genom att skapa en egen exception kan vi:
 *  - Ge tydliga och specifika felmeddelanden
 *  - Hantera olika typer av fel separat i GlobalExceptionHandler
 *  - Returnera lämplig HTTP-statuskod (t.ex. 404 Not Found)
 *
 * Klassen ärver från RuntimeException, vilket betyder:
 *  - Den är unchecked, dvs vi behöver inte deklarera "throws"
 *  - Kan kastas direkt från service- eller controller-lager
 */
public class UserNotFoundException extends RuntimeException {

    /*
     * Konstruktor för att skapa exception när användaren inte hittas via ID.
     *
     * @param id id:t på användaren som inte finns
     */
    public UserNotFoundException(Long id) {
        super("Ingen user med id: " + id); // skickar meddelandet till RuntimeException
    }

    /*
     * Konstruktor för att skapa exception när användaren inte hittas via username.
     *
     * @param username användarnamnet som inte finns
     */
    public UserNotFoundException(String username) {
        super("Ingen user med username: " + username); // skickar meddelandet till RuntimeException
    }
}
