package com.example.demo.auth;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping(value = "/register", consumes = "application/json")
    public ResponseEntity<AuthenticationResponse> register(@Valid @RequestBody RegisterRequest registerRequest) {
        return ResponseEntity.ok(authenticationService.register(registerRequest));
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> login(@Valid @RequestBody AuthenticationRequest registerRequest) throws Exception {
        return ResponseEntity.ok(authenticationService.authenticate(registerRequest));
    }
}