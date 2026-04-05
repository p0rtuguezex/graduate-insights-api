package pe.com.graduate.insights.api.features.graduatesurveys.infrastructure.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
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
import pe.com.graduate.insights.api.features.graduate.infrastructure.entity.GraduateEntity;
import pe.com.graduate.insights.api.features.survey.infrastructure.entity.SurveyEntity;
import pe.com.graduate.insights.api.shared.infrastructure.repository.entities.Auditable;

@Entity
@Table(name = "respuestas_encuestas_graduados")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GraduateSurveyResponseEntity extends Auditable {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "encuesta_id")
  private SurveyEntity survey;

  @ManyToOne
  @JoinColumn(name = "graduado_id")
  private GraduateEntity graduate;

  @OneToMany(mappedBy = "graduateSurveyResponse", cascade = CascadeType.ALL)
  private List<GraduateQuestionResponseEntity> responses;

  @Column(name = "enviada_en")
  private LocalDateTime submittedAt;

  @Column(name = "completada")
  private boolean completed;
}
