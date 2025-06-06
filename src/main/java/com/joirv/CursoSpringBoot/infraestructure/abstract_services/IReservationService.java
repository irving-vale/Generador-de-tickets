package com.joirv.CursoSpringBoot.infraestructure.abstract_services;

import com.joirv.CursoSpringBoot.api.models.request.ReservationRequestDto;
import com.joirv.CursoSpringBoot.api.models.request.TicketRequestDto;
import com.joirv.CursoSpringBoot.api.models.responses.ApiResponseDto;
import com.joirv.CursoSpringBoot.api.models.responses.ReservationResponseDto;
import com.joirv.CursoSpringBoot.api.models.responses.TicketResponseDto;

import java.math.BigDecimal;
import java.util.UUID;

public interface IReservationService extends CrudService<ReservationRequestDto, ReservationResponseDto, UUID> {


}
