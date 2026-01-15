package se.jensen.mikael.springboot.mapper;

import org.springframework.stereotype.Component;
import se.jensen.mikael.springboot.dto.PostResponseDTO;
import se.jensen.mikael.springboot.dto.UserResponseDTO;
import se.jensen.mikael.springboot.dto.UserWithPostsResponseDTO;
import se.jensen.mikael.springboot.model.User;

import java.util.List;

@Component
public class UserWithPostsMapper {
    public UserWithPostsResponseDTO toDto(User user) {

        //Mappa posts till PostResponceDTO
        List<PostResponseDTO> posts = user.getPosts()
                .stream()
                .map(p -> new PostResponseDTO(
                        p.getId(),
                        p.getText(),
                        p.getCreatedAt()
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
