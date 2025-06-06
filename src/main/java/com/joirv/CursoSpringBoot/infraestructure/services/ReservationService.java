package com.joirv.CursoSpringBoot.infraestructure.services;

import com.joirv.CursoSpringBoot.api.models.request.ReservationRequestDto;
import com.joirv.CursoSpringBoot.api.models.responses.ApiResponseDto;
import com.joirv.CursoSpringBoot.api.models.responses.ReservationResponseDto;
import com.joirv.CursoSpringBoot.domain.entities.ReservationEntity;
import com.joirv.CursoSpringBoot.domain.mappers.ReservationMapper;
import com.joirv.CursoSpringBoot.domain.repositories.CustomerRepository;
import com.joirv.CursoSpringBoot.domain.repositories.HotelRepository;
import com.joirv.CursoSpringBoot.domain.repositories.ReservationRepository;
import com.joirv.CursoSpringBoot.infraestructure.abstract_services.IReservationService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.util.UUID;
@Transactional
@Service
@Slf4j
@AllArgsConstructor
public class ReservationService implements IReservationService {

	private final CustomerRepository customerRepository;
	private final ReservationRepository reservationRepository;
	private final HotelRepository hotelRepository;
	private final ReservationMapper reservationMapper;

	@Override
	public ApiResponseDto<ReservationResponseDto> create(ReservationRequestDto request) {
		var customerFindById = customerRepository.findById(request.getIdClient())
		        .orElseThrow(()-> new EntityNotFoundException("Customer not found"));
		var hotelFindById = hotelRepository.findById(request.getIdHotel())
		        .orElseThrow(()-> new EntityNotFoundException("Hotel not found"));

		var dateStart = java.time.LocalDate.now() ;
		var dateEnd =dateStart.plusDays(1);
		int totalDays = (int) ChronoUnit.DAYS.between(dateStart, dateEnd);
		var reservationEntity = ReservationEntity.builder()
		        .id(UUID.randomUUID())
		        .dateTimeReservation(java.time.LocalDateTime.now())
		        .dateStart(dateStart)
		        .dateEnd(dateEnd)
		        .totalDays(totalDays) // Assuming a 1-day reservation
		        .price(hotelFindById.getPrice().multiply(BigDecimal.valueOf(.25))) // Assuming a 25% discount
		        .hotel(hotelFindById)// Assuming a 1-day reservation
		        .customer(customerFindById)
		        .build();
		reservationRepository.save(reservationEntity);
		var reservationToDto = reservationMapper.toReservationResponseDto(reservationEntity);
		return ApiResponseDto.<ReservationResponseDto>builder()
		        .status("success")
		        .message("Reservation created successfully")
		        .data(reservationToDto)
		        .build();

	}

	@Override
	public ApiResponseDto<ReservationResponseDto> read(UUID uuid) {
		var reservationEntity = reservationRepository.findById(uuid)
				.orElseThrow(() -> new EntityNotFoundException("Reservation not found"));
		var reservationResponseDto = reservationMapper.toReservationResponseDto(reservationEntity);
		return ApiResponseDto.<ReservationResponseDto>builder()
				.status("success")
				.message("Reservation retrieved successfully")
				.data(reservationResponseDto)
				.build();
	}

	@Override
	public ApiResponseDto<ReservationResponseDto> update(UUID uuid, ReservationRequestDto request) {
		var reservationEntity = reservationRepository.findById(uuid)
				.orElseThrow(() -> new EntityNotFoundException("Reservation not found"));
		var customerFindById = customerRepository.findById(request.getIdClient())
				.orElseThrow(() -> new EntityNotFoundException("Customer not found"));
		var hotelFindById = hotelRepository.findById(request.getIdHotel())
				.orElseThrow(() -> new EntityNotFoundException("Hotel not found"));
		reservationEntity.setCustomer(customerFindById);
		reservationEntity.setHotel(hotelFindById);
		reservationRepository.save(reservationEntity);
		var reservationToDto = reservationMapper.toReservationResponseDto(reservationEntity);
		return ApiResponseDto.<ReservationResponseDto>builder()
				.status("success")
				.message("Reservation updated successfully")
				.data(reservationToDto)
				.build();
	}

	@Override
	public ApiResponseDto<Void> delete(UUID uuid) {
		var reservationEntity = reservationRepository.findById(uuid)
				.orElseThrow(() -> new EntityNotFoundException("Reservation not found"));
		reservationRepository.delete(reservationEntity);
		return ApiResponseDto.<Void>builder()
				.status("success")
				.message("Reservation deleted successfully")
				.data(null)
				.build();
	}
}
