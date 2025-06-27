package pe.com.graduate.insights.api.infrastructure.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = PasswordConstraintValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface PasswordConstraint {
  String message() default
      "La contraseña debe tener al menos 8 caracteres y contener al menos un dígito, una letra mayúscula, una letra minúscula y un carácter especial";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
