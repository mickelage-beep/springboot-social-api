package se.jensen.mikael.springboot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.jensen.mikael.springboot.model.Post;

/**
 * Repository-gränssnitt för Post-entiteten.
 * Ärver från JpaRepository<Post, Long> vilket ger oss:
 * CRUD-metoder (save, findById, findAll, delete, etc.)
 * <p>
 * Paging och sortering (via metoder i JpaRepository)
 * Vi behöver inte implementera metoderna själva – Spring Data JPA
 * genererar implementationen automatiskt vid runtime.
 */
public interface PostRepository extends JpaRepository<Post, Long> {
    // Här kan man senare lägga till egna query-metoder om det behövs
}
