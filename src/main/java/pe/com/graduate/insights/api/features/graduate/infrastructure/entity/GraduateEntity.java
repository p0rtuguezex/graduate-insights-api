package pe.com.graduate.insights.api.features.graduate.infrastructure.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;
import pe.com.graduate.insights.api.shared.infrastructure.repository.entities.Auditable;
import pe.com.graduate.insights.api.features.user.infrastructure.entity.UserEntity;

@Getter
@Setter
@Entity
@Table(name = "graduados")
public class GraduateEntity extends Auditable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "fecha_inicio")
  private LocalDate fechaInicio;

  @Column(name = "fecha_fin")
  private LocalDate fechaFin;

  @Column(name = "cv")
  private String cv;

  @Column(name = "ruta_cv")
  private String cvPath;

  @Column(name = "validado")
  private Boolean validated = Boolean.TRUE;

  @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn(name = "usuario_id", referencedColumnName = "id", nullable = false, unique = true)
  private UserEntity user;
}



