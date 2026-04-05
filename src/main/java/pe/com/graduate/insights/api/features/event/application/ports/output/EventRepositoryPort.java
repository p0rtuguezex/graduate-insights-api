package pe.com.graduate.insights.api.features.event.application.ports.output;

import pe.com.graduate.insights.api.features.event.application.dto.EventRequest;
import pe.com.graduate.insights.api.features.event.application.dto.EventResponse;
import pe.com.graduate.insights.api.shared.models.response.KeyValueResponse;
import pe.com.graduate.insights.api.shared.ports.generic.GenericCreate;
import pe.com.graduate.insights.api.shared.ports.generic.GenericDelete;
import pe.com.graduate.insights.api.shared.ports.generic.GenericList;
import pe.com.graduate.insights.api.shared.ports.generic.GenericPaginate;
import pe.com.graduate.insights.api.shared.ports.generic.GenericRead;
import pe.com.graduate.insights.api.shared.ports.generic.GenericUpdate;

public interface EventRepositoryPort
    extends GenericCreate<EventRequest>,
        GenericUpdate<EventRequest>,
        GenericList<KeyValueResponse>,
        GenericRead<EventResponse>,
        GenericPaginate<EventResponse>,
        GenericDelete {}
