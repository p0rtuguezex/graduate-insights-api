package pe.com.graduate.insights.api.infrastructure.repository.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "question_options")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuestionOptionEntity extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String optionText;
    private Integer orderNumber;
    
    @ManyToOne
    @JoinColumn(name = "question_id")
    private QuestionEntity question;
} 