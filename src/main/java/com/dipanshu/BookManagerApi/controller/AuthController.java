package com.dipanshu.BookManagerApi.controller;

import com.dipanshu.BookManagerApi.dto.AuthResponse;
import com.dipanshu.BookManagerApi.dto.LoginRequest;
import com.dipanshu.BookManagerApi.service.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @RequestBody LoginRequest request,
            @RequestHeader(value = "X-Device-Id", required = false) String deviceId,
            HttpServletResponse response) {

        AuthResponse auth = authService.login(request.getUsername(), request.getPassword(), deviceId);
        setRefreshTokenCookie(response, auth.getRefreshToken());
        return ResponseEntity.ok(new AuthResponse(auth.getAccessToken(), null));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(
            @CookieValue("refreshToken") String refreshToken,
            @RequestHeader(value = "X-Device-Id", required = false) String deviceId,
            HttpServletResponse response) {

        AuthResponse auth = authService.refresh(refreshToken, deviceId);
        setRefreshTokenCookie(response, auth.getRefreshToken());
        return ResponseEntity.ok(new AuthResponse(auth.getAccessToken(), null));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody LoginRequest request) {
        authService.register(request);
        return ResponseEntity.ok("Registered successfully");
    }

    private void setRefreshTokenCookie(HttpServletResponse response, String token) {
        ResponseCookie cookie = ResponseCookie.from("refreshToken", token)
                .httpOnly(true)
                .secure(true)
                .path("/auth/refresh")
                .maxAge(Duration.ofDays(7))
                .sameSite("None")
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }
}