package com.example.piproject.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.piproject.DTO.RegisterDTO;
import com.example.piproject.DTO.UserProfileDTO;
import com.example.piproject.DTO.UserProfileUpdateDTO;
import com.example.piproject.entity.Role;
import com.example.piproject.entity.User;
import com.example.piproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final Cloudinary cloudinary;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public void registerUser(RegisterDTO registerDTO) {
        // Check if username or email already exists
        if (userRepository.findByUsername(registerDTO.getUsername()).isPresent() ||
                userRepository.findByEmail(registerDTO.getEmail()).isPresent()) {
            throw new RuntimeException("Username / Email already exists!");
        }

        // Create a new user entity and set all values from RegisterDTO
        User newUser = new User();
        newUser.setUsername(registerDTO.getUsername());
        newUser.setEmail(registerDTO.getEmail());
        newUser.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
        newUser.setRole(Role.USER);  // Default role

        // Set the new fields
        newUser.setFirst_name(registerDTO.getFirstName());
        newUser.setLast_name(registerDTO.getLastName());
        newUser.setBirthdate(registerDTO.getBirthdate());

        // Save the new user to the database
        userRepository.save(newUser);
    }


    public com.example.piproject.entity.User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
    }

    public com.example.piproject.entity.User updateProfile(String username, UserProfileUpdateDTO profileUpdateDTO, MultipartFile file) throws IOException {
        com.example.piproject.entity.User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));


        if (file != null && !file.isEmpty()) {
            Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
            String imageUrl = (String) uploadResult.get("secure_url");
            ((com.example.piproject.entity.User) user).setProfilePictureUrl(imageUrl);
        }

        // Update other fields
        user.setFirst_name(profileUpdateDTO.getFirstName());
        user.setLast_name(profileUpdateDTO.getLastName());
        if (profileUpdateDTO.getBirthdate() != null) {
            user.setBirthdate(LocalDate.parse(profileUpdateDTO.getBirthdate()));
        }

        return userRepository.save(user);
    }

    public UserProfileDTO getUserProfile(String username) {
        com.example.piproject.entity.User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        UserProfileDTO dto = new UserProfileDTO();
        dto.setFirstName(user.getFirst_name());
        dto.setLastName(user.getLast_name());
        dto.setBirthdate(user.getBirthdate().toString());
        dto.setEmail(user.getEmail());
        dto.setProfilePictureUrl(user.getProfilePictureUrl());

        return dto;
    }

    public void deleteUserByUsername(String username) {
        com.example.piproject.entity.User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        userRepository.delete(user);
    }



}
