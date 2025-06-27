package pe.com.graduate.insights.api.infrastructure.repository.entities;

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
@Table(name = "graduate_question_responses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GraduateQuestionResponseEntity extends Auditable {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "question_id")
  private QuestionEntity question;

  @ManyToOne
  @JoinColumn(name = "graduate_survey_response_id")
  private GraduateSurveyResponseEntity graduateSurveyResponse;

  @ManyToMany
  @JoinTable(
      name = "graduate_question_response_options",
      joinColumns = @JoinColumn(name = "response_id"),
      inverseJoinColumns = @JoinColumn(name = "option_id"))
  private List<QuestionOptionEntity> selectedOptions;

  private String textResponse;
  private Integer numericResponse;
}
