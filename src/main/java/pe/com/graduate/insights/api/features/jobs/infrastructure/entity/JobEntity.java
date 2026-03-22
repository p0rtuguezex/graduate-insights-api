package pe.com.graduate.insights.api.features.jobs.infrastructure.entity;

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
import pe.com.graduate.insights.api.features.graduate.infrastructure.entity.GraduateEntity;

@Getter
@Setter
@Entity
@Table(name = "trabajos")
public class JobEntity extends Auditable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String compania;

  private String cargo;

  private String modalidad;

  private String estado;

  @Column(name = "fecha_inicio")
  private LocalDate fechaInicio;

  @Column(name = "fecha_fin")
  private LocalDate fechaFin;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "graduado_id")
  private GraduateEntity graduate;

  public String getValues() {
    return compania + " - " + cargo;
  }
}



