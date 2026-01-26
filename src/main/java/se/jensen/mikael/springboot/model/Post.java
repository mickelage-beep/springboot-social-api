package se.jensen.mikael.springboot.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

/*
 * Post-modellen representerar ett inlägg i systemet.
 * Används tillsammans med JPA/Hibernate för att spara inlägg i databasen.
 */
@Entity // Gör klassen till en JPA-entitet
public class Post {

    // Primärnyckel, auto-genereras av databasen
    // Id används inte mot databas ännu – vi sätter 0L i mappern
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String text; // Själva textinnehållet i inlägget

    // Tidsstämpel när posten skapades
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // Many-to-One relation mot User
    // Varje post tillhör en användare
    @ManyToOne
    @JoinColumn(name = "user_id") // FK i Post-tabellen
    private User user;

    // Konstruktor med user, används för att skapa post kopplad till användare
    public Post(User user) {
        this.user = user;
    }

    // Default-konstruktor krävs av JPA
    public Post() {
    }

    // Getter och setter för user
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    // Konstruktor som används i mapper / test
    public Post(Long id, String text, LocalDateTime createdAt) {
        this.id = id;
        this.text = text;
        this.createdAt = createdAt;
    }

    // -------------------------------
    // GETTERS & SETTERS
    // -------------------------------

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
