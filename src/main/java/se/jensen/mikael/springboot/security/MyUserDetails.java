package se.jensen.mikael.springboot.security;

import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import se.jensen.mikael.springboot.model.User;

import java.util.Collection;
import java.util.List;

/*
 ============================================================
  MyUserDetails
 ============================================================

  - Implementerar UserDetails, som Spring Security använder
  - Wrappas runt vår egen User-entity
  - Hanterar username, password och authorities (roller)
  - Returnerar alltid true för account-status (kan byggas ut senare)
*/
public class MyUserDetails implements UserDetails {

    // Referens till vår egen User-entity
    private final User user;

    // Konstruktor som tar in en User
    public MyUserDetails(User user) {
        this.user = user;
    }

    /*
     ============================================================
      Username
      - Returnerar användarnamnet som används av Spring Security
     ============================================================
    */
    @Override
    public String getUsername() {
        return user.getUsername();
    }

    /*
     ============================================================
      Password
      - Returnerar det hashade lösenordet
      - Kan vara null (därav @Nullable)
     ============================================================
    */
    @Override
    public @Nullable String getPassword() {
        return user.getPassword();
    }

    /*
     ============================================================
      Authorities / Roller
      - Returnerar en lista med GrantedAuthority
      - Prefix "ROLE_" krävs av Spring Security
     ============================================================
    */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole()));
    }

    /*
     ============================================================
      Account status
      - Alla returnerar true för nu
      - Kan byggas ut med logik för expired, locked etc.
     ============================================================
    */
    @Override
    public boolean isAccountNonExpired() {
        return true; // kontot är ej utgånget
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // kontot är ej låst
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // inloggningsuppgifter är ej utgångna
    }

    @Override
    public boolean isEnabled() {
        return true; // kontot är aktivt
    }

    /*
     ============================================================
      Get domain User
      - Returnerar original User-entityn
      - Användbart om man behöver fler fält än UserDetails tillhandahåller
     ============================================================
    */
    public User getDomainUser() {
        return user;
    }
}
