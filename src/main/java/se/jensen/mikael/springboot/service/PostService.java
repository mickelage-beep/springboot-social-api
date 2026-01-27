package se.jensen.mikael.springboot.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    // ----------------------------
    // CREATE – skapa en ny Post
    // ----------------------------
    public PostResponseDTO createPost(Long userId, PostRequestDTO postDTO) {

        // Skapa ett nytt Post-objekt
        Post post = new Post();

        // Sätt text från DTO
        post.setText(postDTO.text());

        // Sätt skapandetid till nuvarande tid
        post.setCreatedAt(Instant.now());

        // Hämta användare från databasen baserat på userId
        // Om användaren inte finns kastas ett NoSuchElementException & loggar det med logger.
        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    logger.warn("User not found with id: " + userId);
                    return new NoSuchElementException("User not found with id: " + userId);
                });


        // Koppla posten till användaren
        post.setUser(user);

        // Spara posten i databasen via repository
        Post savedPost = postRepository.save(post);

        // Logga att posten skapas
        logger.info("Post created with id: " + savedPost.getId());

        // Returnera en PostResponseDTO som ska skickas tillbaka till klienten
        return new PostResponseDTO(
                savedPost.getId(),
                savedPost.getText(),
                savedPost.getCreatedAt(),
                userMapper.toDto(savedPost.getUser())
        );
    }
}
