package pe.com.graduate.insights.api.features.user.application.usecase;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pe.com.graduate.insights.api.features.user.application.dto.ProfileUpdateRequest;
import pe.com.graduate.insights.api.features.user.application.dto.UserRequest;
import pe.com.graduate.insights.api.features.user.application.dto.UserResponse;
import pe.com.graduate.insights.api.features.user.application.ports.input.UserUseCase;
import pe.com.graduate.insights.api.features.user.application.ports.output.UserRepositoryPort;

@Service
@RequiredArgsConstructor
public class UserUseCaseHandler implements UserUseCase {

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

  @Override
  public void updateProfile(ProfileUpdateRequest request, Long id) {
    userRepositoryPort.updateProfile(request, id);
  }

  @Override
  public void updatePassword(Long id, String newPassword) {
    userRepositoryPort.updatePassword(id, newPassword);
  }
}
