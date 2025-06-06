package com.joirv.CursoSpringBoot.domain.mappers;

import com.joirv.CursoSpringBoot.api.models.responses.HotelResponseDto;
import com.joirv.CursoSpringBoot.domain.entities.HotelEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface HotelMapper {

	HotelResponseDto toDto(HotelEntity hotel);
}
