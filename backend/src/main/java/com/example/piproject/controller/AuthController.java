package com.example.piproject.controller;

import com.example.piproject.DTO.AuthResponseDTO;
import com.example.piproject.DTO.LoginDTO;
import com.example.piproject.DTO.RegisterDTO;
import com.example.piproject.entity.User;
import com.example.piproject.security.JwtUtil;
import com.example.piproject.services.PasswordResetTokenService;
import com.example.piproject.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final PasswordResetTokenService resetService;

    // ✅ Register a new user
    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(@RequestBody RegisterDTO registerDTO) {
        userService.registerUser(registerDTO);
        Map<String, String> response = new HashMap<>();
        response.put("message", "User registered successfully!");

        return ResponseEntity.ok(response);
    }


    // ✅ Login and get JWT Token
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody LoginDTO loginDTO) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword())
        );
        User dbUser = userService.findByUsername(loginDTO.getUsername());
        String email = dbUser.getEmail();
        String picture = dbUser.getProfilePictureUrl();

        UserDetails userDetails = userService.loadUserByUsername(loginDTO.getUsername());
        String role = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst()
                .orElse("USER");

        String token = jwtUtil.generateToken(userDetails.getUsername(), email, role, picture);
        AuthResponseDTO responseDTO = new AuthResponseDTO(token, dbUser.getUsername(), dbUser.getEmail(), role);

        return ResponseEntity.ok(responseDTO);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<Map<String, String>> forgotPassword(@RequestBody Map<String, String> request) {
        resetService.sendResetEmail(request.get("email"));

        Map<String, String> response = new HashMap<>();
        response.put("message", "Password reset email sent");

        return ResponseEntity.ok(response);
    }


    @GetMapping("/validate-reset-token")
    public ResponseEntity<?> validateToken(@RequestParam String token) {
        return resetService.validateResetToken(token) ?
                ResponseEntity.ok().build() : ResponseEntity.badRequest().build();
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> request) {
        resetService.resetPassword(request.get("token"), request.get("newPassword"));
        return ResponseEntity.ok("Password reset successful");
    }

}
