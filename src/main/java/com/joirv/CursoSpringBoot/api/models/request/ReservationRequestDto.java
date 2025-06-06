package com.joirv.CursoSpringBoot.api.models.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@Data
@AllArgsConstructor
@Builder
public class ReservationRequestDto implements Serializable {

	private String idClient;
	private Long idHotel;

}
