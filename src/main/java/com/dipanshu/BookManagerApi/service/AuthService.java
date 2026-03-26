package com.dipanshu.BookManagerApi.service;

import com.dipanshu.BookManagerApi.dto.AuthResponse;
import com.dipanshu.BookManagerApi.dto.LoginRequest;
import com.dipanshu.BookManagerApi.entity.RefreshToken;
import com.dipanshu.BookManagerApi.entity.User;
import com.dipanshu.BookManagerApi.exception.ResourceNotFoundException;
import com.dipanshu.BookManagerApi.repository.RefreshTokenRepository;
import com.dipanshu.BookManagerApi.repository.UserRepository;
import com.dipanshu.BookManagerApi.security.JwtUtil;
import com.dipanshu.BookManagerApi.security.TokenHashUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

    private final JwtUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;
    private final TokenHashUtil tokenHashUtil;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${jwt.refresh.expiration}")
    private long refreshExpiration;

    @Value("${security.single-session:false}")
    private boolean singleSession;

    public AuthResponse login(String username, String password, String deviceId) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new ResourceNotFoundException("Invalid password");
        }

        if (singleSession) {
            refreshTokenRepository.revokeAllByUsername(username);
        }

        String accessToken = jwtUtil.generateToken(username, "USER");
        String refreshToken = UUID.randomUUID().toString();

        RefreshToken token = RefreshToken.builder()
                .tokenHash(tokenHashUtil.hash(refreshToken))
                .username(username)
                .revoked(false)
                .used(false)
                .deviceId(deviceId)
                .expiryDate(LocalDateTime.now().plusSeconds(refreshExpiration))
                .build();

        refreshTokenRepository.save(token);

        return new AuthResponse(accessToken, refreshToken);
    }

    public AuthResponse refresh(String refreshToken, String deviceId) {

        String hash = tokenHashUtil.hash(refreshToken);

        RefreshToken stored = refreshTokenRepository.findByTokenHash(hash)
                .orElseThrow(() -> new RuntimeException("Invalid refresh token"));

        if (stored.isRevoked() || stored.isUsed()) {
            refreshTokenRepository.revokeAllByUsername(stored.getUsername());
            throw new RuntimeException("Refresh token reuse detected");
        }

        if (stored.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Refresh token expired");
        }

        stored.setUsed(true);
        stored.setRevoked(true);

        String username = stored.getUsername();

        String newAccessToken = jwtUtil.generateToken(username, "USER");
        String newRefreshToken = UUID.randomUUID().toString();

        RefreshToken newToken = RefreshToken.builder()
                .tokenHash(tokenHashUtil.hash(newRefreshToken))
                .username(username)
                .revoked(false)
                .used(false)
                .deviceId(deviceId)
                .parentId(stored.getId())
                .expiryDate(LocalDateTime.now().plusSeconds(refreshExpiration))
                .build();

        refreshTokenRepository.save(newToken);

        return new AuthResponse(newAccessToken, newRefreshToken);
    }

    public void register(LoginRequest request) {

        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new RuntimeException("Username already taken");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole("USER");

        userRepository.save(user);
    }
}