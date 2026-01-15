package se.jensen.mikael.springboot.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/*
 * PostRequestDTO
 * - DTO för data **in** från klienten när en Post skapas eller uppdateras
 * - Validering säkerställer att textfältet är korrekt
 * - Record används för enkel, immutable data container
 * - Automatisk konstruktor
 * - Getter-metoder genereras automatiskt
 */
public record PostRequestDTO(

        @NotBlank(message = "Text får inte vara tom.")         // Textfältet får inte vara tomt
        @Size(min = 3, max = 200, message = "Text måste vara mellan 3 och 200 tecken.") // Min/max-längd
        String text                                            // Innehållet i posten
) {
}
