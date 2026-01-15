package se.jensen.mikael.springboot.dto;

/*
 * UserResponseDTO
 * - DTO som används för att skicka data från servern till klienten
 * - Innehåller endast de fält som klienten behöver se
 * - Observera: password skickas **inte** tillbaka för säkerhetsskäl
 * - Record används för enkel, immutable data container
 * - Fördel: automatisk konstruktor, getters och equals/hashCode genereras
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
