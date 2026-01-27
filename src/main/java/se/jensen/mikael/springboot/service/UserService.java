package se.jensen.mikael.springboot.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import se.jensen.mikael.springboot.dto.UserRequestDTO;
import se.jensen.mikael.springboot.dto.UserResponseDTO;
import se.jensen.mikael.springboot.dto.UserWithPostsResponseDTO;
import se.jensen.mikael.springboot.exception.UserAlreadyExistsException;
import se.jensen.mikael.springboot.exception.UserNotFoundException;
import se.jensen.mikael.springboot.mapper.UserMapper;
import se.jensen.mikael.springboot.mapper.UserWithPostsMapper;
import se.jensen.mikael.springboot.model.User;
import se.jensen.mikael.springboot.repository.UserRepository;

import java.util.List;

/*
 ============================================================
  UserService
 ============================================================

  - Innehåller all business-logic för Users
  - Kommunicerar med UserRepository för databasoperationer
  - Mapperar mellan Entity och DTO
  - Hanterar CRUD (Create, Read, Update, Delete) + speciella endpoints
*/
@Service
public class UserService {

    // Logger
    private static final Logger logger =
            LoggerFactory.getLogger(UserService.class);

    // Repository för att läsa/spara users i databasen
    private final UserRepository userRepository;

    // Mapperar mellan DTO och entity
    private final UserMapper userMapper;

    // Hashar lösenord innan sparning
    private final PasswordEncoder passwordEncoder;

    // Mapper mellan DTO entity
    private final UserWithPostsMapper userWithPostsMapper;

    /*
      Konstruktor för Dependency Injection
      - Spring autowirar repository, mapper och encoder
    */
    public UserService(UserRepository userRepository, UserMapper userMapper, PasswordEncoder passwordEncoder, UserWithPostsMapper userWithPostsMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.userWithPostsMapper = userWithPostsMapper;
    }

    // -----------------------------------------------------------
    // CREATE
    // -----------------------------------------------------------
    public UserResponseDTO addUser(UserRequestDTO dto) {

        // Kontrollera om username eller email redan finns
        boolean exists = userRepository.existsByUsernameOrEmail(
                dto.username(),
                dto.email()
        );

        // Om user finns, kasta exception
        if (exists) {
            throw new UserAlreadyExistsException(
                    "User med detta username och/eller email finns redan i databasen"
            );
        }

        // Skapa User entity från DTO
        User user = userMapper.fromDto(dto);


        // Här hash:as lösenordet
        user.setPassword(passwordEncoder.encode(dto.password()));

        // Spara user i DB
        User saved = userRepository.save(user);

        // Logga användaren som skapas
        logger.info("User created with username: " + saved.getUsername());

        // Returnera DTO för response
        return userMapper.toDto(saved);
    }

    // -----------------------------------------------------------
    // READ ALL
    // -----------------------------------------------------------
    public List<UserResponseDTO> getAllUsers() {

        // Hämta alla Users från DB
        List<User> users = userRepository.findAll();

        // Mappa User-lista till DTO-lista
        return users.stream()
                .map(UserMapper::toDto)
                .toList();
    }

    // -----------------------------------------------------------
    // READ ONE
    // -----------------------------------------------------------
    public UserResponseDTO getUser(Long id) {

        // Hämta user från DB, kasta exception om den inte finns med logger.
        User user = userRepository.findById(id)
                .orElseThrow(() ->{
                    logger.warn("User not found with id: " + id);
                    return new UserNotFoundException("Ingen user i databasen med id: " + id);
                });

        return userMapper.toDto(user);
    }

    public UserResponseDTO getUserByUsername(String username) {
        // Hämta user via username & kasta exception om den inte finns med logger.
        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->{
                    logger.warn("User " + username + " not found.");
                    return new UsernameNotFoundException("User " + username + " hittades ej.");
                });


        return userMapper.toDto(user);
    }

    // -----------------------------------------------------------
    // UPDATE
    // -----------------------------------------------------------
    public UserResponseDTO updateUser(Long id, UserRequestDTO dto) {

        // Hämta user från DB, kasta exception om den inte finns med logger.
        User existing = userRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("User not found with id: " + id);
                    return new UserNotFoundException("Ingen user i databasen med id: " + id);
                });

        // Uppdatera fält baserat på DTO
        UserMapper.updateUserFromDto(existing, dto);

        //Hasha nytt lösenord
        existing.setPassword(passwordEncoder.encode(dto.password()));

        // Spara ändringarna (UPDATE sker automatiskt i DB)
        User updated = userRepository.save(existing);

        // Logga att användaren uppdateras vid sparande
        logger.info("User updated with id: " + id);

        // Returnera DTO
        return userMapper.toDto(updated);
    }

    // -----------------------------------------------------------
    // DELETE
    // -----------------------------------------------------------
    public void deleteUser(Long id) {

        // Kontrollera om user finns, kasta exception med logger om user inte finns.
        if (!userRepository.existsById(id)) {
            logger.warn("User not found with id: " + id);
            throw new UserNotFoundException("Ingen user i databasen med id: " + id);
        }

        // Radera user från DB
        userRepository.deleteById(id);

        // Logga att användaren är borttagen
        logger.info("User deleted with id: " + id);
    }

    // -----------------------------------------------------------
    // GET USER WITH POSTS
    // -----------------------------------------------------------
    public UserWithPostsResponseDTO getUserWithPosts(Long id) {

        // Hämta user med posts (custom query i repository), kasta exception med logger om user inte finns.
        User user = userRepository.findUserWithPosts(id)
                .orElseThrow(() -> {
                    logger.warn("User not found with id: " + id);
                    return new UserNotFoundException("User not found with id: " + id);
                });

        return userWithPostsMapper.toDto(user);
    }
}
