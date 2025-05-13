package pe.com.graduate.insights.api.infrastructure.aop;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
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
    log.debug("Executing: {}, with args: {}", joinPoint.getSignature(), args);
  }

  @AfterReturning(pointcut = "repositoryAdapterLayer()", returning = "result")
  public void logAfter(JoinPoint joinPoint, Object result) {
    result = objectToString(result, "NOT RETURN VALUE");
    log.debug("Executed: {} | Result: {}", joinPoint.getSignature(), result);
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

  private String objectToString(Object object, String otherwise) {
    if (Objects.isNull(object)) {
      return otherwise;
    }
    try {
      ObjectMapper objectMapper = new ObjectMapper();
      objectMapper.registerModule(new JavaTimeModule());
      objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
      return objectMapper.writeValueAsString(object);
    } catch (JsonProcessingException e) {
      log.warn("Error converting object to string: {}", e.getMessage());
      return otherwise;
    }
  }
}
