package pe.com.graduate.insights.api.infrastructure.repository.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "graduate_survey_responses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GraduateSurveyResponseEntity extends Auditable {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "survey_id")
  private SurveyEntity survey;

  @ManyToOne
  @JoinColumn(name = "graduate_id")
  private GraduateEntity graduate;

  @OneToMany(mappedBy = "graduateSurveyResponse", cascade = CascadeType.ALL)
  private List<GraduateQuestionResponseEntity> responses;

  private LocalDateTime submittedAt;
  private boolean completed;
}
