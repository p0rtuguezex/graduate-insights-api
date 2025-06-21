package pe.com.graduate.insights.api.domain.models.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pe.com.graduate.insights.api.infrastructure.repository.entities.QuestionType;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuestionDetailResponse {
    private Long questionId;
    private String questionText;
    private QuestionType questionType;
    private boolean required;
    private List<QuestionOptionResponse> options;
    
    // Respuestas del graduado (si las hay)
    private List<Long> selectedOptionIds;
    private String textResponse;
    private Integer numericResponse;
    private boolean answered;
} 