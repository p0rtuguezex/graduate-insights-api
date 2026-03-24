package pe.com.graduate.insights.api.features.graduate.application.dto;

import java.util.List;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import pe.com.graduate.insights.api.features.user.application.dto.UserResponse;

@SuperBuilder
@Getter
@ToString
public class GraduateResponse extends UserResponse {
  private Long graduateId;
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
  private String bachillerFecha;
  private String bachillerUniversidad;
  private String tituloProfesionalFecha;
  private String tituloProfesionalUniversidad;
  private String maestriaFecha;
  private String maestriaUniversidad;
  private String doctoradoFecha;
  private String doctoradoUniversidad;
  private String otroGradoNombre;
  private String otroGradoFecha;
  private String otroGradoUniversidad;
  private String modalidadTitulacion;
  private String modalidadTitulacionOtro;
  private String idiomaNombre;
  private String idiomaNivel;
  private String idiomaFechaInicio;
  private String idiomaFechaFin;
  private String idiomaAprendizaje;
  private List<GraduateAcademicDegreeResponse> grados;
  private List<GraduateLanguageResponse> idiomas;
  private String fechaInicio;
  private String fechaFin;
  private String cv;
  private String cvPath;
  private Boolean validated;
}
