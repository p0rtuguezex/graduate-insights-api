package pe.com.graduate.insights.api.features.graduate.infrastructure.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;
import pe.com.graduate.insights.api.shared.infrastructure.repository.entities.Auditable;

@Getter
@Setter
@Entity
@Table(name = "graduado_grados")
public class GraduateDegreeEntity extends Auditable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "graduado_id", nullable = false)
  private GraduateEntity graduate;

  @Column(name = "tipo_grado_id")
  private Long tipoGradoId;

  @Column(name = "universidad_id")
  private Long universidadId;

  @Column(name = "fecha_grado")
  private LocalDate fechaGrado;

  @Column(name = "otro_grado_nombre")
  private String otroGradoNombre;

  @OneToOne(mappedBy = "grado", cascade = CascadeType.ALL, orphanRemoval = true)
  private GraduateTitulationEntity titulation;
}
