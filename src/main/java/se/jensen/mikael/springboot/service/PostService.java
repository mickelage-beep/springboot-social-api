package se.jensen.mikael.springboot.service;

import org.springframework.stereotype.Service;
import se.jensen.mikael.springboot.dto.PostRequestDTO;
import se.jensen.mikael.springboot.dto.PostResponseDTO;
import se.jensen.mikael.springboot.model.Post;
import se.jensen.mikael.springboot.model.User;
import se.jensen.mikael.springboot.repository.PostRepository;
import se.jensen.mikael.springboot.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;

@Service
public class PostService {

    // ----------------------------
    // Repositories för databasen
    // ----------------------------
    private final UserRepository userRepository; // Används för att hämta User som posten ska kopplas till
    private final PostRepository postRepository; // Används för att spara och hämta Post-objekt

    // Konstruktor med DI (Dependency Injection)
    public PostService(UserRepository userRepository, PostRepository postRepository) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
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
        post.setCreatedAt(LocalDateTime.now());

        // Hämta användare från databasen baserat på userId
        // Om användaren inte finns kastas ett NoSuchElementException
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found with id: " + userId));

        // Koppla posten till användaren
        post.setUser(user);

        // Spara posten i databasen via repository
        Post savedPost = postRepository.save(post);

        // Returnera en PostResponseDTO som ska skickas tillbaka till klienten
        return new PostResponseDTO(
                savedPost.getId(),
                savedPost.getText(),
                savedPost.getCreatedAt()
        );
    }
}
