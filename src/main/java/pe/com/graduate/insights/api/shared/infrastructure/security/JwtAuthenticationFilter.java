package pe.com.graduate.insights.api.shared.infrastructure.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final JwtService jwtService;
  private final UserDetailsService userDetailsService;

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) {
    String path = request.getRequestURI();
    String method = request.getMethod();

    // No filtrar solicitudes OPTIONS - CORS preflight
    if ("OPTIONS".equals(method)) {
      log.debug("Saltando filtro JWT para petición OPTIONS en ruta: {}", path);
      return true;
    }

    // Verificar rutas públicas específicas
    boolean isPublicRoute =
        path.equals("/graduate-insights/v1/api/auth/login")
            || path.startsWith("/graduate-insights/v1/api/graduate/register")
            || path.startsWith("/graduate-insights/v1/api/employer/register")
            || path.startsWith("/graduate-insights/v1/api/director/register")
            || path.startsWith("/graduate-insights/v1/api/mail/")
            || path.startsWith("/graduate-insights/v1/api/v3/api-docs")
            || path.startsWith("/graduate-insights/v1/api/swagger-ui")
            || path.startsWith("/graduate-insights/v1/api/swagger-resources")
            || path.startsWith("/v3/api-docs")
            || path.startsWith("/swagger-ui")
            || path.startsWith("/swagger-resources");

    if (isPublicRoute) {
      log.info("SALTANDO filtro JWT para ruta pública: {} [{}]", path, method);
      return true;
    }

    // Log para rutas que SÍ necesitan autenticación
    log.info("APLICANDO filtro JWT para ruta protegida: {} [{}]", path, method);
    return false;
  }

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    try {
      final String authHeader = request.getHeader("Authorization");
      log.debug("Auth Header: {}", authHeader);
      final String jwt;
      final String userEmail;

      if (authHeader == null || !authHeader.startsWith("Bearer ")) {
        log.debug("Header de autorización no válido o no presente: {}", authHeader);
        filterChain.doFilter(request, response);
        return;
      }

      jwt = authHeader.substring(7);
      userEmail = jwtService.extractUsername(jwt);
      log.debug("Email extraído del token: {}", userEmail);

      if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);
        log.debug("Usuario cargado: {}", userDetails.getUsername());
        log.debug("Roles del usuario: {}", userDetails.getAuthorities());

        if (jwtService.isTokenValid(jwt, userDetails)) {
          log.debug("Token válido para el usuario: {}", userDetails.getUsername());

          UsernamePasswordAuthenticationToken authToken =
              new UsernamePasswordAuthenticationToken(
                  userDetails, null, userDetails.getAuthorities());
          authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
          SecurityContextHolder.getContext().setAuthentication(authToken);

          log.debug("Autenticación establecida en el contexto de seguridad");
        } else {
          log.warn("Token inválido para el usuario: {}", userDetails.getUsername());
        }
      }

      Authentication auth = SecurityContextHolder.getContext().getAuthentication();
      log.debug("Autenticación en el contexto: {}", auth);

      filterChain.doFilter(request, response);
    } catch (Exception e) {
      log.error("Error en el filtro JWT: {}", e.getMessage(), e);
      filterChain.doFilter(request, response);
    }
  }
}

