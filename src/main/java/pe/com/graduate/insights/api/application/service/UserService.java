package pe.com.graduate.insights.api.application.service;

import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pe.com.graduate.insights.api.application.ports.input.UserUseCase;
import pe.com.graduate.insights.api.application.ports.output.UserRepositoryPort;
import pe.com.graduate.insights.api.domain.models.request.UserRequest;
import pe.com.graduate.insights.api.domain.models.response.User;

@Slf4j
@AllArgsConstructor
@Service
public class UserService implements UserUseCase {

  private final UserRepositoryPort userRepositoryPort;

  @Override
  public List<User> getList(Object... params) {
    return userRepositoryPort.getList(params);
  }

  @Override
  public void save(UserRequest request) {
    userRepositoryPort.save(request);
  }

  @Override
  public Optional<User> getDomain(Long id) {
    return userRepositoryPort.getDomain(id);
  }

  @Override
  public void update(UserRequest request, Long id) {
    userRepositoryPort.update(request, id);
  }

  @Override
  public void delete(Long id) {
    userRepositoryPort.delete(id);

  }

  @Override
  public Page<User> getPagination(String search, Pageable pageable) {
    return userRepositoryPort.getPagination(search,pageable);
  }
}
