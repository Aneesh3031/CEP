package com.crashfinder.service;

import com.crashfinder.dto.AuthRequest;
import com.crashfinder.dto.AuthResponse;
import com.crashfinder.model.LoginRecord;
import com.crashfinder.model.User;
import com.crashfinder.repository.LoginRecordRepository;
import com.crashfinder.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final LoginRecordRepository loginRecordRepository;

    public AuthService(UserRepository userRepository, LoginRecordRepository loginRecordRepository) {
        this.userRepository = userRepository;
        this.loginRecordRepository = loginRecordRepository;
    }

    public static String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedhash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : encodedhash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) {
            return Integer.toHexString(password.hashCode());
        }
    }

    private String getClientIp(HttpServletRequest request) {
        if (request == null) return "127.0.0.1";
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return (ip != null) ? ip : "127.0.0.1";
    }

    private String getUserAgent(HttpServletRequest request) {
        if (request == null) return "Browser";
        String ua = request.getHeader("User-Agent");
        return (ua != null) ? ua : "Unknown Client";
    }

    @Transactional
    public AuthResponse register(AuthRequest request, HttpServletRequest httpServletRequest) {
        String email = request.getEmail() != null ? request.getEmail().trim().toLowerCase() : "";
        if (email.isEmpty()) {
            throw new IllegalArgumentException("Email is required.");
        }
        if (request.getPassword() == null || request.getPassword().length() < 6) {
            throw new IllegalArgumentException("Password must be at least 6 characters.");
        }
        if (userRepository.existsByEmailIgnoreCase(email)) {
            throw new IllegalArgumentException("An account with this email already exists.");
        }

        String name = (request.getName() != null && !request.getName().trim().isEmpty())
                ? request.getName().trim()
                : email.split("@")[0];

        // If client passes raw password, hash it. If it's already a 64-char hex SHA-256 hash, keep it.
        String pwd = request.getPassword();
        String pwdHash = (pwd.length() == 64 && pwd.matches("^[0-9a-fA-F]+$"))
                ? pwd.toLowerCase()
                : hashPassword(pwd);

        User user = new User(email, name, pwdHash);
        userRepository.save(user);

        // Record initial registration login event
        LoginRecord record = new LoginRecord(
                email,
                true,
                getClientIp(httpServletRequest),
                getUserAgent(httpServletRequest),
                "Initial Registration & Sign-in"
        );
        loginRecordRepository.save(record);

        String token = UUID.randomUUID().toString();
        return new AuthResponse(token, user.getEmail(), user.getName(), user.getRole(), "Registration successful");
    }

    @Transactional
    public AuthResponse login(AuthRequest request, HttpServletRequest httpServletRequest) {
        String email = request.getEmail() != null ? request.getEmail().trim().toLowerCase() : "";
        String ip = getClientIp(httpServletRequest);
        String ua = getUserAgent(httpServletRequest);

        if (email.isEmpty() || request.getPassword() == null) {
            loginRecordRepository.save(new LoginRecord(email, false, ip, ua, "Missing credentials"));
            throw new IllegalArgumentException("Email and password are required.");
        }

        Optional<User> userOpt = userRepository.findByEmailIgnoreCase(email);
        if (userOpt.isEmpty()) {
            loginRecordRepository.save(new LoginRecord(email, false, ip, ua, "User not found"));
            throw new IllegalArgumentException("Email or password is incorrect.");
        }

        User user = userOpt.get();
        String pwd = request.getPassword();
        String inputHash = (pwd.length() == 64 && pwd.matches("^[0-9a-fA-F]+$"))
                ? pwd.toLowerCase()
                : hashPassword(pwd);

        if (!user.getPasswordHash().equalsIgnoreCase(inputHash)) {
            loginRecordRepository.save(new LoginRecord(email, false, ip, ua, "Invalid password"));
            throw new IllegalArgumentException("Email or password is incorrect.");
        }

        // Login success: save record to DB
        loginRecordRepository.save(new LoginRecord(email, true, ip, ua, null));

        String token = UUID.randomUUID().toString();
        return new AuthResponse(token, user.getEmail(), user.getName(), user.getRole(), "Login successful");
    }

    public List<LoginRecord> getAllLoginRecords() {
        return loginRecordRepository.findTop50ByOrderByLoginTimeDesc();
    }

    public List<LoginRecord> getUserLoginRecords(String email) {
        return loginRecordRepository.findByEmailIgnoreCaseOrderByLoginTimeDesc(email);
    }
}

