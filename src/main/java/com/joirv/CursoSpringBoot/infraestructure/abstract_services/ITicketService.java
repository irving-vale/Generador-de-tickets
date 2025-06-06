package com.joirv.CursoSpringBoot.infraestructure.abstract_services;

import com.joirv.CursoSpringBoot.api.models.request.TicketRequestDto;
import com.joirv.CursoSpringBoot.api.models.responses.ApiResponseDto;
import com.joirv.CursoSpringBoot.api.models.responses.FlyResponseDto;
import com.joirv.CursoSpringBoot.api.models.responses.TicketResponseDto;

import java.math.BigDecimal;
import java.util.UUID;

public interface ITicketService  extends CrudService<TicketRequestDto, TicketResponseDto, UUID> {

	ApiResponseDto <BigDecimal> flyByPrice(Long idFly);
}
