package se.jensen.mikael.springboot.dto;

import java.util.List;

/*
 * UserWithPostsResponseDTO
 * - DTO som används när vi vill skicka en user tillsammans med dess posts
 * - Exempel: GET /users/{id}/with-posts
 * - Innehåller två fält:
 * 1. user  → UserResponseDTO (information om användaren)
 * 2. posts → Lista med PostResponseDTO (alla posts som användaren har)
 * - Record används här för enkel, immutable DTO
 * - Fördel: automatisk konstruktor, getters och equals/hashCode genereras
 */
public record UserWithPostsResponseDTO(
        UserResponseDTO user,        // Användarinfo
        List<PostResponseDTO> posts  // Lista med användarens posts
) {
}
