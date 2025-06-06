package com.joirv.CursoSpringBoot.domain.mappers;

import com.joirv.CursoSpringBoot.api.models.responses.FlyResponseDto;
import com.joirv.CursoSpringBoot.domain.entities.FlyEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FlyMapper {
	FlyResponseDto toDto(FlyEntity fly);
	FlyEntity toEntity(FlyResponseDto flyResponseDto);
}
