package pe.com.graduate.insights.api.infrastructure.repository.entities;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.CascadeType;
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
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "surveys")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SurveyEntity extends Auditable {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String title;
  private String description;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "survey_type_id", nullable = false)
  private SurveyTypeEntity surveyType;

  @Enumerated(EnumType.STRING)
  private SurveyStatus status;

  private LocalDate startDate;

  private LocalDate endDate;

  @OneToMany(mappedBy = "survey", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<QuestionEntity> questions;
}
