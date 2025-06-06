package com.joirv.CursoSpringBoot.infraestructure.abstract_services;

import com.joirv.CursoSpringBoot.api.models.request.TourFlyRequestDto;
import com.joirv.CursoSpringBoot.api.models.request.TourHotelRequest;
import com.joirv.CursoSpringBoot.api.models.request.TourRequestDto;
import com.joirv.CursoSpringBoot.api.models.responses.ApiResponseDto;
import com.joirv.CursoSpringBoot.api.models.responses.TourResponseDto;

import java.util.UUID;

public interface ITourService extends CrudService<TourRequestDto, TourResponseDto, Long> {



    ApiResponseDto<TourResponseDto> addTicket(Long flyId, Long tourId);

    ApiResponseDto<TourResponseDto> removeTicket(UUID ticketId, Long tourId);

    ApiResponseDto<TourResponseDto> addReservation(UUID reservationId,Long tourId);

    ApiResponseDto<TourResponseDto> removeReservation(UUID reservationId,Long tourId);
}
