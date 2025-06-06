package com.joirv.CursoSpringBoot.domain.mappers;

import com.joirv.CursoSpringBoot.api.models.responses.ReservationResponseDto;
import com.joirv.CursoSpringBoot.domain.entities.ReservationEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring" ,uses = {HotelMapper.class})
public interface ReservationMapper {
	ReservationResponseDto toReservationResponseDto(ReservationEntity reservation);
	ReservationEntity toReservationEntity(ReservationResponseDto reservationResponseDto);
}
