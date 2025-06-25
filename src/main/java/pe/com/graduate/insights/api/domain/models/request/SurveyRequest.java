package pe.com.graduate.insights.api.domain.models.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import pe.com.graduate.insights.api.infrastructure.repository.entities.SurveyStatus;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Future;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SurveyRequest implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    
    @NotBlank(message = "El título es requerido")
    @Size(max = 255, message = "El título no puede exceder los 255 caracteres")
    private String title;
    
    @Size(max = 1000, message = "La descripción no puede exceder los 1000 caracteres")
    private String description;
    
    @NotNull(message = "El tipo de encuesta es requerido")
    private Long surveyTypeId;
    
    @NotNull(message = "El estado de la encuesta es requerido")
    private SurveyStatus status;
    
    @NotNull(message = "La fecha de inicio es requerida")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;
    
    @Future(message = "La fecha de fin debe ser posterior a la fecha actual")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;
    
    @NotNull(message = "La lista de preguntas es requerida")
    @Size(min = 1, message = "Debe incluir al menos una pregunta")
    private List<QuestionRequest> questions;
} 