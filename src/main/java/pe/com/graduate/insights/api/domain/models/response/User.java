package pe.com.graduate.insights.api.domain.models.response;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
@Getter
public class User {

    private Long id;
    private String nombres ;
    private LocalDate fechaDeNacimiento;
    private String genero;
    private String correoElectronico;
    private String dni;
    private String celular;
    private String contraseña;
    private LocalDateTime createAt;
    private LocalDateTime updatedAt;

}

