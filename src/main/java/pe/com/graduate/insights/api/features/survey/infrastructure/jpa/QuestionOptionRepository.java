package pe.com.graduate.insights.api.features.survey.infrastructure.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.com.graduate.insights.api.features.survey.infrastructure.entity.QuestionOptionEntity;

@Repository
public interface QuestionOptionRepository extends JpaRepository<QuestionOptionEntity, Long> {}

