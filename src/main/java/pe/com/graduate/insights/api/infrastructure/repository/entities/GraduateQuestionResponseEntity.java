package pe.com.graduate.insights.api.infrastructure.repository.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "respuestas_preguntas_graduados")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GraduateQuestionResponseEntity extends Auditable {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "pregunta_id")
  private QuestionEntity question;

  @ManyToOne
  @JoinColumn(name = "respuesta_encuesta_graduado_id")
  private GraduateSurveyResponseEntity graduateSurveyResponse;

  @ManyToMany
  @JoinTable(
      name = "opciones_respuesta_preguntas_graduados",
      joinColumns = @JoinColumn(name = "respuesta_id"),
      inverseJoinColumns = @JoinColumn(name = "opcion_id"))
  private List<QuestionOptionEntity> selectedOptions;

  @Column(name = "respuesta_texto")
  private String textResponse;

  @Column(name = "respuesta_numerica")
  private Integer numericResponse;
}
