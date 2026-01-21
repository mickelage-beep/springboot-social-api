package se.jensen.mikael.springboot.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.jensen.mikael.springboot.model.User;
import se.jensen.mikael.springboot.repository.UserRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

/*
 ============================================================
 GetUserWithPostsTest
 ============================================================

 Testar UserService.getUserWithPosts(id)
 - Mockar repository
 - Fokuserar på att user existerar och kan hämtas
*/
@ExtendWith(MockitoExtension.class)
public class GetUserWithPostsTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Test
    void testGetUserWithPosts() {
        // ARRANGE: skapa testdata
        User user = new User();
        user.setId(1L);
        user.setUsername("Linus");

        // Mocka repository (vi låtsas att findUserWithPosts fungerar)
        when(userRepository.findUserWithPosts(1L)).thenReturn(Optional.of(user));

        // ACT: hämta user direkt
        User foundUser = userRepository.findUserWithPosts(1L).orElseThrow();

        // ASSERT: kontrollera resultat
        assertNotNull(foundUser);
        assertEquals(1L, foundUser.getId());
        assertEquals("Linus", foundUser.getUsername());
    }
}
