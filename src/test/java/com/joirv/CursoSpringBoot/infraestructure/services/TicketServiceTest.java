package com.joirv.CursoSpringBoot.infraestructure.services;

import com.joirv.CursoSpringBoot.api.models.request.TicketRequestDto;
import com.joirv.CursoSpringBoot.api.models.responses.ApiResponseDto;
import com.joirv.CursoSpringBoot.api.models.responses.FlyResponseDto;
import com.joirv.CursoSpringBoot.api.models.responses.TicketResponseDto;
import com.joirv.CursoSpringBoot.domain.entities.CustomerEntity;
import com.joirv.CursoSpringBoot.domain.entities.FlyEntity;
import com.joirv.CursoSpringBoot.domain.entities.TicketEntity;
import com.joirv.CursoSpringBoot.domain.mappers.TicketMapper;
import com.joirv.CursoSpringBoot.domain.repositories.CustomerRepository;
import com.joirv.CursoSpringBoot.domain.repositories.FlyRepository;
import com.joirv.CursoSpringBoot.domain.repositories.TicketRepository;
import com.joirv.CursoSpringBoot.util.AeroLinea;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TicketServiceTest {

	@Mock
	private FlyRepository flyRepository;
	@Mock
	private TicketRepository ticketRepository;
	@Mock
	private  CustomerRepository customerRepository;
	@Mock
	private  TicketMapper ticketMapper;
	@InjectMocks
	private TicketService ticketService;

	@Test
	void testCreateTicketSuccess() {
		// Given - Datos y comportamiento esperados
		// se crean los objetos y variables que ocupamos retornen o agregar al methods
		Long flyId = 1L;
		String customerId =UUID.randomUUID().toString();

		//1. construir el request que se manda en el metodo
		TicketRequestDto request = TicketRequestDto.builder()
				.idClient(customerId)
				.idFly(flyId)
				.build();

		//2. construir el objeto que se espera que se devuelva en flyRepository
		FlyEntity fly = FlyEntity.builder()
		        .id(flyId)
		        .price(BigDecimal.valueOf(100))
		        .originName("Lima")
		        .destinyName("Cusco")
		        .originLat(-12.04318)
		        .originLng(-77.02824)
		        .destinyLat(-13.53195)
		        .destinyLng(-71.96746)
		        .aeroLine(AeroLinea.aero_gold)
		        .build();

		// 3. Simular cliente (como si viniera de la BD)
		CustomerEntity customer = CustomerEntity.builder()
		        .dni(customerId)
		        .fullName("Juan Pérez")
		        .creditCard("1234-5678-9012-3456")
		        .phoneNumber("999999999")
		        .totalFlights(0)
		        .totalLodgings(0)
		        .totalTours(0)
		        .build();

		// 4. Ticket simulado que se va a guardar
		TicketEntity savedTicket = TicketEntity.builder()
				.id(UUID.randomUUID())
				.fly(fly)
				.customer(customer)
				.price(fly.getPrice().multiply(BigDecimal.valueOf(0.25)))
		        		.purchaseDate(LocalDate.now())
		        		.arrivalDate(LocalDateTime.now())
		        		.departureDate(LocalDateTime.now())
				.build();
		// 5. DTO esperado que devuelve el mapper
		FlyResponseDto flyResponseDto = FlyResponseDto.builder()
		        .id(fly.getId())
		        .originName(fly.getOriginName())
		        .destinyName(fly.getDestinyName())
		        .originLat(fly.getOriginLat())
		        .originLng(fly.getOriginLng())
		        .destinyLat(fly.getDestinyLat())
		        .destinyLng(fly.getDestinyLng())
		        .price(fly.getPrice())
		        .aeroLine(fly.getAeroLine())
		        .build();

		TicketResponseDto responseDto = TicketResponseDto.builder()
		        .id(savedTicket.getId())
		        .price(savedTicket.getPrice())
		        .purchaseDate(savedTicket.getPurchaseDate())
		        .departureDate(savedTicket.getDepartureDate())
		        .arrivalDate(savedTicket.getArrivalDate())
		        .fly(flyResponseDto)
		        .build();

		// When - Llamada al método a probar
		when(flyRepository.findById(flyId)).thenReturn(java.util.Optional.of(fly));
		when(customerRepository.findById(customerId)).thenReturn(java.util.Optional.of(customer));
		when(ticketRepository.save(any(TicketEntity.class))).thenReturn(savedTicket);
		when(ticketMapper.toTicketResponseDto(any(TicketEntity.class))).thenReturn(responseDto);



		// Ejecutar
		ApiResponseDto<TicketResponseDto> result = ticketService.create(request);
		// Verificar
		assertNotNull(result);
		assertEquals("success", result.getStatus());
		assertEquals(200, result.getStatusCode());
		assertEquals("Ticket created successfully", result.getMessage());
		assertEquals(responseDto, result.getData());

		verify(ticketRepository).save(any(TicketEntity.class));
	}

	@Test
	void create_shouldThrowEntityNotFoundException_whenFlyNotFound() {
		// Dado un request válido (los IDs da igual porque mockeamos el repositorio)
		TicketRequestDto request = new TicketRequestDto();
		request.setIdFly(1L);
		request.setIdClient(UUID.randomUUID().toString());

		// Simulamos que el repositorio de fly NO encuentra el vuelo
		when(flyRepository.findById(request.getIdFly())).thenReturn(Optional.empty());

		// Cuando llamamos al método, esperamos que lance EntityNotFoundException con mensaje "Fly not found"
		EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
			ticketService.create(request);
		});
		assertEquals("Fly not found", exception.getMessage());
	}

	@Test
	void create_shouldThrowEntityNotFoundException_whenCustomerNotFound() {
		// Dado un request válido (los IDs da igual porque mockeamos el repositorio)
		TicketRequestDto request = new TicketRequestDto();
		request.setIdFly(1L);
		request.setIdClient(UUID.randomUUID().toString());

		// Simulamos que el repositorio de cliente NO encuentra el cliente
		when(flyRepository.findById(request.getIdFly())).thenReturn(Optional.of(new FlyEntity()));
		when(customerRepository.findById(request.getIdClient())).thenReturn(Optional.empty());
		// Cuando llamamos al método, esperamos que lance EntityNotFoundException con mensaje "Customer not found"
		EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
			ticketService.create(request);
		});
		assertEquals("Customer not found", exception.getMessage());
	}


	@Test
	void testReadTicketSuccess() {
		// Given - Datos y comportamiento esperados
		UUID uuid = UUID.randomUUID();
		TicketEntity ticket = TicketEntity.builder()
				.id(uuid)
				.departureDate(LocalDateTime.now())
				.arrivalDate(LocalDateTime.now())
				.purchaseDate(LocalDate.now())
				.price(BigDecimal.valueOf(100))
				.build();

		when(ticketRepository.findById(uuid)).
		        				thenReturn(Optional.of(ticket));
		when(ticketMapper.toTicketResponseDto(ticket)).thenReturn(TicketResponseDto.builder()
		        				.id(ticket.getId())
				.departureDate(ticket.getDepartureDate())
				.arrivalDate(ticket.getArrivalDate())
				.purchaseDate(ticket.getPurchaseDate())
				.price(ticket.getPrice())
				.build());
		// When - Llamada al método a probar
		ApiResponseDto<TicketResponseDto> result = ticketService.read(uuid);
		// Verificar
		assertNotNull(result);
		assertEquals("success", result.getStatus());
		assertEquals(200, result.getStatusCode());
		assertEquals("Ticket found successfully", result.getMessage());
		assertEquals(ticket.getId(), result.getData().getId());
		assertEquals(ticket.getPrice(), result.getData().getPrice());
		assertEquals(ticket.getDepartureDate(), result.getData().getDepartureDate());
		assertEquals(ticket.getArrivalDate(), result.getData().getArrivalDate());
		assertEquals(ticket.getPurchaseDate(), result.getData().getPurchaseDate());
		verify(ticketRepository).findById(uuid);
		verify(ticketMapper).toTicketResponseDto(ticket);

		}

	// 1.debemos crear un ticket para poder actualizarlo
	// 2. debemos crear un, id para pasarlo en el request
	// 3. debemos de buscar el ticket en la base de datos con el id
	// 4. debemos de crear la respuesta que queremos que regrese al buscar el ticket
	// 5. debemos de crear el ticket que queremos actualizar
	// 6. debemos de crear  la respuesta dto que queremos que regrese al actualizar el ticket
	@Test
	void testUpdateTicketSuccess() {
			// Given - Datos y comportamiento esperados
			UUID uuid= UUID.randomUUID();
			String idClient = UUID.randomUUID().toString();
			Long idFly = 2L;
			TicketRequestDto request = TicketRequestDto.builder()
			        .idClient(idClient)
			        .idFly(idFly)
			        .build();
			// 1. Simular el vuelo que se va a actualizar
			FlyEntity fly = FlyEntity.builder()
			        .id(1L)
			        .price(BigDecimal.valueOf(100))
			        .originName("Lima")
			        .destinyName("Cusco")
			        .originLat(-12.04318)
			        .originLng(-77.02824)
			        .destinyLat(-13.53195)
			        .destinyLng(-71.96746)
			        .aeroLine(AeroLinea.aero_gold)
			        .build();
			// 2. Simular el ticket que se va a actualizar
			TicketEntity ticket= TicketEntity.builder()
			        .id(uuid)
			        .departureDate(LocalDateTime.now())
			        .arrivalDate(LocalDateTime.now())
			        .purchaseDate(LocalDate.now())
			        .price(BigDecimal.valueOf(100))
			        .fly(fly)
			        .build();

			FlyEntity flyUpdate = FlyEntity.builder()
			        .id(2L)
			        .price(BigDecimal.valueOf(150))
			        .originName("Grecia")
			        .destinyName("Mexico")
			        .originLat(-12.04318)
			        .originLng(-77.02824)
			        .destinyLat(-13.53195)
			        .destinyLng(-71.96746)
			        .aeroLine(AeroLinea.aero_gold)
			        .build();

			CustomerEntity customer = CustomerEntity.builder()
			        .dni(idClient)
			        .fullName("Juan Pérez")
			        .creditCard("1234-5678-9012-3456")
			        .phoneNumber("999999999")
			        .totalFlights(0)
			        .totalLodgings(0)
			        .totalTours(0)
			        .build();
		TicketEntity ticketUpdate= TicketEntity.builder()
		        .id(uuid)
		        .departureDate(LocalDateTime.now())
		        .arrivalDate(LocalDateTime.now())
		        .purchaseDate(LocalDate.now())
		        .price(BigDecimal.valueOf(100))
		        .fly(flyUpdate)
		        .build();
			TicketResponseDto ticketDto = TicketResponseDto.builder()
			        .id(ticket.getId())
			        .departureDate(ticket.getDepartureDate())
			        .arrivalDate(ticket.getArrivalDate())
			        .purchaseDate(ticket.getPurchaseDate())
			        .price(flyUpdate.getPrice().multiply(BigDecimal.valueOf(0.25)))
			        .fly(FlyResponseDto.builder()
			                .id(flyUpdate.getId())
			                .originName(flyUpdate.getOriginName())
			                .destinyName(flyUpdate.getDestinyName())
			                .originLat(flyUpdate.getOriginLat())
			                .originLng(flyUpdate.getOriginLng())
			                .destinyLat(flyUpdate.getDestinyLat())
			                .destinyLng(flyUpdate.getDestinyLng())
			                .price(flyUpdate.getPrice())
			                .aeroLine(flyUpdate.getAeroLine())
			                .build())
			        .build();

			ApiResponseDto <TicketResponseDto> response = ApiResponseDto.<TicketResponseDto>builder()
			        .status("success")
			        .message("Ticket updated successfully")
			        .statusCode(200)
			        .data(ticketDto)
			        .meta(null)
			        .build();

			// When - Llamada al método a probar
			when(ticketRepository.findById(uuid)).thenReturn(java.util.Optional.of(ticket));
			when(flyRepository.findById(idFly)).thenReturn(java.util.Optional.of(flyUpdate));
			when(customerRepository.findById(idClient)).thenReturn(java.util.Optional.of(customer));
			when(ticketRepository.save(any(TicketEntity.class))).thenReturn(ticketUpdate);
			when(ticketMapper.toTicketResponseDto(any(TicketEntity.class))).thenReturn(ticketDto);

			ApiResponseDto<TicketResponseDto> result = ticketService.update(uuid, request);

			// Verificar
			assertNotNull(result);
			assertEquals("success", result.getStatus());
			assertEquals(200, result.getStatusCode());
			assertEquals("Ticket updated successfully", result.getMessage());
			assertEquals(ticketDto, result.getData());
			assertEquals(response, result);
			verify(flyRepository).findById(idFly);
			verify(customerRepository).findById(idClient);

		}


		@Test
		void testDeleteTicketSuccess() {
			// Given - Datos y comportamiento esperados
			UUID uuid = UUID.randomUUID();

			TicketEntity ticket = TicketEntity.builder()
			        .id(uuid)
			        .departureDate(LocalDateTime.now())
			        .arrivalDate(LocalDateTime.now())
			        .purchaseDate(LocalDate.now())
			        .price(BigDecimal.valueOf(100))
			        .build();


			ApiResponseDto<Void> response = ApiResponseDto.<Void>builder()
			        .status("success")
			        .message("Ticket deleted successfully")
			        .statusCode(200)
			        .data(null)
			        .meta(null)
			        .build();

			// When - Llamada al método a probar
			when(ticketRepository.findById(uuid)).thenReturn(java.util.Optional.of(ticket));
			ApiResponseDto<Void> result = ticketService.delete(uuid);
			// Verificar
			verify(ticketRepository).findById(uuid);
			verify(ticketRepository).delete(ticket);
			assertNotNull(result);
			assertEquals("success", result.getStatus());
			assertEquals(200, result.getStatusCode());
			assertEquals("Ticket deleted successfully", result.getMessage());
			assertNull(result.getData());
			assertEquals(response, result);
			// Aquí no hay data porque es un delete, así que debería ser null
			assertNull(result.getMeta());
			// Verificar que el método delete fue llamado
			verify(ticketRepository).delete(ticket);


		}

		@Test
		void testFlyByPrice() {
			// Given - Datos y comportamiento esperados
			Long idFly = 1L;
			FlyEntity fly = FlyEntity.builder()
					.id(idFly)
					.price(BigDecimal.valueOf(100))
					.build();
			var extraPrice =fly.getPrice().multiply(BigDecimal.valueOf(0.25));
			var finalPrice = fly.getPrice().add(extraPrice);
			ApiResponseDto<BigDecimal> response = ApiResponseDto.<BigDecimal>builder()
			        .status("success")
			        .message("Fly found successfully")
			        .statusCode(200)
			        .data(finalPrice)
			        .meta(null)
			        .build();

			when(flyRepository.findById(idFly)).thenReturn(java.util.Optional.of(fly));

			// When - Llamada al método a probar
			ApiResponseDto<BigDecimal> result = ticketService.flyByPrice(idFly);

			// Verificar
			assertEquals(response, result);
			assertNotNull(result);
			assertEquals("success", result.getStatus());
			assertEquals(200, result.getStatusCode());
			assertEquals("Fly found successfully", result.getMessage());
			verify(flyRepository).findById(idFly);
		}

}