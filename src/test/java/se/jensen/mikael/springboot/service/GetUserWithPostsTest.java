package se.jensen.mikael.springboot.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import se.jensen.mikael.springboot.dto.PostResponseDTO;
import se.jensen.mikael.springboot.dto.UserResponseDTO;
import se.jensen.mikael.springboot.dto.UserWithPostsResponseDTO;
import se.jensen.mikael.springboot.exception.UserNotFoundException;
import se.jensen.mikael.springboot.mapper.UserMapper;
import se.jensen.mikael.springboot.mapper.UserWithPostsMapper;
import se.jensen.mikael.springboot.model.Post;
import se.jensen.mikael.springboot.model.User;
import se.jensen.mikael.springboot.repository.UserRepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GetUserWithPostsTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserWithPostsMapper userWithPostsMapper;

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    /**
     * Testar att UserWithPost retunerar korrekt ResponceDTO
     * när användaren finns och har posts.
     */
    @Test
    public void testGetUserWithPosts() {
        // ARRANGE
        User user = new User();
        user.setId(1L);
        user.setUsername("Linus");

        Post post1 = new Post();
        post1.setId(100L);
        post1.setText("Post 1");
        post1.setCreatedAt(Instant.now());
        post1.setUser(user);

        Post post2 = new Post();
        post2.setId(101L);
        post2.setText("Post 2");
        post2.setCreatedAt(Instant.now());
        post2.setUser(user);

        // DTO-versioner
        PostResponseDTO dto1 = new PostResponseDTO(100L, "Post 1", post1.getCreatedAt(), new UserResponseDTO(1L, "Ali", null, null, null, null, null));
        PostResponseDTO dto2 = new PostResponseDTO(101L, "Post 2", post2.getCreatedAt(), new UserResponseDTO(1L, "Ali", null, null, null, null, null));

        UserWithPostsResponseDTO userWithPostsDTO = new UserWithPostsResponseDTO(
                new UserResponseDTO(1L, "Linus", null, null, null, null, null),
                List.of(dto1, dto2)
        );

        when(userRepository.findUserWithPosts(1L)).thenReturn(Optional.of(user));
        when(userWithPostsMapper.toDto(user)).thenReturn(userWithPostsDTO);

        // ACT
        UserWithPostsResponseDTO result = userService.getUserWithPosts(1L);

        // ASSERT
        assertEquals("Linus", result.user().username());
        assertEquals(2, result.posts().size());
        assertEquals("Post 1", result.posts().get(0).text());
        assertEquals("Post 2", result.posts().get(1).text());
    }

    /**
     * Kollar att det kastar Exception när användare inte finns i databasen.
     */
    @Test
    public void testGetUserWithPosts_NotFound() {
        // ARRANGE
        when(userRepository.findUserWithPosts(1L)).thenReturn(Optional.empty());

        // ACT & ASSERT
        assertThrows(UserNotFoundException.class, () -> userService.getUserWithPosts(1L));
    }
}
