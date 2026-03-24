package pe.com.graduate.insights.api.features.graduate.infrastructure.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import pe.com.graduate.insights.api.shared.infrastructure.repository.entities.Auditable;

@Getter
@Setter
@Entity
@Table(name = "graduado_titulaciones")
public class GraduateTitulationEntity extends Auditable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "graduado_grado_id", nullable = false)
  private GraduateDegreeEntity grado;

  @Column(name = "modalidad_titulacion_id")
  private Long modalidadTitulacionId;

  @Column(name = "modalidad_otro")
  private String modalidadOtro;
}
