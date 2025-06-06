package com.joirv.CursoSpringBoot.domain.mappers;

import com.joirv.CursoSpringBoot.api.models.responses.TourResponseDto;
import com.joirv.CursoSpringBoot.domain.entities.ReservationEntity;
import com.joirv.CursoSpringBoot.domain.entities.TicketEntity;
import com.joirv.CursoSpringBoot.domain.entities.TourEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.Collections;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {ReservationMapper.class, HotelMapper.class})
public interface TourMapper {

	@Mapping(target = "ticketsIds", source = "tickets", qualifiedByName = "toTicketIds")
	@Mapping(target = "reservationIds", source = "reservations", qualifiedByName = "toReservationIds")
	TourResponseDto toTourResponseDto(TourEntity tourEntity);

	@Named("toTicketIds")
	default Set<UUID> toTicketIds(Set<TicketEntity> tickets) {
		if (tickets == null) return Collections.emptySet();
		return tickets.stream()
		        .map(TicketEntity::getId)
		        .collect(Collectors.toSet());
	}

	@Named("toReservationIds")
	default Set<UUID> toReservationIds(Set<ReservationEntity> reservations) {
		if (reservations == null) return Collections.emptySet();
		return reservations.stream()
		        .map(ReservationEntity::getId)
		        .collect(Collectors.toSet());
	}
}
