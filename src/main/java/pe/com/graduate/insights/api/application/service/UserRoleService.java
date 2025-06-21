package pe.com.graduate.insights.api.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pe.com.graduate.insights.api.domain.models.enums.UserRole;
import pe.com.graduate.insights.api.infrastructure.repository.jpa.DirectorRepository;
import pe.com.graduate.insights.api.infrastructure.repository.jpa.EmployerRepository;
import pe.com.graduate.insights.api.infrastructure.repository.jpa.GraduateRepository;

@Service
@RequiredArgsConstructor
public class UserRoleService {

    private final DirectorRepository directorRepository;
    private final EmployerRepository employerRepository;
    private final GraduateRepository graduateRepository;

    public UserRole getUserRole(Long userId) {
        // Verificar si es director
        if (directorRepository.existsByUserIdAndUserEstado(userId, "1")) {
            return UserRole.DIRECTOR;
        }
        
        // Verificar si es empleador
        if (employerRepository.existsByUserIdAndUserEstado(userId, "1")) {
            return UserRole.EMPLOYER;
        }
        
        // Verificar si es graduado
        if (graduateRepository.existsByUserIdAndUserEstado(userId, "1")) {
            return UserRole.GRADUATE;
        }
        
        // Por defecto, si no encuentra ningún rol específico, retorna graduado
        return UserRole.GRADUATE;
    }

    public String getUserRoleDisplayName(Long userId) {
        return getUserRole(userId).getDisplayName();
    }

    public String getUserRoleAuthority(Long userId) {
        return getUserRole(userId).getAuthority();
    }
} 