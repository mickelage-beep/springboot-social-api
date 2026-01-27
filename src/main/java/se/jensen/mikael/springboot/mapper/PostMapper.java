package se.jensen.mikael.springboot.mapper;

import org.springframework.stereotype.Component;
import se.jensen.mikael.springboot.dto.PostRequestDTO;
import se.jensen.mikael.springboot.dto.PostResponseDTO;
import se.jensen.mikael.springboot.model.Post;

import java.time.Instant;

/**
 * Mapper som konverterar mellan Post och DTO-klasserna.
 * Spring skapar automatiskt ett objekt av denna klass tack vare @Component.
 */
@Component
public class PostMapper {
    private final UserMapper userMapper;  // lägger dit Usermapper igen

    public PostMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    /**
     * Konverterar en PostRequestDTO till en Post-entitet.
     */
    public Post toPost(PostRequestDTO dto) {
        Post post = new Post();
        post.setId(0L); // Id används temporärt (kan bytas mot auto-genererad id i DB)
        post.setText(dto.text()); // Sätter text från DTO
        post.setCreatedAt(Instant.now()); // Sätter aktuell tid
        return post;
    }

    /**
     * Konverterar en Post-entitet till en PostResponseDTO som
     * skickas till klienten.
     */
    public PostResponseDTO toResponseDTO(Post post) {
        return new PostResponseDTO(
                post.getId(),
                post.getText(),
                post.getCreatedAt(),
                userMapper.toDto(post.getUser())
        );

    }
}
