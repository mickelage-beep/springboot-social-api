package se.jensen.mikael.springboot.dto;

import java.time.LocalDateTime;

/**
 * DTO som skickas tillbaka till klienten när en post hämtas.
 */
public record PostResponseDTO(
        Long id,              // Unikt ID för posten (från databasen)
        String text,          // Textinnehållet i posten
        LocalDateTime createdAt, // Tidpunkt då posten skapades
        UserResponseDTO user     // användare som skapade posten
) {
}
