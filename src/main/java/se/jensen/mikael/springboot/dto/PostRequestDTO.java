package se.jensen.mikael.springboot.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO som används när en klient skickar data för att skapa en ny post.
 */
public record PostRequestDTO(

        @NotBlank(message = "Text får inte vara tom.")         // Textfältet får inte vara tomt
        @Size(min = 3, max = 200, message = "Text måste vara mellan 3 och 200 tecken.") // Min/max-längd
        String text                                            // Innehållet i posten
) {
}
