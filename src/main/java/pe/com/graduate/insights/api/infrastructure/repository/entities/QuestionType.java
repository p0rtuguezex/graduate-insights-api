package pe.com.graduate.insights.api.infrastructure.repository.entities;

public enum QuestionType {
  SINGLE_CHOICE, // Una sola opción (radio buttons)
  MULTIPLE_CHOICE, // Múltiples opciones (checkboxes)
  SCALE, // Escala numérica (1-5, 1-10)
  TEXT, // Respuesta de texto libre
  NUMBER, // Respuesta de texto libre
  DATE, // Respuesta de texto libre
  EMAIL, // Respuesta de texto libre
  PHONE, // Respuesta de texto libre
  YES_NO // Pregunta de Sí/No
}
