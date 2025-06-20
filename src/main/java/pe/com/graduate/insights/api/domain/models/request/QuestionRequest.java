package pe.com.graduate.insights.api.domain.models.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pe.com.graduate.insights.api.infrastructure.repository.entities.QuestionType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuestionRequest {
    private Long id;
    
    @NotBlank(message = "El texto de la pregunta es requerido")
    @Size(max = 500, message = "El texto de la pregunta no puede exceder los 500 caracteres")
    private String questionText;
    
    @NotNull(message = "El tipo de pregunta es requerido")
    private QuestionType questionType;
    
    private boolean required;
    
    @Size(min = 2, message = "Debe incluir al menos dos opciones para preguntas de opción múltiple o única")
    private List<QuestionOptionRequest> options;
} 