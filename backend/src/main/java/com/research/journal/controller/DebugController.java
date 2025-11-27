package com.research.journal.controller;

import com.research.journal.dto.UserDTO;
import com.research.journal.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/debug")
@RequiredArgsConstructor
public class DebugController {
    private final UserService userService;

    @GetMapping("/users")
    public ResponseEntity<List<UserDTO>> listUsers() {
        return ResponseEntity.ok(userService.getAllActiveUsers());
    }
}
