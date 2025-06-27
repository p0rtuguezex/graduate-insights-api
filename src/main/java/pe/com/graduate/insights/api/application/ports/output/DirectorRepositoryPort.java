package pe.com.graduate.insights.api.application.ports.output;

import pe.com.graduate.insights.api.application.ports.generic.GenericCreate;
import pe.com.graduate.insights.api.application.ports.generic.GenericDelete;
import pe.com.graduate.insights.api.application.ports.generic.GenericList;
import pe.com.graduate.insights.api.application.ports.generic.GenericPaginate;
import pe.com.graduate.insights.api.application.ports.generic.GenericRead;
import pe.com.graduate.insights.api.application.ports.generic.GenericUpdate;
import pe.com.graduate.insights.api.domain.models.request.DirectorRequest;
import pe.com.graduate.insights.api.domain.models.response.DirectorResponse;
import pe.com.graduate.insights.api.domain.models.response.KeyValueResponse;

public interface DirectorRepositoryPort
    extends GenericCreate<DirectorRequest>,
        GenericUpdate<DirectorRequest>,
        GenericList<KeyValueResponse>,
        GenericRead<DirectorResponse>,
        GenericPaginate<DirectorResponse>,
        GenericDelete {}
