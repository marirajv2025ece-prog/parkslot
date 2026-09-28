package com.parkspot.service;

import com.parkspot.dto.LoginRequest;
import com.parkspot.dto.LoginResponse;
import com.parkspot.entity.StaffUser;
import com.parkspot.exception.UnauthorizedException;
import com.parkspot.repository.StaffUserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class AuthService {

    private final StaffUserRepository staffUserRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(StaffUserRepository staffUserRepository, PasswordEncoder passwordEncoder) {
        this.staffUserRepository = staffUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public LoginResponse login(LoginRequest request) {
        StaffUser user = staffUserRepository.findByUsername(request.username().trim())
                .orElseThrow(() -> new UnauthorizedException("Invalid username or password."));

        if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
            // same message for wrong user and wrong password, so nobody can guess usernames
            throw new UnauthorizedException("Invalid username or password.");
        }
        return new LoginResponse(user.getUsername(), user.getFullName());
    }

    public LoginResponse getProfile(String username) {
        StaffUser user = staffUserRepository.findByUsername(username)
                .orElseThrow(() -> new UnauthorizedException("Please log in again."));
        return new LoginResponse(user.getUsername(), user.getFullName());
    }
}
