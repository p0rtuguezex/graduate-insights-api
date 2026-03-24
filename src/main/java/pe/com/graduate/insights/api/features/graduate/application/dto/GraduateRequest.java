package pe.com.graduate.insights.api.features.graduate.application.dto;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.springframework.format.annotation.DateTimeFormat;
import pe.com.graduate.insights.api.features.user.application.dto.UserRequest;

@SuperBuilder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class GraduateRequest extends UserRequest implements Serializable {

  @Serial private static final long serialVersionUID = 1L;

  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private LocalDate fechaInicio;

  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private LocalDate fechaFin;

  private String codigoUniversitario;

  private String estadoCivil;

  private String nacionalidad;

  private String correoInstitucional;

  private String direccionActual;

  private String ciudad;

  private String departamento;

  private String paisResidencia;

  private String linkedin;

  private String portafolio;

  private String facultad;

  private String escuelaProfesional;

  private Long escuelaProfesionalId;

  private String anioIngreso;

  private String anioEgreso;

  private String gradoObtenido;

  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private LocalDate bachillerFecha;

  private String bachillerUniversidad;

  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private LocalDate tituloProfesionalFecha;

  private String tituloProfesionalUniversidad;

  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private LocalDate maestriaFecha;

  private String maestriaUniversidad;

  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private LocalDate doctoradoFecha;

  private String doctoradoUniversidad;

  private String otroGradoNombre;

  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private LocalDate otroGradoFecha;

  private String otroGradoUniversidad;

  private String modalidadTitulacion;

  private String modalidadTitulacionOtro;

  private String idiomaNombre;

  private String idiomaNivel;

  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private LocalDate idiomaFechaInicio;

  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private LocalDate idiomaFechaFin;

  private String idiomaAprendizaje;

  private List<GraduateAcademicDegreeRequest> grados;

  private List<GraduateLanguageRequest> idiomas;

  private String cvPath;

  private Boolean validated;
}
