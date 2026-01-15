package se.jensen.mikael.springboot.exception;

// -----------------------------------------------------------
// EGEN EXCEPTION: UserAlreadyExistsException
// -----------------------------------------------------------

/*
 * Genom att skapa en egen exception kan vi:
 *  - Ge tydliga och specifika felmeddelanden
 *  - Hantera olika typer av fel separat i GlobalExceptionHandler
 *  - Returnera lämplig HTTP-statuskod (t.ex. 400 Bad Request)
 *
 * Klassen ärver från RuntimeException, vilket betyder:
 *  - Den är unchecked, dvs vi behöver inte deklarera "throws"
 *  - Kan kastas direkt från service- eller controller-lager
 */
public class UserAlreadyExistsException extends RuntimeException {

    /*
     * Konstruktor som tar ett meddelande som beskriver felet.
     *
     * @param message meddelande som beskriver varför exception kastas
     */
    public UserAlreadyExistsException(String message) {
        super(message); // skickar meddelandet till RuntimeException
    }
}
