package pe.com.graduate.insights.api.features.survey.infrastructure.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pe.com.graduate.insights.api.shared.infrastructure.repository.entities.Auditable;

@Entity
@Table(name = "opciones_pregunta")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuestionOptionEntity extends Auditable {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "texto_opcion")
  private String optionText;

  @Column(name = "numero_orden")
  private Integer orderNumber;

  @ManyToOne
  @JoinColumn(name = "pregunta_id")
  private QuestionEntity question;
}
