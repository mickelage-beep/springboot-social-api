package se.jensen.mikael.springboot.dto;

/**
 * Används för att ta emot inloggningsdata från klienten.
 * Efter som detta är en record skapas getter metoder atomatiskt
 * och den är immutable (kan inte ändras efter skapande)
 */
public record LoginRequestDTO(String username, String password) {
}

