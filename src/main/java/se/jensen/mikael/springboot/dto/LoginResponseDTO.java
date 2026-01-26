package se.jensen.mikael.springboot.dto;

/**
 * DTO som skickas tillbaka vid lyckad inloggning.
 * Innehåller JWT-token och användarens ID.
 */
public record LoginResponseDTO(String token, Long userId) {
}
