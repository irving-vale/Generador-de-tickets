package com.joirv.CursoSpringBoot.api.models.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@NoArgsConstructor
@Data
@AllArgsConstructor
@Builder
public class HotelResponseDto implements Serializable {
	private Long id;
	private String name;
	private String address;
	private Integer rating;
	private BigDecimal price;
}
