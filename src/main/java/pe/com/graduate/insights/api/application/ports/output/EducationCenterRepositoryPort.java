package pe.com.graduate.insights.api.application.ports.output;

import pe.com.graduate.insights.api.application.ports.generic.GenericCreate;
import pe.com.graduate.insights.api.application.ports.generic.GenericDelete;
import pe.com.graduate.insights.api.application.ports.generic.GenericList;
import pe.com.graduate.insights.api.application.ports.generic.GenericPaginate;
import pe.com.graduate.insights.api.application.ports.generic.GenericRead;
import pe.com.graduate.insights.api.application.ports.generic.GenericUpdate;
import pe.com.graduate.insights.api.domain.models.request.EducationCenterRequest;
import pe.com.graduate.insights.api.domain.models.response.EducationCenterResponse;

public interface EducationCenterRepositoryPort
    extends GenericCreate<EducationCenterRequest>,
        GenericUpdate<EducationCenterRequest>,
        GenericList<EducationCenterResponse>,
        GenericRead<EducationCenterResponse>,
        GenericPaginate<EducationCenterResponse>,
        GenericDelete {}
