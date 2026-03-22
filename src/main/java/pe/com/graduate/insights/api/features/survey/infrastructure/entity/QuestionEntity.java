package pe.com.graduate.insights.api.features.survey.infrastructure.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pe.com.graduate.insights.api.shared.infrastructure.repository.entities.Auditable;
import pe.com.graduate.insights.api.features.survey.infrastructure.entity.QuestionType;

@Entity
@Table(name = "preguntas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuestionEntity extends Auditable {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "texto_pregunta")
  private String questionText;

  @Enumerated(EnumType.STRING)
  @Column(name = "tipo_pregunta")
  private QuestionType questionType;

  @Column(name = "requerida")
  private boolean required;

  @ManyToOne
  @JoinColumn(name = "encuesta_id")
  private SurveyEntity survey;

  @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<QuestionOptionEntity> options;
}

