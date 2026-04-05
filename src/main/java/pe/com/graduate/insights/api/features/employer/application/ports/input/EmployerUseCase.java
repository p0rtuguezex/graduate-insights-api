package pe.com.graduate.insights.api.features.employer.application.ports.input;

import pe.com.graduate.insights.api.features.employer.application.dto.EmployerRequest;
import pe.com.graduate.insights.api.features.employer.application.dto.EmployerResponse;
import pe.com.graduate.insights.api.shared.models.response.KeyValueResponse;
import pe.com.graduate.insights.api.shared.ports.generic.GenericCreate;
import pe.com.graduate.insights.api.shared.ports.generic.GenericDelete;
import pe.com.graduate.insights.api.shared.ports.generic.GenericList;
import pe.com.graduate.insights.api.shared.ports.generic.GenericPaginate;
import pe.com.graduate.insights.api.shared.ports.generic.GenericRead;
import pe.com.graduate.insights.api.shared.ports.generic.GenericUpdate;

public interface EmployerUseCase
    extends GenericCreate<EmployerRequest>,
        GenericUpdate<EmployerRequest>,
        GenericList<KeyValueResponse>,
        GenericRead<EmployerResponse>,
        GenericPaginate<EmployerResponse>,
        GenericDelete {}
