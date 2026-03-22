package pe.com.graduate.insights.api.features.user.application.ports.input;

import pe.com.graduate.insights.api.shared.ports.generic.GenericCreate;
import pe.com.graduate.insights.api.shared.ports.generic.GenericDelete;
import pe.com.graduate.insights.api.shared.ports.generic.GenericList;
import pe.com.graduate.insights.api.shared.ports.generic.GenericPaginate;
import pe.com.graduate.insights.api.shared.ports.generic.GenericRead;
import pe.com.graduate.insights.api.shared.ports.generic.GenericUpdate;
import pe.com.graduate.insights.api.features.user.application.dto.ProfileUpdateRequest;
import pe.com.graduate.insights.api.features.user.application.dto.UserRequest;
import pe.com.graduate.insights.api.features.user.application.dto.UserResponse;

public interface UserUseCase
    extends GenericCreate<UserRequest>,
        GenericRead<UserResponse>,
        GenericUpdate<UserRequest>,
        GenericDelete,
        GenericPaginate<UserResponse>,
        GenericList<UserResponse> {

  void updateProfile(ProfileUpdateRequest request, Long id);

  void updatePassword(Long id, String newPassword);
}

