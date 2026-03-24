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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
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

  @Column(name = "fecha_inicio")
  private LocalDate fechaInicio;

  @Column(name = "fecha_fin")
  private LocalDate fechaFin;

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

  @Column(name = "facultad")
  private String facultad;

  @Column(name = "escuela_profesional")
  private String escuelaProfesional;

  @Column(name = "escuela_profesional_id")
  private Long escuelaProfesionalId;

  @Column(name = "anio_ingreso")
  private String anioIngreso;

  @Column(name = "anio_egreso")
  private String anioEgreso;

  @Column(name = "grado_obtenido")
  private String gradoObtenido;

  @Column(name = "bachiller_fecha")
  private LocalDate bachillerFecha;

  @Column(name = "bachiller_universidad")
  private String bachillerUniversidad;

  @Column(name = "titulo_profesional_fecha")
  private LocalDate tituloProfesionalFecha;

  @Column(name = "titulo_profesional_universidad")
  private String tituloProfesionalUniversidad;

  @Column(name = "maestria_fecha")
  private LocalDate maestriaFecha;

  @Column(name = "maestria_universidad")
  private String maestriaUniversidad;

  @Column(name = "doctorado_fecha")
  private LocalDate doctoradoFecha;

  @Column(name = "doctorado_universidad")
  private String doctoradoUniversidad;

  @Column(name = "otro_grado_nombre")
  private String otroGradoNombre;

  @Column(name = "otro_grado_fecha")
  private LocalDate otroGradoFecha;

  @Column(name = "otro_grado_universidad")
  private String otroGradoUniversidad;

  @Column(name = "modalidad_titulacion")
  private String modalidadTitulacion;

  @Column(name = "modalidad_titulacion_otro")
  private String modalidadTitulacionOtro;

  @Column(name = "idioma_nombre")
  private String idiomaNombre;

  @Column(name = "idioma_nivel")
  private String idiomaNivel;

  @Column(name = "idioma_fecha_inicio")
  private LocalDate idiomaFechaInicio;

  @Column(name = "idioma_fecha_fin")
  private LocalDate idiomaFechaFin;

  @Column(name = "idioma_aprendizaje")
  private String idiomaAprendizaje;

  @Column(name = "cv")
  private String cv;

  @Column(name = "ruta_cv")
  private String cvPath;

  @Column(name = "validado")
  private Boolean validated = Boolean.TRUE;

  @OneToMany(mappedBy = "graduate", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<GraduateDegreeEntity> grados = new ArrayList<>();

  @OneToMany(mappedBy = "graduate", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<GraduateLanguageEntity> idiomas = new ArrayList<>();

  @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn(name = "usuario_id", referencedColumnName = "id", nullable = false, unique = true)
  private UserEntity user;
}



