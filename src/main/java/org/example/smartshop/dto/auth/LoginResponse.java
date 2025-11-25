package org.example.smartshop.dto.auth;

import lombok.Builder;
import org.example.smartshop.enums.UserRole;

@Builder
public class LoginResponse {
    private String id;
    private String username;
    private UserRole role;
}
