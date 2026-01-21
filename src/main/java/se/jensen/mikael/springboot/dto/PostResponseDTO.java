package se.jensen.mikael.springboot.dto;

import java.time.LocalDateTime;

/*
 * PostResponseDTO
 * - DTO som skickas tillbaka till klienten efter att en post har skapats eller hämtats
 * - Innehåller endast de fält klienten behöver se
 * - Record används för enkel, immutable data container
 * - Automatisk konstruktor
 * - Getter-metoder genereras automatiskt
 */
public record PostResponseDTO(
        Long id,              // Unikt ID för posten (från databasen)
        String text,          // Textinnehållet i posten
        LocalDateTime createdAt, // Tidpunkt då posten skapades
        UserResponseDTO user     // användare som skapade posten
) {
}
