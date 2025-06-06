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
public class TourHotelRequest implements Serializable {
	private Long idHotel;
	private Integer totalDays;
}
