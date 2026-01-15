package se.jensen.mikael.springboot.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import se.jensen.mikael.springboot.dto.*;
import se.jensen.mikael.springboot.service.PostService;
import se.jensen.mikael.springboot.service.UserService;

import java.util.List;

/*
 * UserController
 * - REST-controller som hanterar alla HTTP-anrop relaterade till User
 * - Använder UserService och PostService för logik och databasinteraktion
 * - Alla endpoints returnerar ResponseEntity<T> med korrekt HTTP-status
 */
@RestController
@RequestMapping("/users") // Bas-URL för alla endpoints i denna controller
public class UserController {

    private final UserService userService; // Service för User-relaterad logik
    private final PostService postService; // Service för Post-relaterad logik

    // Konstruktor – dependency injection av services
    public UserController(UserService userService, PostService postService) {
        this.userService = userService;
        this.postService = postService;
    }

    // -----------------------------------------------------------
    // CREATE
    // -----------------------------------------------------------

    /*
     * Skapar en ny användare
     * - POST /users
     * - Validerar DTO med @Valid
     * - Returnerar UserResponseDTO med status 201 CREATED
     */
    @PostMapping()
    public ResponseEntity<UserResponseDTO> createUser(
            @Valid @RequestBody UserRequestDTO dto) {

        UserResponseDTO response = userService.addUser(dto); // Lägger till user via service

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /*
     * Skapar en post för en specifik användare
     * - POST /users/{userId}/posts
     * - Validerar PostRequestDTO
     * - Returnerar PostResponseDTO med status 201 CREATED
     */
    @PostMapping("/{userId}/posts")
    public ResponseEntity<PostResponseDTO> createPostUser(
            @PathVariable Long userId,
            @Valid @RequestBody PostRequestDTO request) {
        PostResponseDTO responseDTO = postService.createPost(userId, request);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    // -----------------------------------------------------------
    // READ ALL
    // -----------------------------------------------------------

    /*
     * Hämtar alla användare
     * - GET /users
     * - Endast ADMIN får göra detta (@PreAuthorize)
     * - Returnerar lista av UserResponseDTO med status 200 OK
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {

        List<UserResponseDTO> result = userService.getAllUsers();

        return ResponseEntity.ok(result);
    }

    // -----------------------------------------------------------
    // READ ONE
    // -----------------------------------------------------------

    /*
     * Hämtar en användare via id
     * - GET /users/{id}
     * - Endast ADMIN
     * - Returnerar UserResponseDTO med status 200 OK
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUser(@PathVariable Long id) {

        UserResponseDTO response = userService.getUser(id);

        return ResponseEntity.ok(response);
    }

    /*
     * Hämtar en användare tillsammans med alla dess poster
     * - GET /users/{id}/with-posts
     * - Returnerar UserWithPostsResponseDTO med status 200 OK
     */
    @GetMapping("/{id}/with-posts")
    public ResponseEntity<UserWithPostsResponseDTO> getUserWithPosts(@PathVariable Long id) {
        UserWithPostsResponseDTO response = userService.getUserWithPosts(id);
        return ResponseEntity.ok(response);
    }

    // -----------------------------------------------------------
    // READ ME
    // -----------------------------------------------------------

    /*
     * Hämtar info om inloggad användare
     * - GET /users/me
     * - Endast USER eller ADMIN kan nå denna endpoint
     * - Hämtar användarnamn från Authentication och returnerar UserResponseDTO
     */
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/me")
    public ResponseEntity<UserResponseDTO> getMe(Authentication authentication) {
        String username = authentication.getName();
        UserResponseDTO userResponseDTO = userService.getUserByUsername(username);

        return ResponseEntity.ok(userResponseDTO);
    }

    // -----------------------------------------------------------
    // UPDATE
    // -----------------------------------------------------------

    /*
     * Uppdaterar en användare
     * - PUT /users/{id}
     * - Tar emot UserRequestDTO via @RequestBody
     * - Returnerar uppdaterad UserResponseDTO med status 200 OK
     */
    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UserRequestDTO dto) {

        UserResponseDTO response = userService.updateUser(id, dto);

        return ResponseEntity.ok(response);
    }

    // -----------------------------------------------------------
    // DELETE
    // -----------------------------------------------------------

    /*
     * Raderar en användare
     * - DELETE /users/{id}
     * - Returnerar status 204 NO CONTENT vid lyckad radering
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {

        userService.deleteUser(id);

        return ResponseEntity.noContent().build();
    }
}
