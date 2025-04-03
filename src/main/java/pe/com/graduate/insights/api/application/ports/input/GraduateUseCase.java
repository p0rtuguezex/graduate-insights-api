package pe.com.graduate.insights.api.application.ports.input;

import pe.com.graduate.insights.api.application.ports.generic.GenericCreate;
import pe.com.graduate.insights.api.application.ports.generic.GenericList;
import pe.com.graduate.insights.api.application.ports.generic.GenericPaginate;
import pe.com.graduate.insights.api.application.ports.generic.GenericRead;
import pe.com.graduate.insights.api.application.ports.generic.GenericUpdate;
import pe.com.graduate.insights.api.domain.models.request.GraduateRequest;
import pe.com.graduate.insights.api.domain.models.response.Graduate;

public interface GraduateUseCase
    extends GenericCreate<GraduateRequest>,
        GenericUpdate<GraduateRequest>,
        GenericList<Graduate>,
        GenericRead<Graduate>,
        GenericPaginate<Graduate> {}
