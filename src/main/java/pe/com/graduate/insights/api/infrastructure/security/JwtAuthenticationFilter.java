package pe.com.graduate.insights.api.infrastructure.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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

import java.io.IOException;

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
        
        // No filtrar solicitudes OPTIONS
        log.info("Ruta: {}, Método: {}", path, method);
        if ("OPTIONS".equals(method)) {
            return true;
        }
        
        // No filtrar rutas públicas - asegúrate de que /auth/login esté aquí pero NO /auth/me
        return path.equals("/api/v1/auth/login") ||
               path.startsWith("/api/v1/api-docs") ||
               path.startsWith("/api/v1/swagger-ui") ||
               path.startsWith("/api/v1/swagger-resources") ||
               path.startsWith("/api/v1/webjars") ||
               path.startsWith("/api/v1/v3/api-docs");
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
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

                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                    );
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