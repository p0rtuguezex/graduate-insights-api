package pe.com.graduate.insights.api.features.event.infrastructure.entity;

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
import pe.com.graduate.insights.api.features.eventtypes.infrastructure.entity.EventTypesEntity;
import pe.com.graduate.insights.api.shared.infrastructure.repository.entities.Auditable;
import pe.com.graduate.insights.api.features.director.infrastructure.entity.DirectorEntity;

@Getter
@Setter
@Entity
@Table(name = "eventos")
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
  @JoinColumn(name = "tipo_evento_id", nullable = false)
  private EventTypesEntity eventType;
}



