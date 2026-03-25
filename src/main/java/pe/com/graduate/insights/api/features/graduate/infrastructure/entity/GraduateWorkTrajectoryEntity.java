package pe.com.graduate.insights.api.features.graduate.infrastructure.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;
import pe.com.graduate.insights.api.shared.infrastructure.repository.entities.Auditable;

@Getter
@Setter
@Entity
@Table(name = "graduado_trayectorias_laborales")
public class GraduateWorkTrajectoryEntity extends Auditable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "graduado_id", nullable = false)
  private GraduateEntity graduate;

  @Column(name = "empresa")
  private String empresa;

  @Column(name = "cargo")
  private String cargo;

  @Column(name = "modalidad")
  private String modalidad;

  @Column(name = "fecha_inicio")
  private LocalDate fechaInicio;

  @Column(name = "fecha_fin")
  private LocalDate fechaFin;
}
