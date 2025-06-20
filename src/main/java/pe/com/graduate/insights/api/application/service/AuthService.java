package pe.com.graduate.insights.api.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pe.com.graduate.insights.api.application.ports.input.AuthUseCase;
import pe.com.graduate.insights.api.domain.exception.InvalidCredentialsException;
import pe.com.graduate.insights.api.infrastructure.repository.entities.UserEntity;
import pe.com.graduate.insights.api.infrastructure.repository.jpa.UserRepository;

import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class AuthService implements AuthUseCase {
    private static final String EMAIL_PATTERN = "^[A-Za-z0-9+_.-]+@(.+)$";
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRoleService userRoleService;

    @Override
    public UserEntity login(String email, String password) {
        if (!isValidEmail(email)) {
            throw new InvalidCredentialsException("Invalid email format");
        }

        UserEntity user = userRepository.findByCorreo(email)
                .filter(u -> passwordEncoder.matches(password, u.getContrasena()))
                .orElseThrow(() -> new InvalidCredentialsException("Invalid email or password"));
        
        // Establecer el rol del usuario
        user.setUserRole(userRoleService.getUserRole(user.getId()));
        
        return user;
    }


    @Override
    public UserEntity getCurrentUser(Authentication authentication) {
        if (authentication == null) {
            throw new AuthenticationCredentialsNotFoundException("No se encontró autenticación");
        }
        UserEntity user = userRepository.findByCorreo(authentication.getName())
                .orElseThrow(() -> new InvalidCredentialsException("Usuario no encontrado"));
        
        // Establecer el rol del usuario
        user.setUserRole(userRoleService.getUserRole(user.getId()));
        
        return user;
    }

    private boolean isValidEmail(String email) {
        return Pattern.compile(EMAIL_PATTERN)
                .matcher(email)
                .matches();
    }
}
