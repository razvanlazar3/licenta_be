package com.example.demo.auth;

import com.example.demo.config.JwtService;
import com.example.demo.users.data.entities.UserEntity;
import com.example.demo.users.data.repos.UserRepo;
import com.example.demo.users.exceptions.DuplicateUserException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepo userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest registerRequest) {
        if (!userRepository.existsByEmail(registerRequest.getEmail())) {
            var user = UserEntity.builder()
                    .name(registerRequest.getName())
                    .email(registerRequest.getEmail())
                    .password(passwordEncoder.encode(registerRequest.getPassword()))
                    .build();

            userRepository.save(user);

            return getTokenWithExtraClaims(user);
        } else {
            throw new DuplicateUserException("An user with the same email already exists.");
        }
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) throws Exception {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        var user = userRepository.findFirstByEmail(request.getEmail())
                .orElseThrow(() -> new Exception("User not found in db"));

        return getTokenWithExtraClaims(user);
    }

    private AuthenticationResponse getTokenWithExtraClaims(UserEntity user) {
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("name", user.getName());
        extraClaims.put("id", user.getId());

        var jwtToken = jwtService.generateToken(extraClaims, user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
}