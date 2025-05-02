package pe.com.graduate.insights.api.infrastructure.aop;

import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.builder.RecursiveToStringStyle;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

@Aspect
@Component
@Slf4j
public class LogginAspect {

  @Pointcut("execution(* pe.com.graduate.insights.api.infrastructure.repository.adapter..*(..))")
  public void repositoryAdapterLayer() {}

  @Pointcut(
      "within(pe.com.graduate.insights.api.infrastructure.controller..*) || "
          + "within(pe.com.graduate.insights.api.application.service..*) ||"
          + "within(pe.com.graduate.insights.api.infrastructure.repository.adapter..*)")
  public void controllerServiceRepositoryLayer() {}

  @Before("repositoryAdapterLayer()")
  public void logBefore(JoinPoint joinPoint) {
    String args = objectToString(joinPoint.getArgs(), "NOT ARGS");
    log.info("Executing: {}, with args: {}", joinPoint.getSignature(), args);
  }

  @AfterReturning(pointcut = "repositoryAdapterLayer()", returning = "result")
  public void logAfter(JoinPoint joinPoint, Object result) {
    result = objectToString(result, "NOT RETURN VALUE");
    log.info("Executed: {} | Result: {}", joinPoint.getSignature(), result);
  }

  @AfterThrowing(pointcut = "controllerServiceRepositoryLayer()", throwing = "ex")
  public void logException(JoinPoint joinPoint, Throwable ex) {
    Object cause = ObjectUtils.isEmpty(ex.getCause()) ? "NULL" : ex.getCause();
    log.error(
        "Exception in {} with cause: {} and exception: {}",
        joinPoint.getSignature(),
        cause,
        ex.getMessage(),
        ex);
  }

  //  private String objectToString(Object object, String otherwise) {
  //    return Objects.isNull(object)
  //        ? otherwise
  //        : new ReflectionToStringBuilder(object, RecursiveToStringStyle.JSON_STYLE).toString();
  //  }

  // metodo que evita la reflexion de list -- modificado del anterior
  private String objectToString(Object object, String otherwise) {
    if (Objects.isNull(object)) {
      return otherwise;
    }

    // Evita reflexión en tipos del JDK que pueden lanzar errores
    if (object instanceof Iterable
        || object instanceof Number
        || object instanceof String
        || object.getClass().isArray()) {
      return object.toString();
    }

    try {
      return new ReflectionToStringBuilder(object, RecursiveToStringStyle.JSON_STYLE).toString();
    } catch (Exception e) {
      log.warn(
          "Reflection failed on object of type {}: {}",
          object.getClass().getName(),
          e.getMessage());
      return object.toString(); // fallback
    }
  }
}
