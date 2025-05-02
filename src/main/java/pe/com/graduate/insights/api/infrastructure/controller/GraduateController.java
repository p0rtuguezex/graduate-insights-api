package pe.com.graduate.insights.api.infrastructure.controller;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.com.graduate.insights.api.domain.models.response.GraduateResponse;

@RestController
@RequestMapping("/graduate")
public class GraduateController {

  @GetMapping("list-all")
  public List<GraduateResponse> getListSpeakerAll() {
    return null;
  }
}
