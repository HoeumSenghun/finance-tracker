package com.financetracker.service;

import com.financetracker.dto.request.LoginRequest;
import com.financetracker.dto.request.RefreshTokenRequest;
import com.financetracker.dto.request.RegisterRequest;
import com.financetracker.dto.response.TokenResponse;

import java.util.Map;

public interface AuthService {

    Map<String, String> register(RegisterRequest request);

    TokenResponse login(LoginRequest request);

    TokenResponse refresh(RefreshTokenRequest request);

    Map<String, String> logout(RefreshTokenRequest request);
}
