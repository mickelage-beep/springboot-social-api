package se.jensen.mikael.springboot.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import se.jensen.mikael.springboot.dto.UserRequestDTO;
import se.jensen.mikael.springboot.dto.UserResponseDTO;
import se.jensen.mikael.springboot.model.User;
import se.jensen.mikael.springboot.repository.UserRepository;

import static org.junit.jupiter.api.Assertions.*;


/*
 ============================================================
  Integrationstest med TestRestTemplate
 ============================================================

  Denna testklass kör hela Spring Boot-applikationen
  på en slumpad port (RANDOM_PORT) och testar REST-endpoints
  med riktiga HTTP-anrop mot controller + service + repository.

  Vi använder TestRestTemplate:
  - Enkel HTTP-klient för integrationstester
  - Kan hantera Basic Auth
  - Perfekt för REST-testning av Spring MVC-applikationer
*/
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test") // Använder testprofil (t.ex. H2 DB)
class UserControllerTestRestTemplate {

    /*
     ------------------------------------------------------------
      Port som Spring Boot startar den inbyggda servern på
     ------------------------------------------------------------
    */
    @LocalServerPort
    private int port;

    /*
     ------------------------------------------------------------
      Repository för att manipulera / verifiera data i databasen
     ------------------------------------------------------------
    */
    @Autowired
    private UserRepository userRepository;

    /*
     ------------------------------------------------------------
      PasswordEncoder
      - Behövs för att hash:a lösenord så att Basic Auth fungerar
     ------------------------------------------------------------
    */
    @Autowired
    private PasswordEncoder passwordEncoder;

    /*
     ------------------------------------------------------------
      TestRestTemplate
      - HTTP-klient som gör riktiga anrop mot vår server
      - Kan hantera Basic Auth
     ------------------------------------------------------------
    */
    private TestRestTemplate restTemplate;

    /*
     ------------------------------------------------------------
      Hjälpmetod för bas-URL till /users
      - Vi slipper skriva port och localhost varje gång
     ------------------------------------------------------------
    */
    private String baseUrl() {
        return "http://localhost:" + port + "/users";
    }

    /*
     ============================================================
      SETUP – körs före varje test
      - Rensar databasen
      - Skapar admin-användare för autentisering
      - Initierar TestRestTemplate med Basic Auth
     ============================================================
    */
    @BeforeEach
    void setUp() {
        // Rensa alla användare innan varje test
        userRepository.deleteAll();

        // Skapa admin-användare
        User admin = new User();
        admin.setUsername("admin");
        admin.setPassword(passwordEncoder.encode("password")); // hash:a lösenord
        admin.setRole("ADMIN");
        admin.setEmail("admin@test.com");
        admin.setDisplayName("Admin Display");
        admin.setBio("Admin Bio");

        // Spara admin i databasen
        userRepository.save(admin);

        // Initiera TestRestTemplate med admin credentials
        restTemplate = new TestRestTemplate("admin", "password");
    }

    /*
     ============================================================
      TEST 1: GET /users
      - Testar att admin kan hämta alla användare
      - Kontrollerar HTTP-status och response body
     ============================================================
    */
    @Test
    void shouldGetAllUsers() {
        // Skicka GET-anrop mot /users
        ResponseEntity<UserResponseDTO[]> response = restTemplate.getForEntity(baseUrl(), UserResponseDTO[].class);

        // Kontrollera att status är 200 OK
        assertEquals(200, response.getStatusCode().value());

        // Kontrollera att response-body inte är null
        assertNotNull(response.getBody());

        // Kontrollera att vi har exakt 1 användare
        assertEquals(1, response.getBody().length);

        // Kontrollera att användarnamnet är admin
        assertEquals("admin", response.getBody()[0].username());
    }

    /*
     ============================================================
      TEST 2: POST /users
      - Testar att admin kan skapa en ny användare
      - Kontrollerar att status 201 Created returneras
      - Kontrollerar att response body innehåller rätt data
     ============================================================
    */
    @Test
    void shouldCreateUser() {
        // Skapa en DTO för ny användare
        UserRequestDTO newUser = new UserRequestDTO(
                "newuser",        // username
                "new@user.com",   // email
                "secret",         // password
                "USER",           // role
                "New User",       // displayName
                "New bio",        // bio
                null              // profileImagePath (kan vara null)
        );

        // Skicka POST-anrop med DTO som JSON
        ResponseEntity<UserResponseDTO> response = restTemplate.postForEntity(baseUrl(), newUser, UserResponseDTO.class);

        // Kontrollera att status är 201 Created
        assertEquals(201, response.getStatusCode().value());

        // Kontrollera att response body inte är null
        assertNotNull(response.getBody());

        // Kontrollera att användarnamnet matchar
        assertEquals("newuser", response.getBody().username());
    }

    /*
     ============================================================
      TEST 3: PUT /users/{id}
      - Testar att admin kan uppdatera en användare
      - Kontrollerar att ändringar sparas korrekt
     ============================================================
    */
    @Test
    void shouldUpdateUser() {
        // Hämta befintlig användare
        User existingUser = userRepository.findAll().get(0);

        // Skapa DTO med uppdaterade fält
        UserRequestDTO updatedUser = new UserRequestDTO(
                existingUser.getUsername(),
                existingUser.getEmail(),
                "password",
                existingUser.getRole(),
                "Updated Display",  // ny displayName
                "Updated Bio",      // ny bio
                null
        );

        // Skapa headers med Basic Auth
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth("admin", "password");

        // Kombinera body + headers
        HttpEntity<UserRequestDTO> requestEntity = new HttpEntity<>(updatedUser, headers);

        // Skicka PUT-anrop mot /users/{id}
        ResponseEntity<UserResponseDTO> response = restTemplate.exchange(
                baseUrl() + "/" + existingUser.getId(),
                HttpMethod.PUT,
                requestEntity,
                UserResponseDTO.class
        );

        // Hämta uppdaterad användare från response
        UserResponseDTO updated = response.getBody();
        assertNotNull(updated);

        // Kontrollera att fälten uppdaterats
        assertEquals("Updated Display", updated.displayName());
        assertEquals("Updated Bio", updated.bio());
    }

    /*
     ============================================================
      TEST 4: DELETE /users/{id}
      - Testar att admin kan radera en användare
      - Kontrollerar status 204 No Content
      - Verifierar att användaren är borttagen från DB
     ============================================================
    */
    @Test
    void shouldDeleteUser() {
        // Hämta användare som ska tas bort
        User user = userRepository.findAll().get(0);

        // Skapa headers med Basic Auth
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth("admin", "password");
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        // Skicka DELETE-anrop
        ResponseEntity<Void> response = restTemplate.exchange(
                baseUrl() + "/" + user.getId(),
                HttpMethod.DELETE,
                requestEntity,
                Void.class
        );

        // Kontrollera att status är 204 No Content
        assertEquals(204, response.getStatusCode().value());

        // Kontrollera att användaren verkligen är borttagen från DB
        assertTrue(userRepository.findById(user.getId()).isEmpty());
    }
}
