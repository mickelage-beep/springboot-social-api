package se.jensen.mikael.springboot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import se.jensen.mikael.springboot.model.User;

import java.util.Optional;

/*
 * UserRepository
 *
 * - Repository för User-entity
 * - Ärver från JpaRepository<User, Long>, vilket automatiskt ger:
 * findAll(), findById(), save(), delete(), etc.
 * - Här kan vi också lägga till egna metoder med Spring Data conventions
 */
public interface UserRepository extends JpaRepository<User, Long> {

    /*
     ============================================================
      findByUsername
      - Hitta en användare baserat på username
      - Returnerar Optional<User> som hanterar om användaren inte finns
     ============================================================
    */
    Optional<User> findByUsername(String username);

    /*
     ============================================================
      existsByUsernameOrEmail
      - Kontrollerar om en användare redan finns med samma username eller email
      - Returnerar true om en matchning finns
      - Används t.ex. Innan man skapar en ny användare
     ============================================================
    */
    boolean existsByUsernameOrEmail(String username, String email);

    /*
     ============================================================
      findUserWithPosts
      - Custom JPQL-query för att hämta en User och samtidigt fetch:a alla posts
      - LEFT JOIN FETCH används för att undvika LazyInitializationException
      - Returnerar Optional<User> så vi kan hantera om user inte finns
     ============================================================
    */
    // la dit ORDER BY p.createdAt DESC för att de
    @Query("SELECT u FROM User u LEFT JOIN FETCH u.posts p WHERE u.id = :id ORDER BY p.createdAt DESC")
    Optional<User> findUserWithPosts(Long id);
}
