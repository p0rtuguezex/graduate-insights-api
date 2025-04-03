package pe.com.graduate.insights.api.infrastructure.controller;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pe.com.graduate.insights.api.domain.models.response.Graduate;

@RequestMapping("/speaker")
public interface GraduateApi {

  @GetMapping("list-all")
  List<Graduate> getListSpeakerAll();
}
