package pe.com.graduate.insights.api.infrastructure.repository.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

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
        inverseJoinColumns = @JoinColumn(name = "option_id")
    )
    private List<QuestionOptionEntity> selectedOptions;
    
    private String textResponse;
    private Integer numericResponse;
} 