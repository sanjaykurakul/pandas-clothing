package com.sanjay.PandasClothing.controller;

import org.springframework.http.ResponseEntity;
import java.security.Principal;

import com.sanjay.PandasClothing.entity.User;
import com.sanjay.PandasClothing.repository.UserRepository;
import com.sanjay.PandasClothing.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@CrossOrigin
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;

    // ---------------- REGISTER USER ----------------

    @PostMapping("/register")
    public String register(@RequestBody User user) {

        if (userRepository.findByEmail(user.getEmail()) != null) {
            return "Email already exists";
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("USER");

        userRepository.save(user);
        return "User registered successfully";
    }

    // ---------------- CURRENT USER ----------------

    @GetMapping("/current-user")
    public ResponseEntity<String> currentUser(Principal principal) {

        if (principal == null) {
            return ResponseEntity.status(401).body("");
        }

        return ResponseEntity.ok(principal.getName());
    }

    // -------- GET PROFILE --------

    @GetMapping("/profile")
    public User getProfile(Principal principal) {
        return userService.findByEmail(principal.getName());
    }

    // -------- UPDATE PROFILE --------

    @PutMapping("/profile")
    public User updateProfile(@RequestBody User updated, Principal principal) {

        User user = userRepository.findByEmail(principal.getName()).orElseThrow();

        user.setName(updated.getName());
        user.setPhone(updated.getPhone());

        return userRepository.save(user);
    }
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteAccount(Principal principal) {

        User user = userService.findByEmail(principal.getName());

        userRepository.delete(user);

        return ResponseEntity.ok("Account deleted");
    }
}