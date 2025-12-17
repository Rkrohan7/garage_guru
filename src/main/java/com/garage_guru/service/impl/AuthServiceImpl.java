package com.garage_guru.service.impl;

import com.garage_guru.dto.request.LoginRequest;
import com.garage_guru.dto.request.SignupRequest;
import com.garage_guru.dto.response.JwtResponse;
import com.garage_guru.dto.response.SignupResponse;
import com.garage_guru.entity.Garage;
import com.garage_guru.entity.User;
import com.garage_guru.exception.DuplicateResourceException;
import com.garage_guru.exception.InvalidCredentialsException;
import com.garage_guru.exception.ResourceNotFoundException;
import com.garage_guru.repository.GarageRepository;
import com.garage_guru.repository.UserRepository;
import com.garage_guru.security.jwt.JwtUtils;
import com.garage_guru.security.service.UserDetailsImpl;
import com.garage_guru.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final GarageRepository garageRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    @Override
    @Transactional
    public SignupResponse registerUser(SignupRequest signupRequest) {
        // Java 21: Using record accessor methods (name() instead of getName())
        if (userRepository.existsByEmail(signupRequest.email())) {
            throw new DuplicateResourceException("Email is already registered: " + signupRequest.email());
        }

        Set<String> roles = new HashSet<>();
        roles.add("ROLE_ADMIN");

        Garage garage = null;

        // Option 1: Link to existing garage by garageId
        if (signupRequest.garageId() != null) {
            garage = garageRepository.findById(signupRequest.garageId())
                    .orElseThrow(() -> new ResourceNotFoundException("Garage not found with id: " + signupRequest.garageId()));
        }
        // Option 2: Create new garage if garage details are provided
        else if (signupRequest.garageName() != null && !signupRequest.garageName().isBlank()) {
            // Check if garage email already exists
            if (signupRequest.garageEmail() != null && garageRepository.existsByEmail(signupRequest.garageEmail())) {
                throw new DuplicateResourceException("Garage with email already exists: " + signupRequest.garageEmail());
            }

            garage = Garage.builder()
                    .name(signupRequest.garageName())
                    .logoUrl(signupRequest.garageLogoUrl())
                    .address(signupRequest.garageAddress())
                    .phoneNumber(signupRequest.garagePhoneNumber())
                    .email(signupRequest.garageEmail())
                    .googleMapLink(signupRequest.garageGoogleMapLink())
                    .build();

            garage = garageRepository.save(garage);
        }

        User.UserBuilder userBuilder = User.builder()
                .name(signupRequest.name())
                .email(signupRequest.email())
                .password(passwordEncoder.encode(signupRequest.password()))
                .roles(roles);

        if (garage != null) {
            userBuilder.garage(garage);
        }

        User savedUser = userRepository.save(userBuilder.build());

        // Return response with user and garage details
        if (garage != null) {
            return SignupResponse.success(
                    "User registered successfully!",
                    savedUser.getId(),
                    savedUser.getName(),
                    savedUser.getEmail(),
                    garage.getId(),
                    garage.getName()
            );
        } else {
            return SignupResponse.success(
                    "User registered successfully!",
                    savedUser.getId(),
                    savedUser.getName(),
                    savedUser.getEmail()
            );
        }
    }

    @Override
    public JwtResponse authenticateUser(LoginRequest loginRequest) {
        Authentication authentication;
        try {
            // Java 21: Using record accessor methods
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.email(),
                            loginRequest.password()
                    )
            );
        } catch (BadCredentialsException e) {
            throw new InvalidCredentialsException("Invalid email or password");
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Set<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());

        // Java 21: Using record constructor
        return new JwtResponse(
                jwt,
                userDetails.getId(),
                userDetails.getName(),
                userDetails.getEmail(),
                roles,
                userDetails.getGarageId()
        );
    }
}
