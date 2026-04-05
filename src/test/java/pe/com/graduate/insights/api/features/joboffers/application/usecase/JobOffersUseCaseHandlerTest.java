package pe.com.graduate.insights.api.features.joboffers.application.usecase;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import pe.com.graduate.insights.api.features.joboffers.application.dto.JobOffersRequest;
import pe.com.graduate.insights.api.features.joboffers.application.dto.JobOffersResponse;
import pe.com.graduate.insights.api.features.joboffers.application.ports.output.JobOffersRepositoryPort;

@ExtendWith(MockitoExtension.class)
class JobOffersUseCaseHandlerTest {

  @Mock private JobOffersRepositoryPort jobOffersRepositoryPort;

  @InjectMocks private JobOffersUseCaseHandler jobOffersUseCaseHandler;

  @Test
  void getDomainByRoleShouldDelegateToRepositoryPort() {
    JobOffersResponse expected = JobOffersResponse.builder().build();
    when(jobOffersRepositoryPort.getDomainByRole(10L, true, 1L)).thenReturn(expected);

    JobOffersResponse result = jobOffersUseCaseHandler.getDomainByRole(10L, true, 1L);

    assertSame(expected, result);
    verify(jobOffersRepositoryPort).getDomainByRole(10L, true, 1L);
  }

  @Test
  void saveByRoleShouldDelegateToRepositoryPort() {
    JobOffersRequest request = JobOffersRequest.builder().build();

    jobOffersUseCaseHandler.saveByRole(request, false, 8L);

    verify(jobOffersRepositoryPort).saveByRole(request, false, 8L);
  }

  @Test
  void getPaginationByRoleShouldDelegateToRepositoryPort() {
    Pageable pageable = PageRequest.of(0, 10);
    Page<JobOffersResponse> expected = new PageImpl<>(List.of(), pageable, 0);
    when(jobOffersRepositoryPort.getPaginationByRole("dev", pageable, false, 9L))
        .thenReturn(expected);

    Page<JobOffersResponse> result =
        jobOffersUseCaseHandler.getPaginationByRole("dev", pageable, false, 9L);

    assertSame(expected, result);
    verify(jobOffersRepositoryPort).getPaginationByRole("dev", pageable, false, 9L);
  }
}
