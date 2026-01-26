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

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GetAllUsersTest {
    @InjectMocks
    private UserService userService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserMapper userMapper;
    @Mock
    private PasswordEncoder passwordEncoder;

    /**
     * Test för getAllUsers
     * Kollar att Dto-listan har rätt storlek och värden
     */
    @Test
    public void testGetAllUsers() {
        //ARRANGE
        User user = new User();
        user.setId(1L);
        user.setUsername("Ali");

        User user2 = new User();
        user2.setId(2L);
        user2.setUsername("Guven");

        List<User> users = List.of(user, user2);

        UserResponseDTO dto1 = UserResponseDTOBuilder.builder().withId(1L).withUsername("Ali").build();
        UserResponseDTO dto2 = UserResponseDTOBuilder.builder().withId(2L).withUsername("Guven").build();

        when(userRepository.findAll()).thenReturn(users);

        try (MockedStatic<UserMapper> mocked = org.mockito.Mockito.mockStatic(UserMapper.class)) {
            mocked.when(() -> userMapper.toDto(user)).thenReturn(dto1);
            mocked.when(() -> userMapper.toDto(user2)).thenReturn(dto2);

            //Funka ej eftersom metoden är statisk
//        when(userMapper.toDto(user)).thenReturn(dto1);
//        when(userMapper.toDto(user2)).thenReturn(dto2);

            //ACT
            List<UserResponseDTO> result = userService.getAllUsers();

            //ASSERT
            assertEquals(2, result.size());
            assertEquals("Ali", result.get(0).username());
            assertEquals("Guven", result.get(1).username());
        }


    }

}
