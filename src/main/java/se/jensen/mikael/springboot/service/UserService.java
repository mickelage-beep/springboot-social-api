package se.jensen.mikael.springboot.service;

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
                .map(userMapper::toDto)
                .toList();
    }

    // -----------------------------------------------------------
    // READ ONE
    // -----------------------------------------------------------
    public UserResponseDTO getUser(Long id) {

        // Hämta user från DB, kasta exception om den inte finns
        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new UserNotFoundException("Ingen user i databasen med id: " + id));

        return userMapper.toDto(user);
    }

    public UserResponseDTO getUserByUsername(String username) {
        // Hämta user via username
        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found: " + username));
        return userMapper.toDto(user);
    }

    // -----------------------------------------------------------
    // UPDATE
    // -----------------------------------------------------------
    public UserResponseDTO updateUser(Long id, UserRequestDTO dto) {

        // Hämta user från DB, kasta exception om den inte finns
        User existing = userRepository.findById(id)
                .orElseThrow(() ->
                        new UserNotFoundException("Ingen user i databasen med id: " + id));

        // Uppdatera fält baserat på DTO
        userMapper.updateUserFromDto(existing, dto);

        //Hasha nytt lösenord
        existing.setPassword(passwordEncoder.encode(dto.password()));

        // Spara ändringarna (UPDATE sker automatiskt i DB)
        User updated = userRepository.save(existing);

        // Returnera DTO
        return userMapper.toDto(updated);
    }

    // -----------------------------------------------------------
    // DELETE
    // -----------------------------------------------------------
    public void deleteUser(Long id) {

        // Kontrollera om user finns, kasta exception annars
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException("Ingen user i databasen med id: " + id);
        }

        // Radera user från DB
        userRepository.deleteById(id);
    }

    // -----------------------------------------------------------
    // GET USER WITH POSTS
    // -----------------------------------------------------------
    public UserWithPostsResponseDTO getUserWithPosts(Long id) {

        // Hämta user med posts (custom query i repository)
        User user = userRepository.findUserWithPosts(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));

        return userWithPostsMapper.toDto(user);
    }
}
