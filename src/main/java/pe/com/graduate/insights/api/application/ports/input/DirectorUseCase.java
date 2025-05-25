package pe.com.graduate.insights.api.application.ports.input;

import pe.com.graduate.insights.api.application.ports.generic.GenericCreate;
import pe.com.graduate.insights.api.application.ports.generic.GenericDelete;
import pe.com.graduate.insights.api.application.ports.generic.GenericList;
import pe.com.graduate.insights.api.application.ports.generic.GenericPaginate;
import pe.com.graduate.insights.api.application.ports.generic.GenericRead;
import pe.com.graduate.insights.api.application.ports.generic.GenericUpdate;
import pe.com.graduate.insights.api.domain.models.request.DirectorRequest;
import pe.com.graduate.insights.api.domain.models.response.DirectorResponse;

public interface DirectorUseCase
    extends GenericCreate<DirectorRequest>,
        GenericUpdate<DirectorRequest>,
        GenericList<DirectorResponse>,
        GenericRead<DirectorResponse>,
        GenericPaginate<DirectorResponse>,
        GenericDelete {}
