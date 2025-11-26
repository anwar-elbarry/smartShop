package org.example.smartshop.controller.auth;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.example.smartshop.dto.auth.LoginRequest;
import org.example.smartshop.dto.auth.LoginResponse;
import org.example.smartshop.dto.auth.RegisterRequest;
import org.example.smartshop.entity.User;
import org.example.smartshop.service.Auth.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Tag(name = "Auth",description = "Auth management APIs")
@RestController
@AllArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;


    @Operation(summary = "user login")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "the user logged successfully"),
            @ApiResponse(responseCode = "400", description = "Validation error")
    })
    @PostMapping("/login")
    public ResponseEntity<?> login(@Validated @RequestBody LoginRequest request , HttpSession session){
        try {
        User user = authService.login(request);
        session.setAttribute("UserId",user.getId());
        session.setAttribute("role",user.getRole().name());

        LoginResponse loginResponse = LoginResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .role(user.getRole())
                .build();

            return ResponseEntity.ok(loginResponse);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of(
                            "error", "Authentication failed",
                            "message" , e.getMessage()
                    ));
        }
    }
    @Operation(summary = "Admin register")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "the user registered successfully"),
            @ApiResponse(responseCode = "400", description = "Validation error")
    })
    @PostMapping("/register")
    public ResponseEntity<?> register(@Validated @RequestBody RegisterRequest request ){
        try {
        User user = authService.register(request);

            return ResponseEntity.status(HttpStatus.CREATED).body(
                    Map.of(
                            "message","registered successfully",
                            "username",user.getUsername(),
                            "role" , user.getRole()
                    )
            );
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of(
                            "error", "register failed",
                            "message" , e.getMessage()
                    ));
        }
    }
    @Operation(summary = "User logout")
    @ApiResponse(responseCode = "200", description = "Logged out successfully")
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpSession session){
        session.invalidate();
        return ResponseEntity.ok(Map.of(
                "message", "Logged out successfully"
        ));
    }
}
