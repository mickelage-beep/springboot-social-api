package se.jensen.mikael.springboot.dto;

import java.util.List;

/**
 * DTO som används när vi vill skicka en user tillsammans med dess posts
 */
public record UserWithPostsResponseDTO(
        UserResponseDTO user,        // Användarinfo
        List<PostResponseDTO> posts  // Lista med användarens posts
) {
}
