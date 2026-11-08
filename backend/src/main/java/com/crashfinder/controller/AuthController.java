package com.crashfinder.controller;

import com.crashfinder.dto.AuthRequest;
import com.crashfinder.dto.AuthResponse;
import com.crashfinder.model.LoginRecord;
import com.crashfinder.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody AuthRequest request, HttpServletRequest httpRequest) {
        try {
            AuthResponse response = authService.register(request, httpRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", "Registration failed: " + e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request, HttpServletRequest httpRequest) {
        try {
            AuthResponse response = authService.login(request, httpRequest);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.singletonMap("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", "Login failed: " + e.getMessage()));
        }
    }

    @GetMapping("/logins")
    public ResponseEntity<List<LoginRecord>> getAllLoginRecords() {
        return ResponseEntity.ok(authService.getAllLoginRecords());
    }

    @GetMapping("/logins/my")
    public ResponseEntity<List<LoginRecord>> getUserLoginRecords(@RequestParam String email) {
        return ResponseEntity.ok(authService.getUserLoginRecords(email));
    }
}

