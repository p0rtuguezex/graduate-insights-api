package pe.com.graduate.insights.api.infrastructure.repository.entities;

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
@Table(name = "event")
public class EventEntity extends Auditable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String nombre;
  private String contenido;
  private String estado;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "director_id", nullable = false)
  private DirectorEntity director;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "event_type_id", nullable = false)
  private EventTypesEntity eventType;
}
