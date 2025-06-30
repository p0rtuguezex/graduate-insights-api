package pe.com.graduate.insights.api.domain.utils;

public class ConstantsUtils {

  public static final String USER_NOT_FOUND = "Usuario no encontrado con id: %s";
  public static final String USER_NOT_FOUND_BY_EMAIL = "Usuario no encontrado con email: %s";
  public static final String GRADUATE_NOT_FOUND = "Graduado no encontrado con id: %s";
  public static final String DIRECTOR_NOT_FOUND = "Director no encontrado con id: %s";
  public static final String EMPLOYER_NOT_FOUND = "Empleador no encontrado con id: %s";
  public static final String EDUCATION_CENTER_NOT_FOUND_ID =
      "Centro de Educación no encontrado con id: %s";
  public static final String EVENT_TYPES_NOT_FOUND_ID = "Tipo de evento no encontrado con id: %s";
  public static final String JOB_NOT_FOUND = "Trabajo no encontrado con id: %s";
  public static final String JOB_OFFERS_NOT_FOUND = "Oferta laboral no encontrada con id: %s";
  public static final String JOB_NOT_FOUND_BY_GRADUATE =
      "EL graduado con id: %s, no tiene empleos registrados.";
  public static final String RESET_PASSWORD = "1";
  public static final String SENT_CODE_VALIDATED = "2";
  public static final String JOB_OFFERS_NOT_FOUND_BY_EMPLOYER =
      "EL empleador con id: %s, no tiene ofertas de empleo registrados.";
  public static final String USER_CONFLICT = "Usuario ya existe con el correo: %s";
  public static final String EDUCATION_CENTER_CONFLICT =
      "El Centro de educación ya existe con el nombre: %s";
  public static final String EVENT_TYPES_CENTER_CONFLICT =
      "El tipo de evento ya existe con el nombre: %s";
  public static final String STATUS_ACTIVE = "1";
  public static final String STATUS_INACTIVE = "0";
  public static final String SURVEY_NOT_FOUND = "Encuesta no encontrada con ID: %s";
  public static final String SURVEY_CONFLICT = "Titulo de encuesta ya existe: %s";
  public static final String SURVEY_TYPE_CONFLICT = "Titulo de tipo de encuesta ya existe: %s";

  // Roles
  public static final String ROLE_DIRECTOR = "ROLE_DIRECTOR";
  public static final String ROLE_EMPLOYER = "ROLE_EMPLOYER";
  public static final String ROLE_GRADUATE = "ROLE_GRADUATE";
  public static final String ROLE_USER = "ROLE_USER";

  // utils
  public static final Integer NUMBER_100000 = 100000;
  public static final Integer NUMBER_900000 = 900000;

  // email
  public static final String SUBJECT_EMAIL = "Recuperación de contraseña";
  public static final String SUBJECT_USER_VALIDATED = "Verificación de cuenta";
  public static final String USER_KEY = "{{user}}";
  public static final String CODE_KEY = "{{code}}";
  public static final String SISEG = "SISTEMA DE SEGUIMIENTO DE EGRESADOS";

  // templates
  public static final String TEMPLATE_EMAIL_HTML =
      "<!DOCTYPE html> <html lang=\"es\"> <head> <meta charset=\"UTF-8\"> <meta name=\"viewport\" content=\"width=device-width, "
          + "initial-scale=1.0\"> <title>Recuperación de contraseña</title> <style> body { font-family: Arial, sans-serif; "
          + "background-color: #f7f7f7; margin: 0; padding: 0; color: #333; } .container { max-width: 600px; margin: 20px auto; "
          + "background-color: #ffffff; padding: 20px; border: 1px solid #dddddd; border-radius: 5px; box-shadow: 0 0 10px rgba(0, 0, 0, 0.1); } "
          + ".header { text-align: center; padding-bottom: 20px; } .header h1 { color: #22a127; } .content { font-size: 16px; line-height: 1.5; text-align: justify; } "
          + ".code { display: block; width: fit-content; margin: 20px auto; padding: 10px 20px; background-color: #22a127; color: #ffffff; letter-spacing: 2px;"
          + "font-weight: bold; border-radius: 5px; font-size: 18px; text-align: center; } .footer { margin-top: 20px; font-size: 14px; "
          + "text-align: center; color: #888888; } </style> </head> <body> <div class=\"container\"> "
          + "<div class=\"content\"> <p>Hola {{user}}</p> <p>Hemos recibido una solicitud para restablecer tu contraseña. "
          + "Utiliza el siguiente código de verificación para completar el proceso de recuperación:</p> <div class=\"code\">{{code}}</div> "
          + "<p>Si no solicitaste un cambio de contraseña, por favor, "
          + "ignora este correo o contacta a nuestro equipo de soporte para más ayuda.</p> <p>Gracias por tu atención.</p> </div> <div "
          + "class=\"footer\"> <p>Atentamente,</p> <p>SISEG 2025</p> <p>garlee016@gmail.com | 954886187</p> </div> "
          + "</div> </body> </html> ";
  public static final String TEMPLATE_EMAIL_HTML_USER_VALIDATED =
      "<!DOCTYPE html> <html lang=\"es\"> <head> <meta charset=\"UTF-8\"> <meta name=\"viewport\" content=\"width=device-width, "
          + "initial-scale=1.0\"> <title>Validación de cuenta</title> <style> body { font-family: Arial, sans-serif; "
          + "background-color: #f7f7f7; margin: 0; padding: 0; color: #333; } .container { max-width: 600px; margin: 20px auto; "
          + "background-color: #ffffff; padding: 20px; border: 1px solid #dddddd; border-radius: 5px; box-shadow: 0 0 10px rgba(0, 0, 0, 0.1); } "
          + ".header { text-align: center; padding-bottom: 20px; } .header h1 { color: #22a127; } .content { font-size: 16px; line-height: 1.5; text-align: justify; } "
          + ".code { display: block; width: fit-content; margin: 20px auto; padding: 10px 20px; background-color: #22a127; color: #ffffff; letter-spacing: 2px;"
          + "font-weight: bold; border-radius: 5px; font-size: 18px; text-align: center; } .footer { margin-top: 20px; font-size: 14px; "
          + "text-align: center; color: #888888; } </style> </head> <body> <div class=\"container\"> "
          + "<div class=\"content\"> <p>Hola {{user}}</p> <p>"
          + "Utiliza el siguiente código de verificación para validar su cuenta:</p> <div class=\"code\">{{code}}</div> "
          + "<p>Por favor, ingresa este código en la página web de inicio de sesión del SISEG 2025. Si no solicitaste este código, por favor, "
          + "ignora este correo o contacta a nuestro equipo de soporte para más ayuda.</p> <p>Gracias por tu atención.</p> </div> <div "
          + "class=\"footer\"> <p>Atentamente,</p> <p>SISEG 2025</p> <p>garlee016@gmail.com | 954886187</p> </div> "
          + "</div> </body> </html> ";
}
