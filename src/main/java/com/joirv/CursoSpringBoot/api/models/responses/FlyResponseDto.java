package com.joirv.CursoSpringBoot.api.models.responses;

import com.joirv.CursoSpringBoot.util.AeroLinea;

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
public class FlyResponseDto  implements Serializable {

	private Long id;
	private Double originLat;
	private Double originLng;
	private Double destinyLat;
	private Double destinyLng;
	private BigDecimal price;
	private String originName;
	private String destinyName;
	private AeroLinea aeroLine;
}
