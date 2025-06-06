package com.joirv.CursoSpringBoot.api.controllers;

import com.joirv.CursoSpringBoot.api.models.responses.ApiResponseDto;
import com.joirv.CursoSpringBoot.api.models.responses.FlyResponseDto;
import com.joirv.CursoSpringBoot.infraestructure.services.FlyService;
import com.joirv.CursoSpringBoot.util.SortType;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/fly")
@AllArgsConstructor
public class FlyController {

	private final FlyService flyService;

	@GetMapping("/findAll")
	public ResponseEntity<ApiResponseDto<List<FlyResponseDto>>> findAllPagination(@RequestParam Integer page, @RequestParam Integer size, @RequestParam(required = false) SortType sortType) {
		if (sortType == null) {
			sortType = SortType.NONE; // Default sort type if not provided
		}
		return ResponseEntity.ok(flyService.findAllPagination(page, size, sortType));
	}

	@GetMapping("/readLessPrice")
	public ResponseEntity<ApiResponseDto<List<FlyResponseDto>>> readLessPrice(@RequestParam Integer page, @RequestParam Integer size, @RequestParam(required = false) SortType sortType, @RequestParam BigDecimal price) {
		if (sortType == null) {
			sortType = SortType.NONE; // Default sort type if not provided
		}
		return ResponseEntity.ok(flyService.readLessPrice(page, size, sortType, price));
	}

	@GetMapping("/readBetweenPrice")
	public ResponseEntity<ApiResponseDto<List<FlyResponseDto>>> readBetweenPrice(@RequestParam BigDecimal min,@RequestParam BigDecimal max) {
		return ResponseEntity.ok(flyService.readBetweenPrice(min, max));
	}

	@GetMapping("/readByOriginDestiny")
	public ResponseEntity<ApiResponseDto<Set<FlyResponseDto>>> readByOriginDestiny(@RequestParam String origin,@RequestParam String destiny) {
		return ResponseEntity.ok(flyService.readByOriginDestiny(origin, destiny));
	}
}
