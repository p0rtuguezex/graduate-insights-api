package pe.com.graduate.insights.api.features.auth.domain.model;

import lombok.Builder;

@Builder
public record AuthenticatedUser(
    Long id, String firstName, String lastName, String email, String genero, boolean verified) {}
