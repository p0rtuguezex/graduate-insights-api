package pe.com.graduate.insights.api.domain.models.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GraduateSurveyListResponse {
    private Long surveyId;
    private String title;
    private String description;
    private SurveyTypeResponse surveyType;
    private boolean completed;
    private LocalDateTime submittedAt;
    private LocalDateTime createdDate;
    private int questionCount;
} 