package pe.com.graduate.insights.api.infrastructure.adapter.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Getter
@Setter
@Entity
@Table(name = "User")
public class UserEntity {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="id")
    @Id
    private Long id;

    @NotNull
    @Size(min = 1,max = 100)
    @Column(name = "nombres")
    private String nombres ;

    @Column(name = "fecha_de_nacimiento")
    private LocalDate fechaDeNacimiento;

    @Column(name = "genero")
    private String genero;

    @Email
    @Column(name = "correo_electronico",unique = true)
    private String correoElectronico;

    @Size(min = 8,max = 8)
    @Column(name = "dni")
    private String dni;

    @Column(name = "celular")
    private String celular;

    @Column(name = "contraseña")
    private String contraseña;


    @Column(name = "created_at")
    private LocalDateTime createAt;

    @Column(name = "udpated_at")
    private LocalDateTime updatedAt;

}
