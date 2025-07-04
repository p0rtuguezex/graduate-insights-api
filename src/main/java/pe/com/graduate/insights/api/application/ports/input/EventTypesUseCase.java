package pe.com.graduate.insights.api.application.ports.input;

import pe.com.graduate.insights.api.application.ports.generic.GenericCreate;
import pe.com.graduate.insights.api.application.ports.generic.GenericDelete;
import pe.com.graduate.insights.api.application.ports.generic.GenericList;
import pe.com.graduate.insights.api.application.ports.generic.GenericPaginate;
import pe.com.graduate.insights.api.application.ports.generic.GenericRead;
import pe.com.graduate.insights.api.application.ports.generic.GenericUpdate;
import pe.com.graduate.insights.api.domain.models.request.EventTypesRequest;
import pe.com.graduate.insights.api.domain.models.response.EventTypesResponse;
import pe.com.graduate.insights.api.domain.models.response.KeyValueResponse;

public interface EventTypesUseCase
    extends GenericCreate<EventTypesRequest>,
        GenericUpdate<EventTypesRequest>,
        GenericList<KeyValueResponse>,
        GenericRead<EventTypesResponse>,
        GenericPaginate<EventTypesResponse>,
        GenericDelete {}
