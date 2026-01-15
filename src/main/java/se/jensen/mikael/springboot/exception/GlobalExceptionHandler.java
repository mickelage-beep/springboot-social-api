package se.jensen.mikael.springboot.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

/*
 * Global exception handler
 *
 * - Fångar fel från hela applikationen
 * - Returnerar strukturerade felmeddelanden istället för generiska statuskoder
 * - Exempel: 400 Bad Request med info om vilka fält som är fel, 404 Not Found etc.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /*
     * Valideringsfel (t.ex. @Valid i DTO)
     *
     * - Fångar MethodArgumentNotValidException
     * - Ex: textfält är tomt eller för kort
     * - Returnerar HTTP 400 + JSON med fältnamn och felmeddelande
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationErrors(
            MethodArgumentNotValidException ex
    ) {
        Map<String, String> errors = new HashMap<>();

        // Loopar igenom alla fält där valideringen misslyckades
        for (org.springframework.validation.FieldError fieldError
                : ex.getBindingResult().getFieldErrors()) {

            String fieldName = fieldError.getField();          // Namnet på fältet som är fel
            String message = fieldError.getDefaultMessage();   // Felmeddelande från @NotBlank/@Size etc.
            errors.put(fieldName, message);                    // Lägg till i map som ska returneras
        }

        // Returnerar HTTP 400 med map som body
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    /*
     * 404 – NOT FOUND
     * - Fångar NoSuchElementException, ex: när en user med givet id inte finns
     * - Returnerar HTTP 404 + felmeddelande
     */
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<String> handelNoSuchElement(NoSuchElementException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    /*
     * 400 – BAD REQUEST
     * - Fångar IllegalArgumentException
     * - Ex: försöker skapa user med redan befintligt username/email
     * - Returnerar HTTP 400 + felmeddelande
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgument(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    // -----------------------------------------------------------
    // 404 – USER FINNS EJ
    // -----------------------------------------------------------
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleUserNotFound(UserNotFoundException ex) {
        // När vi kastar UserNotFoundException i service-lag, hamnar det här
        // Vi returnerar HTTP 404 + tydligt meddelande till klient
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    // -----------------------------------------------------------
    // 409 – USER FINNS REDAN
    // -----------------------------------------------------------
    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<String> handleUserAlreadyExists(UserAlreadyExistsException ex) {
        // När vi kastar UserAlreadyExistsException i service-lag
        // Returnerar HTTP 409 Conflict + meddelande
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }
}
