package se.jensen.mikael.springboot.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.jensen.mikael.springboot.dto.UserResponseDTO;
import se.jensen.mikael.springboot.model.User;
import se.jensen.mikael.springboot.repository.UserRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

/*
 ============================================================
 GetUserByIdTest
 ============================================================

 Testar UserService.getUser(id)
 - Mockar repository
 - Följer ARRANGE / ACT / ASSERT
*/
@ExtendWith(MockitoExtension.class)
public class GetUserByIdTest {

    // Service vi vill testa
    @InjectMocks
    private UserService userService;

    // Mockade beroenden
    @Mock
    private UserRepository userRepository;


    @Test
    void testGetUserById() {
        // ARRANGE: skapa testdata
        User user = new User();
        user.setId(1L);
        user.setUsername("Micke");


        // Mocka repository
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));


        // ACT: anropa service-metoden
        UserResponseDTO result = userService.getUser(1L);

        // ASSERT: kontrollera resultat
        assertNotNull(result);
        assertEquals(1L, result.id());
        assertEquals("Micke", result.username());
    }
}
