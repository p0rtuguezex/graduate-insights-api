package pe.com.graduate.insights.api.features.survey.infrastructure.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pe.com.graduate.insights.api.features.surveytype.infrastructure.entity.SurveyTypeEntity;
import pe.com.graduate.insights.api.shared.infrastructure.repository.entities.Auditable;
import pe.com.graduate.insights.api.features.survey.infrastructure.entity.SurveyStatus;

@Entity
@Table(name = "encuestas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SurveyEntity extends Auditable {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "titulo")
  private String title;

  @Column(name = "descripcion")
  private String description;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "tipo_encuesta_id", nullable = false)
  private SurveyTypeEntity surveyType;

  @Enumerated(EnumType.STRING)
  @Column(name = "estado")
  private SurveyStatus status;

  @Column(name = "fecha_inicio")
  private LocalDate startDate;

  @Column(name = "fecha_fin")
  private LocalDate endDate;

  @OneToMany(mappedBy = "survey", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<QuestionEntity> questions;
}

