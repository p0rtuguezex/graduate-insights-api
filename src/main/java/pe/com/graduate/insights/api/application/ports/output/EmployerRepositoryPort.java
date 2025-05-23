package pe.com.graduate.insights.api.application.ports.output;

import pe.com.graduate.insights.api.application.ports.generic.GenericCreate;
import pe.com.graduate.insights.api.application.ports.generic.GenericDelete;
import pe.com.graduate.insights.api.application.ports.generic.GenericList;
import pe.com.graduate.insights.api.application.ports.generic.GenericPaginate;
import pe.com.graduate.insights.api.application.ports.generic.GenericRead;
import pe.com.graduate.insights.api.application.ports.generic.GenericUpdate;
import pe.com.graduate.insights.api.domain.models.request.EmployerRequest;
import pe.com.graduate.insights.api.domain.models.response.EmployerResponse;

public interface EmployerRepositoryPort
    extends GenericCreate<EmployerRequest>,
        GenericUpdate<EmployerRequest>,
        GenericList<EmployerResponse>,
        GenericRead<EmployerResponse>,
        GenericPaginate<EmployerResponse>,
        GenericDelete {}
