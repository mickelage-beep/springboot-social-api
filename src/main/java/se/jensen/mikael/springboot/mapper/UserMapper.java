package se.jensen.mikael.springboot.mapper;

import org.springframework.stereotype.Component;
import se.jensen.mikael.springboot.dto.UserRequestDTO;
import se.jensen.mikael.springboot.dto.UserResponseDTO;
import se.jensen.mikael.springboot.model.User;

/*
 * UserMapper
 *
 * - Ansvarar för att konvertera mellan User-entity och DTO:er
 * - Hjälper till att separera intern modell från API-data
 * - Förhindrar att interna fält exponeras direkt via REST
 */
@Component
public class UserMapper {

    // ============================================================
    // fromDto – skapa ny User från DTO
    // ============================================================
    public static User fromDto(UserRequestDTO dto) {
        User user = new User();                 // Skapar ny User-entity
        updateUserFromDto(user, dto);    // Sätter alla värden från DTO
        return user;
    }

    // ============================================================
    // updateUserFromDto – uppdatera befintlig User från DTO
    // ============================================================
    public static void updateUserFromDto(User user, UserRequestDTO dto) {
        user.setUsername(dto.username());
        user.setEmail(dto.email());
        user.setPassword(dto.password());
        user.setRole(dto.role());
        user.setDisplayName(dto.displayName());
        user.setBio(dto.bio());
        user.setProfileImagePath(dto.profileImagePath());
    }

    // ============================================================
    // toDto – konvertera User till UserResponseDTO
    // ============================================================
    public static UserResponseDTO toDto(User user) {
        return new UserResponseDTO(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole(),
                user.getDisplayName(),
                user.getBio(),
                user.getProfileImagePath()
        );
    }

    // ============================================================
    // Här kan du lägga till metoder för mappning till UserWithPostsResponseDTO
    // ============================================================
}
