package se.jensen.mikael.springboot.security;

import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import se.jensen.mikael.springboot.model.User;

import java.util.Collection;
import java.util.List;

/**
 * Implementerar UserDetails, som Spring Security använder
 * Wrappas runt vår egen User-entity
 * Hanterar username, password och authorities (roller)
 * Returnerar alltid true för account-status (kan byggas ut senare)
 */
public class MyUserDetails implements UserDetails {

    // Referens till vår egen User-entity
    private final User user;

    // Konstruktor som tar in en User
    public MyUserDetails(User user) {
        this.user = user;
    }


    @Override
    public String getUsername() {
        return user.getUsername();
    }


    @Override
    public @Nullable String getPassword() {
        return user.getPassword();
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole()));
    }


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


    public User getDomainUser() {
        return user;
    }
}
