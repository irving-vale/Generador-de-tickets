package com.joirv.CursoSpringBoot.infraestructure.abstract_services;

import com.joirv.CursoSpringBoot.api.models.responses.ApiResponseDto;
import com.joirv.CursoSpringBoot.api.models.responses.FlyResponseDto;

import java.math.BigDecimal;
import java.util.Set;

public interface IFlyService extends  CatalogoService<FlyResponseDto> {

	ApiResponseDto<Set<FlyResponseDto>> readByOriginDestiny(String origin, String destiny);
}
