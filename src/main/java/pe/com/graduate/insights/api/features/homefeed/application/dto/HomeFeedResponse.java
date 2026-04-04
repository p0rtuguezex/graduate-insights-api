package pe.com.graduate.insights.api.features.homefeed.application.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HomeFeedResponse {
  private Long id;
  private String tipo;
  private String titulo;
  private String descripcion;
  private String contenido;
  private LocalDateTime fechaCreacion;
  private String fuente;
  private String estado;
  private String tipoEvento;
  private String empresa;
  private String salario;
  private String ubicacion;
  private String link;
  private String enlaceInscripcion;
}