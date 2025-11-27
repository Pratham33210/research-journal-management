package com.research.journal.service;

import com.research.journal.dto.UserDTO;
import com.research.journal.entity.User;
import com.research.journal.entity.UserRole;
import com.research.journal.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserDTO registerUser(String email, String password, String firstName, String lastName, String affiliation, UserRole role, String username){
        // Check if user already exists
        if (userRepository.findByEmail(email).isPresent()) {
            throw new RuntimeException("User with this email already exists");
        }

        // Create new user
     User user = User.builder()
        .username(username)                    // ← CORRECT: .username()
        .email(email)
        .password(passwordEncoder.encode(password))
        .firstName(firstName)
        .lastName(lastName)
        .affiliation(affiliation)
        .role(role)
        .isActive(true)
        .bio("")
        .build();

        User savedUser = userRepository.save(user);
        return convertToDTO(savedUser);
    }

    public UserDTO loginUser(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        if (!user.getIsActive()) {
            throw new RuntimeException("User account is inactive");
        }

        return convertToDTO(user);
    }

    public UserDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return convertToDTO(user);
    }

    public UserDTO getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return convertToDTO(user);
    }

    public List<UserDTO> getUsersByRole(UserRole role) {
        return userRepository.findByRole(role).stream()
                .map(this::convertToDTO)
                .toList();
    }

    public List<UserDTO> getAllActiveUsers() {
        return userRepository.findByIsActiveTrue().stream()
                .map(this::convertToDTO)
                .toList();
    }

    public UserDTO updateUser(Long id, UserDTO userDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setAffiliation(userDTO.getAffiliation());
        user.setBio(userDTO.getBio());

        User updatedUser = userRepository.save(user);
        return convertToDTO(updatedUser);
    }

    public void deactivateUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setIsActive(false);
        userRepository.save(user);
    }

    private UserDTO convertToDTO(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .role(user.getRole())
                .affiliation(user.getAffiliation())
                .bio(user.getBio())
                .isActive(user.getIsActive())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
}
