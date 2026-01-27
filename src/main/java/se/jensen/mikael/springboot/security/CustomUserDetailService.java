package se.jensen.mikael.springboot.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import se.jensen.mikael.springboot.model.User;
import se.jensen.mikael.springboot.repository.UserRepository;

/**
 * CustomUserDetailService används av Spring Security för autentisering.
 * Klassen hämtar en användare från databasen baserat på användarnamn
 * och returnerar ett UserDetails-objekt som Spring Security kan använda.
 */
@Service
public class CustomUserDetailService implements UserDetailsService {

    // Repository för att hämta användare från databasen
    private final UserRepository userRepository;

    // Konstruktor för Dependency Injection
    public CustomUserDetailService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Hämtar en användare från databasen baserat på username.
     * Anropas automatiskt av Spring Security vid login.
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
