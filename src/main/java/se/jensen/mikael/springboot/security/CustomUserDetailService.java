package se.jensen.mikael.springboot.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import se.jensen.mikael.springboot.model.User;
import se.jensen.mikael.springboot.repository.UserRepository;

/*
 ============================================================
  CustomUserDetailService
 ============================================================

  - Implementerar UserDetailsService som används av Spring Security
  - Ansvarar för att hämta en användare från databasen vid login
  - Wrappar User-entityn i MyUserDetails som implementerar UserDetails
*/
@Service
public class CustomUserDetailService implements UserDetailsService {

    // Repository för att hämta användare från databasen
    private final UserRepository userRepository;

    // Konstruktor för Dependency Injection
    public CustomUserDetailService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /*
     ============================================================
      loadUserByUsername
      - Anropas automatiskt av Spring Security vid autentisering
      - Hämtar user via username
      - Kastar UsernameNotFoundException om användaren inte finns
      - Returnerar MyUserDetails som innehåller username, password och authorities
     ============================================================
    */
    @Override
    public UserDetails loadUserByUsername(String username) {
        // Hämta user från DB
        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found: " + username));

        // Returnera vår wrapper MyUserDetails
        return new MyUserDetails(user);
    }
}
