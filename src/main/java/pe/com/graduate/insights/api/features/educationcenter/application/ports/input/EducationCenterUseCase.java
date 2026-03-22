package pe.com.graduate.insights.api.features.educationcenter.application.ports.input;

import pe.com.graduate.insights.api.shared.ports.generic.GenericCreate;
import pe.com.graduate.insights.api.shared.ports.generic.GenericDelete;
import pe.com.graduate.insights.api.shared.ports.generic.GenericList;
import pe.com.graduate.insights.api.shared.ports.generic.GenericPaginate;
import pe.com.graduate.insights.api.shared.ports.generic.GenericRead;
import pe.com.graduate.insights.api.shared.ports.generic.GenericUpdate;
import pe.com.graduate.insights.api.features.educationcenter.application.dto.EducationCenterRequest;
import pe.com.graduate.insights.api.features.educationcenter.application.dto.EducationCenterResponse;

public interface EducationCenterUseCase
    extends GenericCreate<EducationCenterRequest>,
        GenericUpdate<EducationCenterRequest>,
        GenericList<EducationCenterResponse>,
        GenericRead<EducationCenterResponse>,
        GenericPaginate<EducationCenterResponse>,
        GenericDelete {}
