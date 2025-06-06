package com.joirv.CursoSpringBoot.api.models.responses;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.joirv.CursoSpringBoot.domain.entities.HotelEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@NoArgsConstructor
@Data
@AllArgsConstructor
@Builder
public class ReservationResponseDto  implements Serializable {
	private UUID id;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss")
	private LocalDateTime dateTimeReservation;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy ")
	private LocalDate dateStart;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy ")
	private LocalDate dateEnd;
	private Integer totalDays;
	private BigDecimal price;
	private HotelResponseDto hotel;
}
