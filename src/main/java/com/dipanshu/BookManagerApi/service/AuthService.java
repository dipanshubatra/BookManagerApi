package com.dipanshu.BookManagerApi.service;

import com.dipanshu.BookManagerApi.dto.*;

public interface AuthService {

    AuthResponse registerUser(RegisterRequest request);

    AuthResponse login(LoginRequest request);

}