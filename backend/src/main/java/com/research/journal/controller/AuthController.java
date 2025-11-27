package com.research.journal.controller;

import com.research.journal.dto.LoginRequest;
import com.research.journal.dto.LoginResponse;
import com.research.journal.dto.RegisterRequest;
import com.research.journal.dto.UserDTO;
import com.research.journal.entity.UserRole;
import com.research.journal.security.JwtTokenProvider;
import com.research.journal.service.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class AuthController {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;
    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

   @PostMapping("/register")
public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
    log.info("Register attempt for email={}", request.getEmail());
    try {
        UserRole role = UserRole.valueOf(request.getRole().toUpperCase());

        // Use username from frontend, if missing → create from email
        String username = request.getUsername();
        if (username == null || username.trim().isEmpty()) {
            username = request.getEmail().split("@")[0].toLowerCase().replaceAll("[^a-z0-9]", "");
        }

        UserDTO userDTO = userService.registerUser(
                request.getEmail(),
                request.getPassword(),
                request.getFirstName(),
                request.getLastName(),
                request.getAffiliation(),
                role,
                username      // ← NOW WE PASS USERNAME!
        );

        String token = jwtTokenProvider.generateToken(userDTO.getEmail(), userDTO.getId());
        LoginResponse response = LoginResponse.builder()
                .token(token)
                .user(userDTO)
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);

    } catch (IllegalArgumentException e) {
        return ResponseEntity.badRequest().body("Invalid role: " + request.getRole());
    } catch (RuntimeException e) {
        log.error("Registration failed: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }
}

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            UserDTO userDTO = userService.loginUser(request.getEmail(), request.getPassword());
            String token = jwtTokenProvider.generateToken(userDTO.getEmail(), userDTO.getId());

            LoginResponse response = LoginResponse.builder()
                    .token(token)
                    .user(userDTO)
                    .build();

            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(e.getMessage());
        }
    }

    @GetMapping("/validate")
    public ResponseEntity<?> validateToken(@RequestParam String token) {
        if (jwtTokenProvider.validateToken(token)) {
            String email = jwtTokenProvider.getEmailFromToken(token);
            Long userId = jwtTokenProvider.getUserIdFromToken(token);
            return ResponseEntity.ok()
                    .body("{'email': '" + email + "', 'userId': " + userId + "}");
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
    }
}
