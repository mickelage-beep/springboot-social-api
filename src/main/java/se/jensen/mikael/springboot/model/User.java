package se.jensen.mikael.springboot.model;

import jakarta.persistence.*;

import java.util.List;

/**
 * User-modellen representerar en användare i applikationen.
 * DTO:er används för in/utdata i API:t.
 */
@Entity                         // Gör klassen till en JPA-entitet
@Table(name = "app_user")       // Kopplar entiteten till tabellen "app_user" i databasen
public class User {

    @Id                                                     // Markerar primärnyckel
    @GeneratedValue(strategy = GenerationType.IDENTITY)     // Auto-increment i MySQL
    private Long id;

    @Column(unique = true, nullable = false)               // Unikt och inte null
    private String username;

    @Column(unique = true, nullable = false)               // Unikt och inte null
    private String email;

    @Column(nullable = false)                               // Måste finnas
    private String password;

    @Column(nullable = false)
    private String role;

    @Column(nullable = false)
    private String bio;

    @Column(name = "display_name", nullable = false)       // Kolumnnamn skillt från fältnamn
    private String displayName;

    @Column(name = "profile_image_path")                   // Får vara null
    private String profileImagePath;

    /**
     * Relation till Post-entity
     * En User kan ha många Posts
     * mappedBy="user" anger fältet i Post som äger relationen
     * cascade = REMOVE → radera alla posts när användaren tas bort
     */
    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<Post> posts;

    // -----------------------------------------------------------
    // Konstruktorer
    // -----------------------------------------------------------

    public User(List<Post> posts) {
        this.posts = posts;
    }

    public User() {
        // Default-konstruktor behövs för JPA
    }

    public User(Long id, String username, String password, String role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public User(Long id, String username, String email, String password, String role, String bio, String displayName, String profileImagePath) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
        this.bio = bio;
        this.displayName = displayName;
        this.profileImagePath = profileImagePath;
    }

    // -----------------------------------------------------------
    // Getter & Setter – används för att kunna uppdatera värden
    // -----------------------------------------------------------

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getProfileImagePath() {
        return profileImagePath;
    }

    public void setProfileImagePath(String profileImagePath) {
        this.profileImagePath = profileImagePath;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }
}
