package pe.com.graduate.insights.api.features.survey.infrastructure.entity;

public enum SurveyStatus {
  DRAFT, // Borrador - En construcción
  ACTIVE, // Activa - Disponible para responder
  PAUSED, // Pausada - Temporalmente no disponible
  CLOSED, // Cerrada - Ya no acepta respuestas
  COMPLETED // Completada - Finalizada y procesada
}

