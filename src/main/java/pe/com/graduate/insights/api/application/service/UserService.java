package pe.com.graduate.insights.api.application.service;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pe.com.graduate.insights.api.application.ports.input.UserUseCase;
import pe.com.graduate.insights.api.application.ports.output.UserRepositoryPort;
import pe.com.graduate.insights.api.domain.models.request.UserRequest;
import pe.com.graduate.insights.api.domain.models.response.User;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService implements UserUseCase {

  private final UserRepositoryPort userRepositoryPort;

  @Override
  public void save(UserRequest request) {
    userRepositoryPort.save(request);
  }

  @Override
  public Optional<User> getDomain(Long id) {
    return userRepositoryPort.getDomain(id);
  }
}
