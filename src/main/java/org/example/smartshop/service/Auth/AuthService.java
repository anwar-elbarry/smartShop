package org.example.smartshop.service.Auth;

import lombok.RequiredArgsConstructor;
import org.example.smartshop.dto.auth.LoginRequest;
import org.example.smartshop.dto.auth.RegisterRequest;
import org.example.smartshop.entity.User;
import org.example.smartshop.repository.UserRepository;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    public User login(LoginRequest request){
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(()-> new RuntimeException("user not found") );

        if (!BCrypt.checkpw(request.getPassword(),user.getPassword())){
            throw new RuntimeException("Mot de passe incorrect");
        }
        return user;
    }

    public User register(RegisterRequest request){
        String passwordHash = BCrypt.hashpw(request.getPassword(),BCrypt.gensalt());
        User user = User.builder()
                .username(request.getUsername())
                .role(request.getRole())
                .password(passwordHash)
                .build();
        userRepository.save(user);
        return user;

    }
}
