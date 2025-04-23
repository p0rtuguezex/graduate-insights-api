package pe.com.graduate.insights.api.application.ports.output;

import pe.com.graduate.insights.api.application.ports.generic.*;
import pe.com.graduate.insights.api.domain.models.request.UserRequest;
import pe.com.graduate.insights.api.domain.models.response.User;

public interface UserRepositoryPort
    extends GenericCreate<UserRequest>,
        GenericRead<User>,
        GenericUpdate<UserRequest>,
        GenericDelete,
        GenericList<User>,GenericPaginate<User> {}
