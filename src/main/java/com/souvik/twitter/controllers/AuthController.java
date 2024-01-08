package com.souvik.twitter.controllers;

import com.souvik.twitter.config.JwtProvider;
import com.souvik.twitter.exception.UserException;
import com.souvik.twitter.models.User;
import com.souvik.twitter.models.Verification;
import com.souvik.twitter.repositories.UserRepository;
import com.souvik.twitter.response.AuthResponse;
import com.souvik.twitter.services.CustomUserDetailsServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CustomUserDetailsServiceImpl customUserDetails;
    private JwtProvider jwtProvider;

    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder, CustomUserDetailsServiceImpl customUserDetails) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.customUserDetails = customUserDetails;
    }

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> createUserHandler(@RequestBody User user) throws UserException {
        String email = user.getEmail();
        String password = user.getPassword();
        String fullName = user.getFullName();
        String birthDate = user.getBirthDate();

        User isEmailExist = userRepository.findUserByEmail(email);
        if (isEmailExist != null) {
            throw new UserException("User with given email already exists with another account: " + email);
        }

        User newUser = new User();
        newUser.setEmail(email);
        newUser.setFullName(fullName);
        newUser.setPassword(password);
        newUser.setBirthDate(birthDate);
        newUser.setVerification(new Verification());

        User savedUser = userRepository.save(newUser);

        Authentication authentication = new UsernamePasswordAuthenticationToken(savedUser.getEmail(), savedUser.getPassword());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtProvider.generateToken(authentication);
        AuthResponse response = new AuthResponse(token, true);

        return new ResponseEntity(response, HttpStatus.CREATED);
    }
}