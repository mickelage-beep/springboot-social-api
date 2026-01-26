package se.jensen.mikael.springboot.dto;

/**
 * DTO som används för att skicka data från servern till klienten
 * Innehåller endast de fält som klienten behöver se
 */
public record UserResponseDTO(
        Long id,                  // Unikt ID för användaren (från databasen)
        String username,          // Användarnamn
        String email,             // E-postadress
        String role,              // Roll: t.ex. USER eller ADMIN
        String displayName,       // Visningsnamn
        String bio,               // Kort biografi
        String profileImagePath   // Länk till profilbild (kan vara null)
) {
}
