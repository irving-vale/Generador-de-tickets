package com.joirv.CursoSpringBoot.domain.mappers;

import com.joirv.CursoSpringBoot.api.models.responses.TicketResponseDto;
import com.joirv.CursoSpringBoot.domain.entities.TicketEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {FlyMapper.class})
public interface TicketMapper {
	TicketResponseDto toTicketResponseDto(TicketEntity ticket);
	TicketEntity toTicketEntity(TicketResponseDto ticketResponseDto);
}
