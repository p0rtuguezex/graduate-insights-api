package pe.com.graduate.insights.api.features.surveytype.infrastructure.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pe.com.graduate.insights.api.features.survey.infrastructure.entity.SurveyEntity;
import pe.com.graduate.insights.api.shared.infrastructure.repository.entities.Auditable;

@Entity
@Table(name = "tipos_encuesta")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SurveyTypeEntity extends Auditable {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "nombre", nullable = false, unique = true, length = 100)
  private String name;

  @Column(name = "descripcion", length = 500)
  private String description;

  @Column(name = "activo", nullable = false)
  private Boolean active = true;

  @OneToMany(mappedBy = "surveyType", cascade = CascadeType.ALL)
  private List<SurveyEntity> surveys;
}
