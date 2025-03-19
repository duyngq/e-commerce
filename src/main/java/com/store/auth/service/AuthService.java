package com.store.auth.service;

import com.store.auth.config.JwtUtil;
import com.store.auth.entity.Role;
import com.store.auth.entity.User;
import com.store.auth.model.request.AuthRequest;
import com.store.auth.model.request.UserRequest;
import com.store.auth.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

public interface AuthService {
    String register(UserRequest request);

    String login(AuthRequest request);

    User findByUsername(String username);
}
