package pe.com.graduate.insights.api.infrastructure.repository.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

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
    
    private String currentPosition;
    private String currentCompany;
    private BigDecimal currentSalaryRange;
    private String location;
    
    @OneToMany(mappedBy = "graduateSurveyResponse", cascade = CascadeType.ALL)
    private List<GraduateQuestionResponseEntity> responses;
    
    private LocalDateTime submittedAt;
    private boolean completed;
} 