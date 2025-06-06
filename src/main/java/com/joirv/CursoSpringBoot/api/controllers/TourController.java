package com.joirv.CursoSpringBoot.api.controllers;

import com.joirv.CursoSpringBoot.api.models.request.TourRequestDto;
import com.joirv.CursoSpringBoot.api.models.responses.ApiResponseDto;
import com.joirv.CursoSpringBoot.api.models.responses.TourResponseDto;
import com.joirv.CursoSpringBoot.infraestructure.services.TourService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/tours")
@AllArgsConstructor
public class TourController {


	private TourService tourService;


	@PostMapping("/create")
	public ResponseEntity<ApiResponseDto<TourResponseDto>> createTour(@RequestBody TourRequestDto request){
		System.out.println("Tour created: " + tourService);
		return ResponseEntity.ok(tourService.create(request));
	}

	@GetMapping("/{id}")
	public ResponseEntity<ApiResponseDto<TourResponseDto>> getTour(@PathVariable Long id){
		return ResponseEntity.ok(tourService.read(id));
	}

	@PutMapping("/{id}")
	public ResponseEntity<ApiResponseDto<TourResponseDto>> updateTour(@PathVariable Long id, @RequestBody TourRequestDto request){
		return ResponseEntity.ok(tourService.update(id, request));
	}

	@PutMapping("/add-ticket")
	public ResponseEntity<ApiResponseDto<TourResponseDto>> addTicket(@RequestParam Long flyId, @RequestParam  Long tourId){
		return ResponseEntity.ok(tourService.addTicket(flyId, tourId));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<ApiResponseDto<Void>> deleteTour(@PathVariable Long id){
		return ResponseEntity.ok(tourService.delete(id));
	}

	@DeleteMapping("/remove-ticket")
	public ResponseEntity<ApiResponseDto<TourResponseDto>> removeTicket(@RequestParam UUID ticketId,@RequestParam Long tourId){
		return ResponseEntity.ok(tourService.removeTicket(ticketId, tourId));
	}

	@PutMapping("/add-reservation")
	public ResponseEntity<ApiResponseDto<TourResponseDto>> addReservation(@RequestParam UUID reservationId, @RequestParam Long tourId){
		return ResponseEntity.ok(tourService.addReservation(reservationId, tourId));
	}

	@DeleteMapping("/remove-reservation")
	public ResponseEntity<ApiResponseDto<TourResponseDto>> removeReservation(@RequestParam UUID reservationId, @RequestParam Long tourId){
		return ResponseEntity.ok(tourService.removeReservation(reservationId, tourId));
	}

}
