package pe.com.graduate.insights.api.features.emailconfig.infrastructure.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import pe.com.graduate.insights.api.shared.infrastructure.repository.entities.Auditable;

@Getter
@Setter
@Entity
@Table(name = "configuracion_email")
public class EmailConfigEntity extends Auditable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "proveedor", nullable = false)
  private String proveedor;

  @Column(name = "api_key", nullable = false)
  private String apiKey;

  @Column(name = "email_remitente", nullable = false)
  private String emailRemitente;

  @Column(name = "nombre_remitente", nullable = false)
  private String nombreRemitente;

  @Column(name = "activo")
  private Boolean activo;
}
