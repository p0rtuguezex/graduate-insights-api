package pe.com.graduate.insights.api.domain.models.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GraduateSurveyResponseRequest {
    @NotNull(message = "El ID de la encuesta es requerido")
    private Long surveyId;

    @NotNull(message = "El ID del graduado es requerido")
    private Long graduateId;

    @Size(max = 255, message = "La posición actual no puede exceder los 255 caracteres")
    private String currentPosition;

    @Size(max = 255, message = "La empresa actual no puede exceder los 255 caracteres")
    private String currentCompany;

    private BigDecimal currentSalaryRange;

    @Size(max = 255, message = "La ubicación no puede exceder los 255 caracteres")
    private String location;

    @NotNull(message = "Las respuestas son requeridas")
    private List<GraduateQuestionResponseRequest> responses;
} 