package pe.com.graduate.insights.api.features.director.application.ports.output;

import pe.com.graduate.insights.api.features.director.application.dto.DirectorRequest;
import pe.com.graduate.insights.api.features.director.application.dto.DirectorResponse;
import pe.com.graduate.insights.api.shared.models.response.KeyValueResponse;
import pe.com.graduate.insights.api.shared.ports.generic.GenericCreate;
import pe.com.graduate.insights.api.shared.ports.generic.GenericDelete;
import pe.com.graduate.insights.api.shared.ports.generic.GenericList;
import pe.com.graduate.insights.api.shared.ports.generic.GenericPaginate;
import pe.com.graduate.insights.api.shared.ports.generic.GenericRead;
import pe.com.graduate.insights.api.shared.ports.generic.GenericUpdate;

public interface DirectorRepositoryPort
    extends GenericCreate<DirectorRequest>,
        GenericUpdate<DirectorRequest>,
        GenericList<KeyValueResponse>,
        GenericRead<DirectorResponse>,
        GenericPaginate<DirectorResponse>,
        GenericDelete {}
