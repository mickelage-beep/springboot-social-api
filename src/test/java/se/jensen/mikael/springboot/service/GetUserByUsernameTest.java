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
import se.jensen.mikael.springboot.mapper.UserMapper;
import se.jensen.mikael.springboot.model.User;
import se.jensen.mikael.springboot.repository.UserRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GetUserByUsernameTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper usermapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    /**
     * Testar så jag kan hämta via Username istället
     */
    @Test
    public void testGetUserByUsername() {
        //ARRANGE
        User user = new User();
        user.setId(1L);
        user.setUsername("Ali");
        when(userRepository.findByUsername("Ali")).thenReturn(Optional.of(user));

        UserResponseDTO dto = UserResponseDTOBuilder.builder().withId(1L).withUsername("Ali").build();

        try (MockedStatic<UserMapper> mockStatic = org.mockito.Mockito.mockStatic(UserMapper.class)) {
            mockStatic.when(() -> UserMapper.toDto(user)).thenReturn(dto);

            //ACT
            UserResponseDTO result = userService.getUserByUsername("Ali");
            //ASSERT
            assertEquals("Ali", result.username());
        }
    }

    /**
     * Testar mitt UsernameNotFoundException genom att göra det tomt.
     */
    @Test
    public void testGetUserByUsername_NotFound() {
        when(userRepository.findByUsername("Ali")).thenReturn(Optional.empty());
        assertThrows(org.springframework.security.core.userdetails.UsernameNotFoundException.class,
                () -> userService.getUserByUsername("Ali"));
    }
}
