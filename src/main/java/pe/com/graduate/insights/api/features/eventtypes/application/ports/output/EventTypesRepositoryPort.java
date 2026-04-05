package pe.com.graduate.insights.api.features.eventtypes.application.ports.output;

import pe.com.graduate.insights.api.features.eventtypes.application.dto.EventTypesRequest;
import pe.com.graduate.insights.api.features.eventtypes.application.dto.EventTypesResponse;
import pe.com.graduate.insights.api.shared.models.response.KeyValueResponse;
import pe.com.graduate.insights.api.shared.ports.generic.GenericCreate;
import pe.com.graduate.insights.api.shared.ports.generic.GenericDelete;
import pe.com.graduate.insights.api.shared.ports.generic.GenericList;
import pe.com.graduate.insights.api.shared.ports.generic.GenericPaginate;
import pe.com.graduate.insights.api.shared.ports.generic.GenericRead;
import pe.com.graduate.insights.api.shared.ports.generic.GenericUpdate;

public interface EventTypesRepositoryPort
    extends GenericCreate<EventTypesRequest>,
        GenericUpdate<EventTypesRequest>,
        GenericList<KeyValueResponse>,
        GenericRead<EventTypesResponse>,
        GenericPaginate<EventTypesResponse>,
        GenericDelete {}
