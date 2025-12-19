package pe.com.graduate.insights.api.infrastructure.repository.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "ofertas_laborales")
public class JobOffersEntity extends Auditable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String titulo;
  private String link;

  @Column(columnDefinition = "TEXT")
  private String descripcion;

  private String estado;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "empleador_id")
  private EmployerEntity employer;
}
