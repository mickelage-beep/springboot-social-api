package se.jensen.mikael.springboot.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import se.jensen.mikael.springboot.dto.UserResponseDTO;
import se.jensen.mikael.springboot.dto.UserResponseDTOBuilder;
import se.jensen.mikael.springboot.exception.UserNotFoundException;
import se.jensen.mikael.springboot.mapper.UserMapper;
import se.jensen.mikael.springboot.model.User;
import se.jensen.mikael.springboot.repository.UserRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GetUserByIdTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    /**
     * Testar så jag kan hämta UserById
     */
    @Test
    void testGetUserById() {
        // ARRANGE
        User user = new User();
        user.setId(1L);
        user.setUsername("Micke");
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        UserResponseDTO dto = UserResponseDTOBuilder.builder()
                .withId(1L)
                .withUsername("Micke")
                .build();

        try (MockedStatic<UserMapper> mockStatic = org.mockito.Mockito.mockStatic(UserMapper.class)) {
            mockStatic.when(() -> UserMapper.toDto(user)).thenReturn(dto);
            //when(userRepository.findById(1L)).thenReturn(Optional.of(user));
            //when(userMapper.toDto(user)).thenReturn(dto);

            // ACT
            UserResponseDTO result = userService.getUser(1L);

            // ASSERT
            assertNotNull(result);
            assertEquals(1L, result.id());
            assertEquals("Micke", result.username());
        }
    }

    /**
     * Testar mitt UserNotFoundException ifall id:t är tomt.
     */
    @Test
    public void testGetUser_NotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> userService.getUser(1L));
    }
}
