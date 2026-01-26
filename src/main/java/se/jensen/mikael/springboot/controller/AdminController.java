package se.jensen.mikael.springboot.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller som hanterar admin-relaterade endpoints.
 * - /admin (GET) → Returnerar en enkel textsträng som visar att det är en admin-sida
 * Notera: Säkerheten hanteras via Spring Security (t.ex. SecurityConfig) för att
 * endast ge åtkomst till användare med rollen ADMIN.
 */
@RestController // Markerar att denna klass är en REST-controller som returnerar data direkt
@RequestMapping("/admin") // Bas-URL för alla endpoints i denna controller
public class AdminController {

    /**
     * GET /admin
     * Exempel på enkel GET-endpoint som returnerar en textsträng.
     * Endast användare med roll ADMIN ska ha tillgång (konfigureras i SecurityConfig).
     *
     * @return en sträng som representerar admin-sidan
     */
    @GetMapping
    public String getAdminPage() {
        return "Admin page"; // Returnerar text direkt som HTTP-response body
    }
}
