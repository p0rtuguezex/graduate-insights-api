package pe.com.graduate.insights.api.features.graduate.infrastructure.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.HashSet;
import java.util.Set;
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

  @Column(name = "codigo_universitario")
  private String codigoUniversitario;

  @Column(name = "estado_civil")
  private String estadoCivil;

  @Column(name = "nacionalidad")
  private String nacionalidad;

  @Column(name = "correo_institucional")
  private String correoInstitucional;

  @Column(name = "direccion_actual")
  private String direccionActual;

  @Column(name = "ciudad")
  private String ciudad;

  @Column(name = "departamento")
  private String departamento;

  @Column(name = "pais_residencia")
  private String paisResidencia;

  @Column(name = "linkedin")
  private String linkedin;

  @Column(name = "portafolio")
  private String portafolio;

  @Column(name = "escuela_profesional_id")
  private Long escuelaProfesionalId;

  @Column(name = "anio_ingreso")
  private String anioIngreso;

  @Column(name = "anio_egreso")
  private String anioEgreso;

  @Column(name = "ruta_cv")
  private String cvPath;

  @Column(name = "validado")
  private Boolean validated = Boolean.TRUE;

  @OneToMany(mappedBy = "graduate", cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<GraduateDegreeEntity> grados = new HashSet<>();

  @OneToMany(mappedBy = "graduate", cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<GraduateLanguageEntity> idiomas = new HashSet<>();

  @OneToMany(mappedBy = "graduate", cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<GraduateComplementaryTrainingEntity> formacionesComplementarias =
      new HashSet<>();

  @OneToMany(mappedBy = "graduate", cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<GraduateWorkTrajectoryEntity> trayectoriasLaborales = new HashSet<>();

  @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn(name = "usuario_id", referencedColumnName = "id", nullable = false, unique = true)
  private UserEntity user;
}
