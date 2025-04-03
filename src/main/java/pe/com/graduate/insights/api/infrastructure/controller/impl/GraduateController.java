package pe.com.graduate.insights.api.infrastructure.controller.impl;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;
import pe.com.graduate.insights.api.domain.models.response.Graduate;
import pe.com.graduate.insights.api.infrastructure.controller.GraduateApi;

@Slf4j
@RestController
public class GraduateController implements GraduateApi {

  @Override
  public List<Graduate> getListSpeakerAll() {
    return null;
  }
}
