package se.jensen.mikael.springboot.controller;

import org.springframework.web.bind.annotation.*;

/**
 * Enkel controller som visar hur man kan skapa en REST-endpoint.
 * - /hello (GET) → Returnerar en enkel sträng
 * - /hello (POST) → Tar emot en sträng från klienten och returnerar den med ett meddelande
 */
@RestController // Markerar att denna klass är en REST-controller och returnerar data (inte vyer)
@RequestMapping("/hello") // Bas-URL för alla endpoints i denna controller
public class HelloController {

    /**
     * Exempel på enkel GET-endpoint som returnerar en statisk sträng.
     * Kan testas via webbläsare eller Postman/curl.
     */
    @GetMapping
    public String hello() {
        return "Hello from Spring Boot"; // Returnerar text direkt som response body
    }

    /**
     * Exempel på enkel POST-endpoint som tar emot en sträng från klienten.
     */
    @PostMapping
    public String post(@RequestBody String message) {
        return message + " received"; // Returnerar samma meddelande + " received"
    }
}
