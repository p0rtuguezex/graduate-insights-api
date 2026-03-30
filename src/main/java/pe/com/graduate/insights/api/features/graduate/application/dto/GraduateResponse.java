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
  private String provincia;
  private String distrito;
  private String paisResidencia;
  private Boolean viveEnPeru;
  private String linkedin;
  private String portafolio;
  private Long escuelaProfesionalId;
  private String anioIngreso;
  private String anioEgreso;
  private List<GraduateAcademicDegreeResponse> grados;
  private List<GraduateLanguageResponse> idiomas;
  private List<GraduateComplementaryTrainingResponse> formacionesComplementarias;
  private List<GraduateWorkTrajectoryResponse> trayectoriasLaborales;
  private String cvPath;
  private String fotoPath;
  private Boolean validated;
}
