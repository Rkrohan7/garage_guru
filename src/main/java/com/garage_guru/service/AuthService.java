package com.garage_guru.service;

import com.garage_guru.dto.request.LoginRequest;
import com.garage_guru.dto.request.SignupRequest;
import com.garage_guru.dto.response.JwtResponse;
import com.garage_guru.dto.response.SignupResponse;

public interface AuthService {
    SignupResponse registerUser(SignupRequest signupRequest);
    JwtResponse authenticateUser(LoginRequest loginRequest);
}
