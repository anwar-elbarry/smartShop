package org.example.smartshop.dto.auth;

import lombok.Builder;
import lombok.Data;
import org.example.smartshop.enums.UserRole;

@Builder
@Data
public class LoginResponse {
    private String id;
    private String username;
    private UserRole role;
}
