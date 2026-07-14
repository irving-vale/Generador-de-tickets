package com.joirv.CursoSpringBoot.infraestructure.abstract_services;

import com.joirv.CursoSpringBoot.api.models.request.ReservationRequestDto;
import com.joirv.CursoSpringBoot.api.models.responses.ReservationResponseDto;

import java.util.UUID;

public interface IReservationService extends CrudService<ReservationRequestDto, ReservationResponseDto, UUID> {


}
