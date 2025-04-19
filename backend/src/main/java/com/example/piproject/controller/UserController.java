package com.example.piproject.controller;

import com.example.piproject.DTO.UserProfileDTO;
import com.example.piproject.DTO.UserProfileUpdateDTO;
import com.example.piproject.entity.User;
import com.example.piproject.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor

public class UserController {
    private final UserService userService;

    @PutMapping(value = "/update-profile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<User> updateProfile(
            @RequestPart("profileData") String profileData,
            @RequestPart(value = "file", required = false) MultipartFile file
    ) {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String username = ((User) auth.getPrincipal()).getUsername();

        try {
            // Deserialize JSON string into DTO
            ObjectMapper objectMapper = new ObjectMapper();
            UserProfileUpdateDTO profileUpdateDTO = objectMapper.readValue(profileData, UserProfileUpdateDTO.class);

            User updatedUser = userService.updateProfile(username,profileUpdateDTO, file);
            return ResponseEntity.ok(updatedUser);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/profile/@{username}")
    public ResponseEntity<UserProfileDTO> getProfileByUsername(@PathVariable String username) {
        UserProfileDTO profile = userService.getUserProfile(username);
        if (profile == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(profile);
    }

    @DeleteMapping("/profile/delete/@{username}")
    public ResponseEntity<Void> deleteProfile(@PathVariable String username) {
        try {
            userService.deleteUserByUsername(username);
            return ResponseEntity.noContent().build(); // 204 No Content
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }



}
