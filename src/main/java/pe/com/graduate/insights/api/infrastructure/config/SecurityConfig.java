package pe.com.graduate.insights.api.infrastructure.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;
import pe.com.graduate.insights.api.infrastructure.security.JwtAuthenticationFilter;

@Slf4j
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private final JwtAuthenticationFilter jwtAuthFilter;
  private final AuthenticationProvider authenticationProvider;
    private final CorsConfigurationSource corsConfigurationSource;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    log.info("Configurando SecurityFilterChain");

    return http
        .securityMatcher("/graduate-insights/v1/api/**")
        .csrf(AbstractHttpConfigurer::disable)
        .cors(cors -> cors.configurationSource(corsConfigurationSource))
        .sessionManagement(
            session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(
            auth ->
                auth
                    // IMPORTANTE: Permitir TODAS las peticiones OPTIONS primero (para CORS preflight)
                    .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                    // Luego las rutas específicas de autenticación
                    .requestMatchers("/graduate-insights/v1/api/auth/login").permitAll()
                    .requestMatchers("/graduate-insights/v1/api/graduate/register").permitAll()
                    .requestMatchers("/graduate-insights/v1/api/employer/register").permitAll()
                    .requestMatchers("/graduate-insights/v1/api/director/register").permitAll()
                    .requestMatchers("/graduate-insights/v1/api/mail/**").permitAll()
                    // Rutas protegidas
                    .requestMatchers("/graduate-insights/v1/api/auth/me").authenticated()
                    // Documentación API
                    .requestMatchers(
                        "/v3/api-docs/**",
                        "/swagger-ui/**",
                        "/swagger-ui.html",
                        "/swagger-resources/**")
                    .permitAll()
                    .anyRequest().authenticated())
        .authenticationProvider(authenticationProvider)
        .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
        .build();
  }
}
