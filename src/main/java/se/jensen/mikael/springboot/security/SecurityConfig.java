package se.jensen.mikael.springboot.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/*
 ============================================================
  SecurityConfig
 ============================================================

  - Konfigurerar Spring Security för applikationen
  - Definierar vilka endpoints som är publika, vilka som kräver admin/user
  - Slår på Basic Auth och Form Login
  - Hashar lösenord med BCrypt
*/
@EnableMethodSecurity // Aktiverar @PreAuthorize och @PostAuthorize på metoder
@Configuration        // Spring vet att detta är en konfigurationsklass
public class SecurityConfig {

    /*
     ============================================================
      SecurityFilterChain Bean
      - Här definieras alla regler för HTTP-säkerhet
     ============================================================
    */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        // ------------------------------------------------------------
        // Stäng av CSRF-skydd
        // - Behövs inte för REST-API med Basic Auth
        // ------------------------------------------------------------
        http.csrf(csrf -> csrf.disable());

        // ------------------------------------------------------------
        // Authorization rules
        // ------------------------------------------------------------

        /// Testar för endpoints
        http.authorizeHttpRequests(auth -> auth
                .anyRequest().permitAll() // ALLA endpoints är publika
        );
        http.csrf(csrf -> csrf.disable());

        ///Kommentear ut tillfälligt
//        http.authorizeHttpRequests(auth -> auth
//
//                        // Alla kan skapa användare (POST /users)
//                        .requestMatchers(HttpMethod.POST, "/users").permitAll()
//
//                        // Tillgång till Swagger UI och API-dokumentation utan autentisering
//                        .requestMatchers(
//                                "/swagger-ui/**",
//                                "/v3/api-docs/**",
//                                "/swagger-ui.html"
//                        ).permitAll()
//
//                        // Publika endpoints under /public/** kräver ingen autentisering
//                        .requestMatchers("/public/**").permitAll()
//
//                        // Endpoints under /admin/** kräver ADMIN-roll
//                        .requestMatchers("/admin/**").hasRole("ADMIN")
//
//                        // Endpoints under /users/** kan nås av både USER och ADMIN
//                        .requestMatchers("/users/**").hasAnyRole("USER", "ADMIN")
//
//                        // Alla andra endpoints kräver autentisering
//                        .anyRequest().authenticated()
//                )
//                // ------------------------------------------------------------
//                // Autentisering med Basic Auth
//                // ------------------------------------------------------------
//                .httpBasic(Customizer.withDefaults())
//
//                // ------------------------------------------------------------
//                // Form Login (webbformulär för login)
//                // ------------------------------------------------------------
//                .formLogin(Customizer.withDefaults());

        // Bygg och returnera SecurityFilterChain
        return http.build();
    }

    /*
     ============================================================
      PasswordEncoder Bean
      - Används för att hash:a lösenord vid skapande och jämförelse
      - BCrypt är säkert och rekommenderat
     ============================================================
    */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
