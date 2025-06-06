package com.joirv.CursoSpringBoot.api.controllers;

import com.joirv.CursoSpringBoot.api.models.request.ReservationRequestDto;
import com.joirv.CursoSpringBoot.api.models.responses.ApiResponseDto;
import com.joirv.CursoSpringBoot.api.models.responses.ReservationResponseDto;
import com.joirv.CursoSpringBoot.infraestructure.services.ReservationService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/reservations")
@AllArgsConstructor
public class ReservationController {

	private final ReservationService reservationService;

	@PostMapping("/create")
	public ResponseEntity<ApiResponseDto<ReservationResponseDto>> create(@RequestBody ReservationRequestDto request) {
		return ResponseEntity.ok(reservationService.create(request));
	}

	@GetMapping("/read")
	public ResponseEntity<ApiResponseDto<ReservationResponseDto>> read(@RequestParam UUID uuid) {
		return ResponseEntity.ok(reservationService.read(uuid));
	}

	@PutMapping ("/update/{uuid}")
	public ResponseEntity<ApiResponseDto<ReservationResponseDto>> update(@PathVariable UUID uuid,@RequestBody ReservationRequestDto request) {
		return ResponseEntity.ok(reservationService.update(uuid,request));
	}

	@DeleteMapping("/delete/{uuid}")
	public ResponseEntity<ApiResponseDto<Void>> delete(@PathVariable UUID uuid) {
		return ResponseEntity.ok(reservationService.delete(uuid));
	}
}
