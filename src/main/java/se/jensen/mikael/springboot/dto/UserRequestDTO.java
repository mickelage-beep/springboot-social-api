package se.jensen.mikael.springboot.dto;

import jakarta.validation.constraints.NotBlank;

/*
 * UserRequestDTO
 * - DTO som används när klienten skickar in data till servern
 * - Används vid POST/PUT för att skapa eller uppdatera en användare
 * - Innehåller validering (@NotBlank) för att säkerställa att viktiga fält inte är tomma
 * - Record används för enkel, immutable data container
 * - Automatisk konstruktor
 * - Getter-metoder genereras automatiskt
 */
public record UserRequestDTO(

        @NotBlank(message = "Username får inte vara tomt.")
        String username,             // Användarnamn

        @NotBlank(message = "Email får inte vara tomt")
        String email,                // E-postadress

        @NotBlank(message = "Password får inte vara tomt.")
        String password,             // Lösenord (klartext, kommer hashas i service)

        @NotBlank(message = "Role får inte vara tomt.")
        String role,                 // Roll: t.ex. USER eller ADMIN

        @NotBlank(message = "DisplayName får inte vara tomt.")
        String displayName,          // Visningsnamn

        @NotBlank(message = "Bio får inte vara tomt.")
        String bio,                  // Kort biografi

        String profileImagePath      // Profilbildens sökväg (kan vara null eller tom)
) {
}
