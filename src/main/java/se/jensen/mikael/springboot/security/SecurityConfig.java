package se.jensen.mikael.springboot.security;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

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


        //        /// Testar för endpoints
//        http.authorizeHttpRequests(auth -> auth
//                .anyRequest().permitAll() // ALLA endpoints är publika
//        );
//        http.csrf(csrf -> csrf.disable());


        /// JWT
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(s ->
                        s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/request-token",
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/swagger-ui.html"
                        ).permitAll()
                        .anyRequest().authenticated()
                )

                .oauth2ResourceServer(oauth2 ->
                        oauth2.jwt(jwt ->
                                jwt.jwtAuthenticationConverter(
                                        jwtAuthenticationConverter())));


        // ------------------------------------------------------------
        // Stäng av CSRF-skydd
        // - Behövs inte för REST-API med Basic Auth
        // ------------------------------------------------------------

//        http.csrf(csrf -> csrf.disable());
//
//        // ------------------------------------------------------------
//        // Authorization rules
//        // ------------------------------------------------------------
//
//
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


    @Bean
    public KeyPair keyPair(
            @Value("${JWT_PRIVATE_KEY}") String privateKey,
            @Value("${JWT_PUBLIC_KEY}") String publicKey
    ) throws Exception {
        byte[] privateBytes = Base64.getDecoder().decode(privateKey);
        byte[] publicBytes = Base64.getDecoder().decode(publicKey);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privKey = keyFactory.generatePrivate(
                new PKCS8EncodedKeySpec(privateBytes)
        );
        PublicKey pubKey = keyFactory.generatePublic(
                new X509EncodedKeySpec(publicBytes)
        );
        return new KeyPair(pubKey, privKey);
    }

    @Bean
    public JWKSource<SecurityContext> jwkSource(KeyPair keyPair) {
        RSAKey rsaKey = new RSAKey.Builder((RSAPublicKey) keyPair.getPublic())
                .privateKey((RSAPrivateKey) keyPair.getPrivate())
                .keyID("jwt-key-1")
                .build();
        JWKSet jwkSet = new JWKSet(rsaKey);
        return (jwkSelector, context) -> jwkSelector.select(jwkSet);
    }

    @Bean
    public JwtEncoder jwtEncoder(JWKSource<SecurityContext> jwkSource) {
        return new NimbusJwtEncoder(jwkSource);
    }

    @Bean
    public JwtDecoder jwtDecoder(KeyPair keyPair) {
        return NimbusJwtDecoder
                .withPublicKey((RSAPublicKey) keyPair.getPublic())
                .build();
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter converter =
                new JwtGrantedAuthoritiesConverter();
        converter.setAuthorityPrefix("");
        converter.setAuthoritiesClaimName("scope");
        JwtAuthenticationConverter authenticationConverter =
                new JwtAuthenticationConverter();
        authenticationConverter.setJwtGrantedAuthoritiesConverter(converter);
        return authenticationConverter;
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }


}
