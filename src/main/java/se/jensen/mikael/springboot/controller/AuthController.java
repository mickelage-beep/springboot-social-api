package se.jensen.mikael.springboot.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.jensen.mikael.springboot.dto.LoginRequestDTO;
import se.jensen.mikael.springboot.dto.LoginResponseDTO;
import se.jensen.mikael.springboot.security.MyUserDetails;
import se.jensen.mikael.springboot.service.TokenService;

/**
 * Controller som ansvarar för autentisering (inloggning)
 * Tar emot användarnamn och lösen och
 * skapar och returnerar en JWT-token om inloggningen lyckas.
 */
@RestController
@RequestMapping("/request-token")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    public AuthController(AuthenticationManager am,
                          TokenService ts) {
        this.authenticationManager = am;
        this.tokenService = ts;
    }

    @PostMapping
    public ResponseEntity<LoginResponseDTO> token(
            @RequestBody LoginRequestDTO loginRequest) {

        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.username(),
                        loginRequest.password()
                )
        );
        MyUserDetails details = (MyUserDetails) auth.getPrincipal();


        String token = tokenService.generateToken(auth);

        return ResponseEntity.ok(new LoginResponseDTO(token, details.getDomainUser().getId()));
    }
}

