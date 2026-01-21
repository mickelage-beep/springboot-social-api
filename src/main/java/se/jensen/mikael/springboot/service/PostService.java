package se.jensen.mikael.springboot.service;

import org.springframework.stereotype.Service;
import se.jensen.mikael.springboot.dto.PostRequestDTO;
import se.jensen.mikael.springboot.dto.PostResponseDTO;
import se.jensen.mikael.springboot.mapper.UserMapper;
import se.jensen.mikael.springboot.model.Post;
import se.jensen.mikael.springboot.model.User;
import se.jensen.mikael.springboot.repository.PostRepository;
import se.jensen.mikael.springboot.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;

@Service
public class PostService {

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

        Post post = new Post();

        post.setText(postDTO.text());

        post.setCreatedAt(LocalDateTime.now());

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
