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
import se.jensen.mikael.springboot.model.Post;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/*
 * PostController
 * - Hanterar alla HTTP-anrop relaterade till Post
 * - Simulerar en enkel "databas" med List<Post>
 * - Använder PostMapper för att mappa mellan Post och DTO
 * - Returnerar ResponseEntity med korrekt HTTP-status
 */
@RestController
@RequestMapping("/posts") // Bas-URL för alla endpoints i denna controller
public class PostController {

    /*
     * "Låtsas-databas" – en lista med Post-objekt.
     * Vi använder tidigare List<String> men nu med Post-modellen.
     */
    private static final Logger log = LoggerFactory.getLogger(PostController.class);

    private List<Post> posts = new ArrayList<>();

    private final PostMapper postMapper; // Mapper injiceras via DI

    // Konstruktor som tar emot mappern. Spring sköter instansiering automatiskt
    public PostController(PostMapper postMapper) {
        this.postMapper = postMapper;
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

        posts.add(post); // Lägg in i listan ("databasen")

        // Skapa response-DTO också via mapper
        PostResponseDTO response = postMapper.toResponseDTO(post);

        log.debug("Post skapad med id {}", post.getId()); // debug-logg

        return ResponseEntity.status(HttpStatus.CREATED).body(response); // 201 Created
    }

    // -------------------------------------------------------------------
    // GET – hämta alla inlägg
    // -------------------------------------------------------------------
    @GetMapping
    public ResponseEntity<List<PostResponseDTO>> getAllPosts() {

        // Konvertera alla Post → PostResponseDTO
        List<PostResponseDTO> result = posts.stream()
                .sorted(
                        Comparator.comparing(Post::getCreatedAt).reversed()
                )
                .map(p -> new PostResponseDTO(
                        (long) posts.indexOf(p), // Skapar ett id baserat på index i listan
                        p.getText(),
                        p.getCreatedAt()
                ))
                .toList();

        return ResponseEntity.ok(result); // 200 OK
    }

    // -------------------------------------------------------------------
    // GET – hämta post på index (id)
    // -------------------------------------------------------------------
    @GetMapping("/{index}")
    public ResponseEntity<PostResponseDTO> getPost(@PathVariable int index) {
        log.info("Hämtar post med index {}", index);

        if (index < 0 || index >= posts.size()) {
            log.warn("Felaktigt index: {}", index); // varning logg
            return ResponseEntity.notFound().build(); // 404 Not Found
        }

        Post p = posts.get(index);

        PostResponseDTO response = postMapper.toResponseDTO(posts.get(index));
        log.debug("Returnerar post: {}", response);

        return ResponseEntity.ok(response); // 200 OK
    }

    // -------------------------------------------------------------------
    // PUT – uppdatera en Post
    // -------------------------------------------------------------------
    @PutMapping("/{index}")
    public ResponseEntity<PostResponseDTO> updatePost(
            @PathVariable int index,
            @Valid @RequestBody PostRequestDTO dto
    ) {
        if (index < 0 || index >= posts.size()) {
            return ResponseEntity.notFound().build(); // 404 Not Found
        }

        Post post = posts.get(index);

        // Uppdatera text (validering sker via DTO)
        post.setText(dto.text());

        // createdAt ändras inte

        PostResponseDTO response = postMapper.toResponseDTO(posts.get(index));

        return ResponseEntity.ok(response); // 200 OK
    }

    // -------------------------------------------------------------------
    // DELETE – ta bort post
    // -------------------------------------------------------------------
    @DeleteMapping("/{index}")
    public ResponseEntity<Void> deletePost(@PathVariable int index) {

        if (index < 0 || index >= posts.size()) {
            return ResponseEntity.notFound().build(); // 404 Not Found
        }

        posts.remove(index); // Ta bort post från listan

        return ResponseEntity.noContent().build(); // 204 No Content
    }
}
