package org.example.smartshop.dto.client;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.example.smartshop.enums.UserRole;

@Data
public class ClientRequest {
    @NotBlank(message = "Le nom est requis")
    private String name;

    @NotBlank(message = "L'email est requis")
    @Email(message = "Format d'email invalide")
    private String email;

    @NotBlank(message = "Le mot de passe est requis")
    private String password;

    @NotBlank(message = "Le nom d'utilisateur est requis")
    private String username;
}
