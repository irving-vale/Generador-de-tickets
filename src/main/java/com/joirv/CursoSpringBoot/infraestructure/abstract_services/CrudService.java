package com.joirv.CursoSpringBoot.infraestructure.abstract_services;

import com.joirv.CursoSpringBoot.api.models.responses.ApiResponseDto;

public interface CrudService  <RQ, RS, ID> {

    ApiResponseDto<RS>   create(RQ request);

    ApiResponseDto<RS> read(ID id);

    ApiResponseDto<RS> update(ID id, RQ request);

    ApiResponseDto<Void>delete(ID id);


}
