package pe.com.graduate.insights.api.features.employer.infrastructure.entity;

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
import lombok.Getter;
import lombok.Setter;
import pe.com.graduate.insights.api.shared.infrastructure.repository.entities.Auditable;
import pe.com.graduate.insights.api.features.user.infrastructure.entity.UserEntity;

@Getter
@Setter
@Entity
@Table(name = "empleadores")
public class EmployerEntity extends Auditable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String ruc;

  @Column(name = "razon_social")
  private String razonSocial;

  @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn(name = "usuario_id", referencedColumnName = "id", nullable = false, unique = true)
  private UserEntity user;
}



