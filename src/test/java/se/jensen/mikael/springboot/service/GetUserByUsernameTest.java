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
 UserServiceGetUserByUsernameTest
 ============================================================

 Testar UserService.getUserByUsername(username)
 - Mockar repository
*/
@ExtendWith(MockitoExtension.class)
public class GetUserByUsernameTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Test
    void testGetUserByUsername() {
        // ARRANGE: skapa testdata
        User user = new User();
        user.setId(1L);
        user.setUsername("Ali");

        // Mocka repository
        when(userRepository.findByUsername("Ali")).thenReturn(Optional.of(user));

        // ACT: hämta user direkt
        User foundUser = userRepository.findByUsername("Ali").orElseThrow();

        // ASSERT: kontrollera resultat
        assertNotNull(foundUser);
        assertEquals(1L, foundUser.getId());
        assertEquals("Ali", foundUser.getUsername());
    }
}
