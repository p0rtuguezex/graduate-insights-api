package pe.com.graduate.insights.api.application.ports.output;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pe.com.graduate.insights.api.domain.models.response.HomeFeedResponse;

public interface HomeFeedRepositoryPort {

  /**
   * Obtiene una lista combinada de eventos y ofertas laborales ordenados por fecha descendente
   *
   * @param pageable Configuración de paginación
   * @return Lista paginada de eventos y ofertas laborales
   */
  Page<HomeFeedResponse> getHomeFeed(Pageable pageable);

  /**
   * Obtiene los elementos más recientes del home feed (sin paginación)
   *
   * @param limit Número máximo de elementos a retornar
   * @return Lista de eventos y ofertas laborales más recientes
   */
  List<HomeFeedResponse> getRecentHomeFeed(int limit);
}
