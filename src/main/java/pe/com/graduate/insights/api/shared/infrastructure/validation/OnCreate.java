package pe.com.graduate.insights.api.shared.infrastructure.validation;

/**
 * Validation group applied only when creating a resource. Constraints annotated with this group
 * (e.g. mandatory password) are enforced on create endpoints but skipped on updates.
 */
public interface OnCreate {}
