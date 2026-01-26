package se.jensen.mikael.springboot.controller;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.jensen.mikael.springboot.dto.PostRequestDTO;
import se.jensen.mikael.springboot.dto.PostResponseDTO;
import se.jensen.mikael.springboot.mapper.PostMapper;
import se.jensen.mikael.springboot.mapper.UserMapper;
import se.jensen.mikael.springboot.model.Post;
import se.jensen.mikael.springboot.repository.PostRepository;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * Hanterar alla HTTP-anrop relaterade till Post
 * Använder PostMapper för att mappa mellan Post och DTO
 * Returnerar ResponseEntity med korrekt HTTP-status
 */
@RestController
@RequestMapping("/posts") // Bas-URL för alla endpoints i denna controller
public class PostController {
    
    private static final Logger log = LoggerFactory.getLogger(PostController.class);
    //ändrat till repo istället för new list
    private final PostRepository postRepository;

    private final PostMapper postMapper; // Mapper injiceras via DI
    private final UserMapper userMapper;

    // Konstruktor som tar emot mappern. Spring sköter instansiering automatiskt
    public PostController(PostMapper postMapper, PostRepository postRepository, UserMapper userMapper) {
        this.postMapper = postMapper;
        this.postRepository = postRepository;
        this.userMapper = userMapper;
    }

    // -------------------------------------------------------------------
    // POST – skapa en ny Post
    // -------------------------------------------------------------------
    @PostMapping
    public ResponseEntity<PostResponseDTO> createPost(
            @Valid @RequestBody PostRequestDTO dto
    ) {
        log.info("Skapar nytt inlägg med text: {}", dto.text()); // info-logg

        // Skapa nytt Post-objekt via mapper
        Post post = postMapper.toPost(dto);
        // ändrar så den går mot postDTO
        Post savedPost = postRepository.save(post); // Sparar till databasen

        // Skapa response-DTO också via mapper
        PostResponseDTO response = postMapper.toResponseDTO(savedPost);

        log.debug("Post skapad med id {}", post.getId()); // debug-logg

        return ResponseEntity.status(HttpStatus.CREATED).body(response); // 201 Created
    }

    // -------------------------------------------------------------------
    // GET – hämta alla inlägg
    // -------------------------------------------------------------------
    @GetMapping
    public ResponseEntity<List<PostResponseDTO>> getAllPosts() {
        List<Post> posts = postRepository.findAll();
        // Konvertera alla Post → PostResponseDTO
        List<PostResponseDTO> result = posts.stream()
                .sorted(
                        Comparator.comparing(Post::getCreatedAt).reversed()
                )
                .map(p -> new PostResponseDTO(
                        p.getId(), // Skapar ett id baserat på index i listan // tog bort den så att den går direkt på ID
                        p.getText(),
                        p.getCreatedAt(),
                        userMapper.toDto(p.getUser())
                ))
                .toList();

        return ResponseEntity.ok(result); // 200 OK
    }

    // -------------------------------------------------------------------
    // GET – hämta post på index (id)
    // -------------------------------------------------------------------
    @GetMapping("/{id}")
    public ResponseEntity<PostResponseDTO> getPost(@PathVariable Long id) {
        log.info("Hämtar post med id {}", id);
        //använder optional för att säkert kolla om post finns för att undvika nullpointerexception
        Optional<Post> postOptional = postRepository.findById(id);

        if (postOptional.isEmpty()) {
            log.warn("Post med id {} hittades inte", id);
            return ResponseEntity.notFound().build(); // 404 Not Found
        }

        Post post = postOptional.get();
        PostResponseDTO response = postMapper.toResponseDTO(post);
        log.debug("Returnerar post: {}", response);

        return ResponseEntity.ok(response); // 200 OK
    }

    // -------------------------------------------------------------------
    // PUT – uppdatera en Post
    // -------------------------------------------------------------------
    @PutMapping("/{index}")
    public ResponseEntity<PostResponseDTO> updatePost(
            @PathVariable Long id,
            @Valid @RequestBody PostRequestDTO dto
    ) {
        //använder optional för att säkert kolla om post finns för att undvika nullpointerexception
        Optional<Post> postOptional = postRepository.findById(id);

        if (postOptional.isEmpty()) {
            return ResponseEntity.notFound().build(); // 404 Not Found
        }


        Post post = postOptional.get();

        // Uppdatera text (validering sker via DTO)
        post.setText(dto.text());

        // createdAt ändras inte
        Post updatedPost = postRepository.save(post); // Spara uppdateringen
        PostResponseDTO response = postMapper.toResponseDTO(updatedPost);

        return ResponseEntity.ok(response); // 200 OK
    }

    // -------------------------------------------------------------------
    // DELETE – ta bort post
    // -------------------------------------------------------------------
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {

        if (!postRepository.existsById(id)) {
            return ResponseEntity.notFound().build(); // 404 Not Found
        }

        postRepository.deleteById(id); // Ta bort post med id x

        return ResponseEntity.noContent().build(); // 204 No Content
    }
}
