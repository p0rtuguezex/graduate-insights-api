package pe.com.graduate.insights.api.application.ports.input;

import pe.com.graduate.insights.api.application.ports.generic.*;
import pe.com.graduate.insights.api.domain.models.request.UserRequest;
import pe.com.graduate.insights.api.domain.models.response.User;

public interface UserUseCase
    extends GenericCreate<UserRequest>,
        GenericRead<User>,
        GenericUpdate<UserRequest>,
        GenericList<User>, GenericDelete,GenericPaginate<User> {}
