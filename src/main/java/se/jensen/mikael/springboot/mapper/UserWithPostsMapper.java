package se.jensen.mikael.springboot.mapper;

import org.springframework.stereotype.Component;
import se.jensen.mikael.springboot.dto.PostResponseDTO;
import se.jensen.mikael.springboot.dto.UserResponseDTO;
import se.jensen.mikael.springboot.dto.UserWithPostsResponseDTO;
import se.jensen.mikael.springboot.model.User;

import java.util.List;

/**
 * Mapper som omvandlar en User-entity med alla dess posts till en
 * UserWithPostsResponseDTO för API-respons.
 * Använder UserMapper för att mappa varje User till UserResponseDTO.
 */
@Component
public class UserWithPostsMapper {
    //inject UserMapper
    private final UserMapper userMapper;

    /**
     * Konstruktor med dependency injection av UserMapper.
     */
    public UserWithPostsMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    /**
     * Omvandlar en User med dess posts till UserWithPostsResponseDTO.
     */
    public UserWithPostsResponseDTO toDto(User user) {


        //Mappa posts till PostResponceDTO
        //PostResponseDTO behöver userMapper
        List<PostResponseDTO> posts = user.getPosts()
                .stream()
                .map(p -> new PostResponseDTO(
                        p.getId(),
                        p.getText(),
                        p.getCreatedAt(),
                        userMapper.toDto(p.getUser()) // Mappa User till UserResponseDTO
                ))
                .toList();

        //Mappa User till UserResponseDTO
        UserResponseDTO userResponseDTO = new UserResponseDTO(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole(),
                user.getDisplayName(),
                user.getBio(),
                user.getProfileImagePath()
        );

        //Retunera UserWithPostsResponseDTO
        return new UserWithPostsResponseDTO(userResponseDTO, posts);
    }
}
