package pe.com.graduate.insights.api.domain.models.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pe.com.graduate.insights.api.infrastructure.repository.entities.SurveyType;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SurveyResponse {
    private Long id;
    private String title;
    private String description;
    private SurveyType surveyType;
    private List<QuestionResponse> questions;
} 