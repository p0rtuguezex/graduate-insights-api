package pe.com.graduate.insights.api.application.ports.input;

import pe.com.graduate.insights.api.application.ports.generic.GenericCreate;
import pe.com.graduate.insights.api.application.ports.generic.GenericDelete;
import pe.com.graduate.insights.api.application.ports.generic.GenericList;
import pe.com.graduate.insights.api.application.ports.generic.GenericPaginate;
import pe.com.graduate.insights.api.application.ports.generic.GenericRead;
import pe.com.graduate.insights.api.application.ports.generic.GenericUpdate;
import pe.com.graduate.insights.api.domain.models.request.UserRequest;
import pe.com.graduate.insights.api.domain.models.response.UserResponse;

public interface UserUseCase
    extends GenericCreate<UserRequest>,
        GenericRead<UserResponse>,
        GenericUpdate<UserRequest>,
        GenericList<UserResponse>,
        GenericDelete,
        GenericPaginate<UserResponse> {}
