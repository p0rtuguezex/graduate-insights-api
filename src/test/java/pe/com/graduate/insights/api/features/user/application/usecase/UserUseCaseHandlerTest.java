package pe.com.graduate.insights.api.features.user.application.usecase;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.com.graduate.insights.api.features.user.application.dto.UserResponse;
import pe.com.graduate.insights.api.features.user.application.ports.output.UserRepositoryPort;

@ExtendWith(MockitoExtension.class)
class UserUseCaseHandlerTest {

  @Mock private UserRepositoryPort userRepositoryPort;

  @InjectMocks private UserUseCaseHandler userUseCaseHandler;

  @Test
  void getListShouldDelegateToRepositoryPort() {
    List<UserResponse> expected = List.of();
    when(userRepositoryPort.getList()).thenReturn(expected);

    List<UserResponse> result = userUseCaseHandler.getList();

    assertSame(expected, result);
    verify(userRepositoryPort).getList();
  }

  @Test
  void getDomainShouldDelegateToRepositoryPort() {
    UserResponse expected = UserResponse.builder().build();
    when(userRepositoryPort.getDomain(3L)).thenReturn(expected);

    UserResponse result = userUseCaseHandler.getDomain(3L);

    assertSame(expected, result);
    verify(userRepositoryPort).getDomain(3L);
  }
}
