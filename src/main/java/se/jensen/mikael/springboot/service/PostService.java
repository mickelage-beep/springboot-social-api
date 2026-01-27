package se.jensen.mikael.springboot.service;

import org.springframework.stereotype.Service;
import se.jensen.mikael.springboot.dto.PostRequestDTO;
import se.jensen.mikael.springboot.dto.PostResponseDTO;
import se.jensen.mikael.springboot.mapper.UserMapper;
import se.jensen.mikael.springboot.model.Post;
import se.jensen.mikael.springboot.model.User;
import se.jensen.mikael.springboot.repository.PostRepository;
import se.jensen.mikael.springboot.repository.UserRepository;

import java.time.Instant;
import java.util.NoSuchElementException;

/**
 * Service-klass som hanterar logik för inlägg (Post).
 * Använder UserRepository för att hämta användare
 * och PostRepository för att spara och hämta inlägg.
 * Mapperar även User till UserResponseDTO.
 */
@Service
public class PostService {

    // Logger
    private static final Logger logger =
            LoggerFactory.getLogger(PostService.class);


    // ----------------------------
    // Repositories för databasen
    // ----------------------------
    private final UserRepository userRepository; // Används för att hämta User som posten ska kopplas till
    private final PostRepository postRepository; // Används för att spara och hämta Post-objekt
    private final UserMapper userMapper;         // används för att mappar User till UserDTO

    // Konstruktor med DI (Dependency Injection)
    public PostService(UserRepository userRepository, PostRepository postRepository) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.userMapper = new UserMapper();
    }

    /**
     * Skapar ett nytt inlägg kopplat till en användare.
     * userId, id för användaren som äger posten
     * postDTO, data för det nya inlägget
     * PostResponseDTO, med sparad post och användarinformation
     * NoSuchElementException om användaren inte finns
     */
    public PostResponseDTO createPost(Long userId, PostRequestDTO postDTO) {

        Post post = new Post();

        post.setText(postDTO.text());

        post.setCreatedAt(Instant.now());

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found with id: " + userId));

        post.setUser(user);

        Post savedPost = postRepository.save(post);

        return new PostResponseDTO(
                savedPost.getId(),
                savedPost.getText(),
                savedPost.getCreatedAt(),
                userMapper.toDto(savedPost.getUser())
        );
    }
}

