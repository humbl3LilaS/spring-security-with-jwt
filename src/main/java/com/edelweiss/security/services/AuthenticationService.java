package com.edelweiss.security.services;


import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.edelweiss.security.domain.Role;
import com.edelweiss.security.domain.User;
import com.edelweiss.security.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import pojo.auth.AuthenticationRequest;
import pojo.auth.AuthenticationResponse;
import pojo.auth.RegisterRequest;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepo;
    private final PasswordEncoder encoder;
    private final JwtServices jwtServices;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {
        var user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(encoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();
        userRepo.save(user);
        var jwt_Token = jwtServices.generateToken(user);
        var response = AuthenticationResponse.builder().token(jwt_Token).build();
        return response;
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        var user = userRepo.findByEmail(request.getEmail()).orElseThrow(RuntimeException::new);
        var jwt_Token = jwtServices.generateToken(user);
        var response = AuthenticationResponse.builder().token(jwt_Token).build();
        return response;
    }
}
