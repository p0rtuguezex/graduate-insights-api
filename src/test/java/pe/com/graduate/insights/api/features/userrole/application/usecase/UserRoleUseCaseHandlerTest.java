package pe.com.graduate.insights.api.features.userrole.application.usecase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.com.graduate.insights.api.features.userrole.application.ports.output.UserRoleRepositoryPort;
import pe.com.graduate.insights.api.shared.security.UserRole;

@ExtendWith(MockitoExtension.class)
class UserRoleUseCaseHandlerTest {

  @Mock private UserRoleRepositoryPort userRoleRepositoryPort;

  @InjectMocks private UserRoleUseCaseHandler userRoleUseCaseHandler;

  @Test
  void getUserRoleShouldDelegateToRepositoryPort() {
    when(userRoleRepositoryPort.getUserRole(5L)).thenReturn(UserRole.DIRECTOR);

    UserRole role = userRoleUseCaseHandler.getUserRole(5L);

    assertEquals(UserRole.DIRECTOR, role);
    verify(userRoleRepositoryPort).getUserRole(5L);
  }

  @Test
  void getUserRoleDisplayNameShouldDelegateToRepositoryPort() {
    when(userRoleRepositoryPort.getUserRoleDisplayName(9L)).thenReturn("Director");

    String role = userRoleUseCaseHandler.getUserRoleDisplayName(9L);

    assertEquals("Director", role);
    verify(userRoleRepositoryPort).getUserRoleDisplayName(9L);
  }
}
