package pe.com.graduate.insights.api.application.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pe.com.graduate.insights.api.application.ports.input.UserUseCase;
import pe.com.graduate.insights.api.application.ports.output.UserRepositoryPort;
import pe.com.graduate.insights.api.domain.models.request.UserRequest;
import pe.com.graduate.insights.api.domain.models.response.UserResponse;

@Service
@RequiredArgsConstructor
public class UserService implements UserUseCase {

  private final UserRepositoryPort userRepositoryPort;

  @Override
  public void save(UserRequest request) {
    userRepositoryPort.save(request);
  }

  @Override
  public UserResponse getDomain(Long id) {
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
  public Page<UserResponse> getPagination(String search, Pageable pageable) {
    return userRepositoryPort.getPagination(search, pageable);
  }

  @Override
  public List<UserResponse> getList() {
    return userRepositoryPort.getList();
  }
}
