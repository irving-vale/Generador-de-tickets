package com.joirv.CursoSpringBoot.infraestructure.services;

import com.joirv.CursoSpringBoot.api.models.request.TourFlyRequestDto;
import com.joirv.CursoSpringBoot.api.models.request.TourHotelRequest;
import com.joirv.CursoSpringBoot.api.models.request.TourRequestDto;
import com.joirv.CursoSpringBoot.api.models.responses.ApiResponseDto;
import com.joirv.CursoSpringBoot.api.models.responses.TourResponseDto;
import com.joirv.CursoSpringBoot.domain.entities.CustomerEntity;
import com.joirv.CursoSpringBoot.domain.entities.FlyEntity;
import com.joirv.CursoSpringBoot.domain.entities.HotelEntity;
import com.joirv.CursoSpringBoot.domain.entities.ReservationEntity;
import com.joirv.CursoSpringBoot.domain.entities.TicketEntity;
import com.joirv.CursoSpringBoot.domain.entities.TourEntity;
import com.joirv.CursoSpringBoot.domain.mappers.TourMapper;
import com.joirv.CursoSpringBoot.domain.repositories.*;
import com.joirv.CursoSpringBoot.infraestructure.helper.TourHelper;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class TourServiceTest {

    @Mock
    private TourRepository tourRepository;

    @Mock
    private FlyRepository flyRepository;

    @Mock
    private HotelRepository hotelRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private TourHelper tourHelper;

    @Mock
    private TourMapper tourMapper;

    @Mock
    private ReservationRepository reservationRepository;

    @InjectMocks
    private TourService tourService;

    @Test
    void testCreateTourSuccess() {
        // Given - Setup test data
        String customerId = UUID.randomUUID().toString();
        Long flyId = 1L;
        Long hotelId = 1L;
        Integer totalDays = 5;

        // Create request DTOs
        TourFlyRequestDto flyRequestDto = TourFlyRequestDto.builder()
                .idFly(flyId)
                .build();

        TourHotelRequest hotelRequestDto = TourHotelRequest.builder()
                .idHotel(hotelId)
                .totalDays(totalDays)
                .build();

        Set<TourFlyRequestDto> flyRequests = new HashSet<>();
        flyRequests.add(flyRequestDto);

        Set<TourHotelRequest> hotelRequests = new HashSet<>();
        hotelRequests.add(hotelRequestDto);

        TourRequestDto request = TourRequestDto.builder()
                .idClient(customerId)
                .idFlys(flyRequests)
                .idHotels(hotelRequests)
                .build();

        // Create entities
        CustomerEntity customer = CustomerEntity.builder()
                .dni(customerId)
                .fullName("John Doe")
                .creditCard("1234-5678-9012-3456")
                .phoneNumber("123456789")
                .totalFlights(0)
                .totalLodgings(0)
                .totalTours(0)
                .build();

        FlyEntity fly = FlyEntity.builder()
                .id(flyId)
                .price(BigDecimal.valueOf(100))
                .originName("Madrid")
                .destinyName("Barcelona")
                .originLat(40.4168)
                .originLng(-3.7038)
                .destinyLat(41.3851)
                .destinyLng(2.1734)
                .build();

        HotelEntity hotel = HotelEntity.builder()
                .id(hotelId)
                .name("Test Hotel")
                .address("Test Address")
                .rating(4)
                .price(BigDecimal.valueOf(200))
                .build();

        // Create tickets and reservations
        Set<TicketEntity> tickets = new HashSet<>();
        TicketEntity ticket = TicketEntity.builder()
                .id(UUID.randomUUID())
                .fly(fly)
                .customer(customer)
                .price(fly.getPrice().multiply(BigDecimal.valueOf(1.25)))
                .purchaseDate(LocalDate.now())
                .arrivalDate(LocalDateTime.now())
                .departureDate(LocalDateTime.now())
                .build();
        tickets.add(ticket);

        Set<ReservationEntity> reservations = new HashSet<>();
        ReservationEntity reservation = ReservationEntity.builder()
                .id(UUID.randomUUID())
                .hotel(hotel)
                .customer(customer)
                .dateTimeReservation(LocalDateTime.now())
                .dateStart(LocalDate.now())
                .dateEnd(LocalDate.now().plusDays(totalDays))
                .totalDays(totalDays)
                .price(hotel.getPrice().multiply(BigDecimal.valueOf(1.25)))
                .build();
        reservations.add(reservation);

        // Create a tour entity
        TourEntity tourEntity = TourEntity.builder()
                .id(1L)
                .customer(customer)
                .tickets(tickets)
                .reservations(reservations)
                .build();

        // Create response DTO
        TourResponseDto tourResponseDto = TourResponseDto.builder()
                .id(tourEntity.getId())
                .ticketsIds(Set.of(ticket.getId()))
                .reservationIds(Set.of(reservation.getId()))
                .build();

        // When - Mock repository and mapper behavior
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        when(flyRepository.findById(flyId)).thenReturn(Optional.of(fly));
        when(hotelRepository.findById(hotelId)).thenReturn(Optional.of(hotel));
        when(tourHelper.createTickets(any(), any())).thenReturn(tickets);
        when(tourHelper.createReservations(any(), any())).thenReturn(reservations);
        when(tourRepository.save(any(TourEntity.class))).thenReturn(tourEntity);
        when(tourMapper.toTourResponseDto(tourEntity)).thenReturn(tourResponseDto);

        // Execute
        ApiResponseDto<TourResponseDto> result = tourService.create(request);

        // Then - Verify results
        assertNotNull(result);
        assertEquals("success", result.getStatus());
        assertEquals("Tour created successfully", result.getMessage());
        assertEquals(tourResponseDto, result.getData());

        // Verify interactions
        verify(customerRepository).findById(customerId);
        verify(flyRepository).findById(flyId);
        verify(hotelRepository).findById(hotelId);
        verify(tourHelper).createTickets(any(), any());
        verify(tourHelper).createReservations(any(), any());
        verify(tourRepository).save(any(TourEntity.class));
        verify(tourMapper).toTourResponseDto(tourEntity);
    }

    @Test
    void create_shouldThrowEntityNotFoundException_whenCustomerNotFound() {
        // Given
        TourRequestDto request = TourRequestDto.builder()
                .idClient(UUID.randomUUID().toString())
                .idFlys(new HashSet<>())
                .idHotels(new HashSet<>())
                .build();

        // When
        when(customerRepository.findById(request.getIdClient())).thenReturn(Optional.empty());

        // Then
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            tourService.create(request);
        });
        assertEquals("Customer not found", exception.getMessage());
    }

    @Test
    void create_shouldThrowEntityNotFoundException_whenFlyNotFound() {
        // Given
        String customerId = UUID.randomUUID().toString();
        Long flyId = 1L;

        TourFlyRequestDto flyRequestDto = TourFlyRequestDto.builder()
                .idFly(flyId)
                .build();

        Set<TourFlyRequestDto> flyRequests = new HashSet<>();
        flyRequests.add(flyRequestDto);

        TourRequestDto request = TourRequestDto.builder()
                .idClient(customerId)
                .idFlys(flyRequests)
                .idHotels(new HashSet<>())
                .build();

        CustomerEntity customer = CustomerEntity.builder()
                .dni(customerId)
                .build();

        // When
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        when(flyRepository.findById(flyId)).thenReturn(Optional.empty());

        // Then
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            tourService.create(request);
        });
        assertEquals("Fly not found", exception.getMessage());
    }

    @Test
    void create_shouldThrowEntityNotFoundException_whenHotelNotFound() {
        // Given
        String customerId = UUID.randomUUID().toString();
        Long hotelId = 1L;

        TourHotelRequest hotelRequestDto = TourHotelRequest.builder()
                .idHotel(hotelId)
                .totalDays(5)
                .build();

        Set<TourHotelRequest> hotelRequests = new HashSet<>();
        hotelRequests.add(hotelRequestDto);

        TourRequestDto request = TourRequestDto.builder()
                .idClient(customerId)
                .idFlys(new HashSet<>())
                .idHotels(hotelRequests)
                .build();

        CustomerEntity customer = CustomerEntity.builder()
                .dni(customerId)
                .build();

        // When
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        when(hotelRepository.findById(hotelId)).thenReturn(Optional.empty());

        // Then
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            tourService.create(request);
        });
        assertEquals("Hotel not Found", exception.getMessage());
    }

    @Test
    void testDeleteTourSuccess() {
        // Given
        Long tourId = 1L;

        TourEntity tourEntity = TourEntity.builder()
                .id(tourId)
                .customer(CustomerEntity.builder().dni(UUID.randomUUID().toString()).build())
                .tickets(new HashSet<>())
                .reservations(new HashSet<>())
                .build();

        // When
        when(tourRepository.findById(tourId)).thenReturn(Optional.of(tourEntity));

        // Execute
        ApiResponseDto<Void> result = tourService.delete(tourId);

        // Then
        assertNotNull(result);
        assertEquals("success", result.getStatus());
        assertEquals("Tour deleted successfully", result.getMessage());

        // Verify interactions
        verify(tourRepository).findById(tourId);
        verify(tourRepository).delete(tourEntity);
    }

    @Test
    void delete_shouldThrowEntityNotFoundException_whenTourNotFound() {
        // Given
        Long nonExistentId = 999L;

        // When
        when(tourRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        // Then
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            tourService.delete(nonExistentId);
        });
        assertEquals("Tour not found", exception.getMessage());
    }

    @Test
    void testAddTicketSuccess() {
        // Given
        Long tourId = 1L;
        Long flyId = 2L;

        // Create customer
        CustomerEntity customer = CustomerEntity.builder()
                .dni(UUID.randomUUID().toString())
                .fullName("John Doe")
                .creditCard("1234-5678-9012-3456")
                .phoneNumber("123456789")
                .totalFlights(0)
                .totalLodgings(0)
                .totalTours(0)
                .build();

        // Create fly
        FlyEntity fly = FlyEntity.builder()
                .id(flyId)
                .price(BigDecimal.valueOf(100))
                .originName("Madrid")
                .destinyName("Barcelona")
                .originLat(40.4168)
                .originLng(-3.7038)
                .destinyLat(41.3851)
                .destinyLng(2.1734)
                .build();

        // Create existing tickets
        Set<TicketEntity> existingTickets = new HashSet<>();
        TicketEntity existingTicket = TicketEntity.builder()
                .id(UUID.randomUUID())
                .fly(FlyEntity.builder().id(3L).build())
                .customer(customer)
                .price(BigDecimal.valueOf(125))
                .purchaseDate(LocalDate.now())
                .arrivalDate(LocalDateTime.now())
                .departureDate(LocalDateTime.now())
                .build();
        existingTickets.add(existingTicket);

        // Create tour
        TourEntity tourEntity = TourEntity.builder()
                .id(tourId)
                .customer(customer)
                .tickets(existingTickets)
                .reservations(new HashSet<>())
                .build();

        // Create new ticket
        Set<TicketEntity> newTickets = new HashSet<>();
        TicketEntity newTicket = TicketEntity.builder()
                .id(UUID.randomUUID())
                .fly(fly)
                .customer(customer)
                .price(fly.getPrice().multiply(BigDecimal.valueOf(1.25)))
                .purchaseDate(LocalDate.now())
                .arrivalDate(LocalDateTime.now())
                .departureDate(LocalDateTime.now())
                .build();
        newTickets.add(newTicket);

        // Create expected updated tour
        Set<TicketEntity> allTickets = new HashSet<>(existingTickets);
        allTickets.addAll(newTickets);
        TourEntity updatedTourEntity = TourEntity.builder()
                .id(tourId)
                .customer(customer)
                .tickets(allTickets)
                .reservations(new HashSet<>())
                .build();

        // Create response DTO
        TourResponseDto tourResponseDto = TourResponseDto.builder()
                .id(tourId)
                .ticketsIds(allTickets.stream().map(TicketEntity::getId).collect(Collectors.toSet()))
                .reservationIds(new HashSet<>())
                .build();

        // When
        when(tourRepository.findById(tourId)).thenReturn(Optional.of(tourEntity));
        when(flyRepository.findById(flyId)).thenReturn(Optional.of(fly));
        when(tourHelper.createTickets(any(), any())).thenReturn(newTickets);
        when(tourRepository.save(any(TourEntity.class))).thenReturn(updatedTourEntity);
        when(tourMapper.toTourResponseDto(updatedTourEntity)).thenReturn(tourResponseDto);

        // Execute
        ApiResponseDto<TourResponseDto> result = tourService.addTicket(flyId, tourId);

        // Then
        assertNotNull(result);
        assertEquals("success", result.getStatus());
        assertEquals("Ticket added successfully", result.getMessage());
        assertEquals(tourResponseDto, result.getData());

        // ✅ Verificación adicional: el ticket debe estar vinculado al tour
        assertEquals(tourEntity, newTicket.getTour(), "El ticket no tiene asignado el tour correctamente");

        // Verify interactions
        verify(tourRepository).findById(tourId);
        verify(flyRepository).findById(flyId);
        verify(tourHelper).createTickets(any(), any());
        verify(tourRepository).save(any(TourEntity.class));
        verify(tourMapper).toTourResponseDto(updatedTourEntity);
    }

    @Test
    void addTicket_shouldThrowEntityNotFoundException_whenTourNotFound() {
        // Given
        Long tourId = 999L;
        Long flyId = 1L;

        // When
        when(tourRepository.findById(tourId)).thenReturn(Optional.empty());

        // Then
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            tourService.addTicket(flyId, tourId);
        });
        assertEquals("Tour not found", exception.getMessage());
    }

    @Test
    void addTicket_shouldThrowEntityNotFoundException_whenFlyNotFound() {
        // Given
        Long tourId = 1L;
        Long flyId = 999L;

        // Create tour
        TourEntity tourEntity = TourEntity.builder()
                .id(tourId)
                .customer(CustomerEntity.builder().dni(UUID.randomUUID().toString()).build())
                .tickets(new HashSet<>())
                .reservations(new HashSet<>())
                .build();

        // When
        when(tourRepository.findById(tourId)).thenReturn(Optional.of(tourEntity));
        when(flyRepository.findById(flyId)).thenReturn(Optional.empty());

        // Then
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            tourService.addTicket(flyId, tourId);
        });
        assertEquals("Fly not found", exception.getMessage());
    }


    @Test
    void read_shouldThrowEntityNotFoundException_whenTourNotFound() {
        // Given
        Long tourId = 1L;
        when(tourRepository.findById(tourId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(EntityNotFoundException.class, () -> tourService.read(tourId));
    }

    @Test
    void testReadTourSuccess(){
        // se debe de crear el objeto id que se manda en e request
        // se debe de crear el objeto tour que se espera como respuesta
        // se debe de crear el objeto reservation que se agreg al tour buscado  el id nomas
        // se debe de crear el objeto ticket que se agreg al tour buscado el id nomas
        //se debe de crear el objeto custumero que se agrega en el resultado  el id nomas
        // se debe de convertir a TourResponseDto el objeto tour que se espera como respuesta
        // se debe de crear el objeto ApiResponseDto que se espera como respuesta
        // se debe de mockear el comportamiento del repository y mapper
        //
        // se debe de llamar al metodo read del servicio

        // Given
        Long tourId = 1L;

        // Creando objeto reservation
        Set<ReservationEntity> reservations = new HashSet<>();
        ReservationEntity reservation = ReservationEntity.builder()
                .id(UUID.randomUUID())
                .dateTimeReservation(LocalDateTime.now())
                .dateStart(LocalDate.now())
                .dateEnd(LocalDate.now().plusDays(5))
                .totalDays(5)
                .price(BigDecimal.valueOf(100))
                .build();
        reservations.add(reservation);

        // creando el objeto custumer
        CustomerEntity customer = CustomerEntity.builder()
                .dni("12345678")
                .fullName("John Doe")
                .creditCard("1234-5678-9012-3456")
                .phoneNumber("123456789")
                .totalFlights(0)
                .totalLodgings(0)
                .totalTours(0)
                .build();

        // Creando objeto ticket
        Set<TicketEntity> tickets = new HashSet<>();
        TicketEntity ticket = TicketEntity.builder()
                .id(UUID.randomUUID())
                .fly(FlyEntity.builder().id(1L).price(BigDecimal.valueOf(100)).build())
                .customer(CustomerEntity.builder().dni("12345678").build())
                .price(BigDecimal.valueOf(125))
                .purchaseDate(LocalDate.now())
                .arrivalDate(LocalDateTime.now())
                .departureDate(LocalDateTime.now())
                .build();
        tickets.add(ticket);

        // Creando objeto tour
        TourEntity tour = TourEntity.builder()
                .id(tourId)
                .customer(customer)
                .reservations(reservations)
                .tickets(tickets)
                .build();

        // Creando objeto TourResponseDto
        TourResponseDto tourResponseDto = TourResponseDto.builder()
                .id(tour.getId())
                .ticketsIds(tickets.stream().map(TicketEntity::getId).collect(Collectors.toSet()))
                .reservationIds(reservations.stream().map(ReservationEntity::getId).collect(Collectors.toSet()))
                .build();

        // Creando objeto ApiResponseDto
        ApiResponseDto<TourResponseDto> expectedResponse = ApiResponseDto.<TourResponseDto>builder()
                .status("success")
                .message("Tour retrieved successfully")
                .data(tourResponseDto)
                .build();

        // Mocking repository and mapper behavior
        when(tourRepository.findById(tourId)).thenReturn(Optional.of(tour));
        when(tourMapper.toTourResponseDto(tour)).thenReturn(tourResponseDto);
        // When - Calling the read method
        ApiResponseDto<TourResponseDto> result = tourService.read(tourId);
        // Then - Verifying results
        assertNotNull(result);
        assertEquals("success", result.getStatus());
        assertEquals("Tour retrieved successfully", result.getMessage());
        assertEquals(expectedResponse.getData().getId(), result.getData().getId());
        assertEquals(expectedResponse.getData().getTicketsIds(), result.getData().getTicketsIds());
        assertEquals(expectedResponse.getData().getReservationIds(), result.getData().getReservationIds());
        // Verifying interactions
        verify(tourRepository).findById(tourId);
        verify(tourMapper).toTourResponseDto(tour);
        // Verifying that the tour was found and mapped correctly
        assertTrue(result.getData().getTicketsIds().contains(ticket.getId()));
        assertTrue(result.getData().getReservationIds().contains(reservation.getId()));
        assertEquals(customer.getDni(), tour.getCustomer().getDni());
        assertEquals(customer.getFullName(), tour.getCustomer().getFullName());
        assertEquals(tour.getId(), result.getData().getId());
        assertEquals(tickets.size(), result.getData().getTicketsIds().size());

    }


    @Test
    void update_shouldThrowEntityNotFoundException_whenTourNotFound() {
        // Given
        Long tourId = 1L;
        TourRequestDto request = TourRequestDto.builder().build();
        when(tourRepository.findById(tourId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(EntityNotFoundException.class, () -> tourService.update(tourId, request));
    }

    @Test
    void update_shouldThrowEntityNotFoundException_whenCustomerNotFound() {
        // Given
        Long tourId = 1L;
        String customerId = "customer123";
        TourRequestDto request = TourRequestDto.builder()
                .idClient(customerId)
                .idFlys(new HashSet<>())
                .idHotels(new HashSet<>())
                .build();

        TourEntity tourEntity = TourEntity.builder().id(tourId).build();
        when(tourRepository.findById(tourId)).thenReturn(Optional.of(tourEntity));
        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(EntityNotFoundException.class, () -> tourService.update(tourId, request));
    }

    @Test
    void update_shouldThrowEntityNotFoundException_whenFlyNotFound() {
        // Given
        Long tourId = 1L;
        String customerId = "customer123";
        Long flyId = 1L;

        TourFlyRequestDto flyRequestDto = TourFlyRequestDto.builder()
                .idFly(flyId)
                .build();
        Set<TourFlyRequestDto> flyRequests = new HashSet<>();
        flyRequests.add(flyRequestDto);

        TourRequestDto request = TourRequestDto.builder()
                .idClient(customerId)
                .idFlys(flyRequests)
                .idHotels(new HashSet<>())
                .build();

        TourEntity tourEntity = TourEntity.builder().id(tourId).build();
        CustomerEntity customer = CustomerEntity.builder().dni(customerId).build();

        when(tourRepository.findById(tourId)).thenReturn(Optional.of(tourEntity));
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        when(flyRepository.findById(flyId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(EntityNotFoundException.class, () -> tourService.update(tourId, request));
    }

    @Test
    void update_shouldThrowEntityNotFoundException_whenHotelNotFound() {
        // Given
        Long tourId = 1L;
        String customerId = "customer123";
        Long hotelId = 1L;

        TourHotelRequest hotelRequestDto = TourHotelRequest.builder()
                .idHotel(hotelId)
                .totalDays(5)
                .build();
        Set<TourHotelRequest> hotelRequests = new HashSet<>();
        hotelRequests.add(hotelRequestDto);

        TourRequestDto request = TourRequestDto.builder()
                .idClient(customerId)
                .idFlys(new HashSet<>())
                .idHotels(hotelRequests)
                .build();

        TourEntity tourEntity = TourEntity.builder().id(tourId).build();
        CustomerEntity customer = CustomerEntity.builder().dni(customerId).build();

        when(tourRepository.findById(tourId)).thenReturn(Optional.of(tourEntity));
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        when(hotelRepository.findById(hotelId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(EntityNotFoundException.class, () -> tourService.update(tourId, request));
    }

    @Test
    void testTourUpdate() {
        // Given
        Long tourId = 1L;
        String customerId = UUID.randomUUID().toString();
        Long flyId = 1L;
        Long hotelId = 1L;
        Integer totalDays = 5;


        // Create request DTOs
        TourFlyRequestDto flyRequestDto = TourFlyRequestDto.builder()
                .idFly(flyId)
                .build();

        TourHotelRequest hotelRequestDto = TourHotelRequest.builder()
                .idHotel(hotelId)
                .totalDays(totalDays)
                .build();

        Set<TourFlyRequestDto> flyRequests = new HashSet<>();
        flyRequests.add(flyRequestDto);

        Set<TourHotelRequest> hotelRequests = new HashSet<>();
        hotelRequests.add(hotelRequestDto);

        // Create TourRequestDto par amandarlo a parametro del metodo update
        TourRequestDto request = TourRequestDto.builder()
                .idClient(customerId)
                .idFlys(flyRequests)
                .idHotels(hotelRequests)
                .build();

        // Create entities
        CustomerEntity customer = CustomerEntity.builder()
                .dni(UUID.randomUUID().toString())
                .fullName("John Doe")
                .creditCard("1234-5678-9012-3456")
                .phoneNumber("123456789")
                .totalFlights(0)
                .totalLodgings(0)
                .totalTours(0)
                .build();

        FlyEntity fly = FlyEntity.builder()
                .id(2L)
                .price(BigDecimal.valueOf(100))
                .originName("Madrid")
                .destinyName("Barcelona")
                .originLat(40.4168)
                .originLng(-3.7038)
                .destinyLat(41.3851)
                .destinyLng(2.1734)
                .build();

        HotelEntity hotel = HotelEntity.builder()
                .id(hotelId)
                .name("Test Hotel")
                .address("Test Address")
                .rating(4)
                .price(BigDecimal.valueOf(200))
                .build();

        Set<TicketEntity> tickets = new HashSet<>();
        TicketEntity ticket = TicketEntity.builder()
                .id(UUID.randomUUID())
                .fly(fly)
                .customer(customer)
                .price(fly.getPrice().multiply(BigDecimal.valueOf(1.25)))
                .purchaseDate(LocalDate.now())
                .arrivalDate(LocalDateTime.now())
                .departureDate(LocalDateTime.now())
                .build();
        tickets.add(ticket);

        Set<ReservationEntity> reservations = new HashSet<>();
        ReservationEntity reservation = ReservationEntity.builder()
                .id(UUID.randomUUID())
                .hotel(hotel)
                .customer(customer)
                .dateTimeReservation(LocalDateTime.now())
                .dateStart(LocalDate.now())
                .dateEnd(LocalDate.now().plusDays(totalDays))
                .totalDays(totalDays)
                .price(hotel.getPrice().multiply(BigDecimal.valueOf(1.25)))
                .build();
        reservations.add(reservation);


        TourEntity tourWhitFindId = TourEntity.builder()
                .id(tourId)
                .customer(customer)
                .tickets(tickets)
                .reservations(reservations)
                .build();

        // updatedel tour
        FlyEntity flyUpdate = FlyEntity.builder()
                .id(flyId)
                .price(BigDecimal.valueOf(100))
                .originName("Madrid")
                .destinyName("Barcelona")
                .originLat(40.4168)
                .originLng(-3.7038)
                .destinyLat(41.3851)
                .destinyLng(2.1734)
                .build();

        HotelEntity hotelUpdate = HotelEntity.builder()
                .id(hotelId)
                .name("Test Hotel")
                .address("Test Address")
                .rating(4)
                .price(BigDecimal.valueOf(200))
                .build();

        CustomerEntity customerUpdate = CustomerEntity.builder()
                .dni(customerId)
                .fullName("John Doe Updated")
                .creditCard("1234-5678-9012-3456")
                .phoneNumber("123456789")
                .totalFlights(0)
                .totalLodgings(0)
                .totalTours(0)
                .build();

        Set<TicketEntity> ticketsUpdate = new HashSet<>();
        TicketEntity ticketUpdate = TicketEntity.builder()
                .id(UUID.randomUUID())
                .fly(flyUpdate)
                .customer(customerUpdate)
                .price(flyUpdate.getPrice().multiply(BigDecimal.valueOf(1.25)))
                .purchaseDate(LocalDate.now())
                .arrivalDate(LocalDateTime.now())
                .departureDate(LocalDateTime.now())
                .build();
        ticketsUpdate.add(ticketUpdate);

        Set<ReservationEntity> reservationsUpdate = new HashSet<>();
        ReservationEntity reservationUpdate = ReservationEntity.builder()
                .id(UUID.randomUUID())
                .hotel(hotelUpdate)
                .customer(customerUpdate)
                .dateTimeReservation(LocalDateTime.now())
                .dateStart(LocalDate.now())
                .dateEnd(LocalDate.now().plusDays(totalDays))
                .totalDays(totalDays)
                .price(hotelUpdate.getPrice().multiply(BigDecimal.valueOf(1.25)))
                .build();
        reservationsUpdate.add(reservationUpdate);

        TourEntity updatedTour = TourEntity.builder()
                .id(tourId)
                .customer(customerUpdate)
                .tickets(ticketsUpdate)
                .reservations(reservationsUpdate)
                .build();

        // Creando objeto TourResponseDto
        TourResponseDto tourResponseDto = TourResponseDto.builder()
                .id(updatedTour.getId())
                .ticketsIds(ticketsUpdate.stream().map(TicketEntity::getId).collect(Collectors.toSet()))
                .reservationIds(reservationsUpdate.stream().map(ReservationEntity::getId).collect(Collectors.toSet()))
                .build();

        // Creando objeto ApiResponseDto
        ApiResponseDto<TourResponseDto> expectedResponse = ApiResponseDto.<TourResponseDto>builder()
                .status("success")
                .message("Tour update successfully")
                .data(tourResponseDto)
                .build();

        // Mocking repository and mapper behavior
        when(tourRepository.findById(tourId)).thenReturn(Optional.of(tourWhitFindId));
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customerUpdate));
        when(flyRepository.findById(flyId)).thenReturn(Optional.of(flyUpdate));
        when(hotelRepository.findById(hotelId)).thenReturn(Optional.of(hotelUpdate));
        when(tourHelper.createTickets(any(), any())).thenReturn(ticketsUpdate);
        when(tourHelper.createReservations(any(), any())).thenReturn(reservationsUpdate);
        when(tourRepository.save(any(TourEntity.class))).thenReturn(updatedTour);
        when(tourMapper.toTourResponseDto(updatedTour)).thenReturn(tourResponseDto);
        // When - Calling the update method
        ApiResponseDto<TourResponseDto> result = tourService.update(tourId, request);
        // Then - Verifying results
        assertNotNull(result);
        assertEquals("success", result.getStatus());
        assertEquals("Tour update successfully", result.getMessage());
        assertEquals(expectedResponse.getData().getId(), result.getData().getId());
        assertEquals(expectedResponse.getData().getTicketsIds(), result.getData().getTicketsIds());
        assertEquals(expectedResponse.getData().getReservationIds(), result.getData().getReservationIds());
        // Verifying interactions
        verify(tourRepository).findById(tourId);
        verify(customerRepository).findById(customerId);
        verify(flyRepository).findById(flyId);
        verify(hotelRepository).findById(hotelId);
        verify(tourHelper).createTickets(any(), any());
        verify(tourHelper).createReservations(any(), any());
        verify(tourRepository).save(any(TourEntity.class));
        verify(tourMapper).toTourResponseDto(updatedTour);
        // Verifying that the tour was found and mapped correctly
        assertEquals(1, result.getData().getTicketsIds().size());
        assertTrue(result.getData().getReservationIds().contains(reservationUpdate.getId()));
        assertEquals(updatedTour.getId(), result.getData().getId());
        assertTrue(result.getData().getTicketsIds().contains(ticketUpdate.getId()));
        assertEquals(customerUpdate.getDni(), updatedTour.getCustomer().getDni());
        assertEquals(customerUpdate.getFullName(), updatedTour.getCustomer().getFullName());


    }

    @Test
    void testRemoveTickeSucces() {
        // Given
        UUID ticketId = UUID.randomUUID();
        Long tourId = 1L;

        // Create a tour with the ticket
        Set<TicketEntity> tickets = new HashSet<>();
        TicketEntity ticket = TicketEntity.builder()
                .id(ticketId)
                .fly(FlyEntity.builder().id(1L).build())
                .customer(CustomerEntity.builder().dni(UUID.randomUUID().toString()).build())
                .price(BigDecimal.valueOf(125))
                .purchaseDate(LocalDate.now())
                .arrivalDate(LocalDateTime.now())
                .departureDate(LocalDateTime.now())
                .build();
        tickets.add(ticket);
        TicketEntity ticket2 = TicketEntity.builder()
                .id(UUID.randomUUID())
                .fly(FlyEntity.builder().id(1L).build())
                .customer(CustomerEntity.builder().dni(UUID.randomUUID().toString()).build())
                .price(BigDecimal.valueOf(125))
                .purchaseDate(LocalDate.now())
                .arrivalDate(LocalDateTime.now())
                .departureDate(LocalDateTime.now())
                .build();
        tickets.add(ticket2);

        TourEntity tourEntity = TourEntity.builder()
                .id(tourId)
                .customer(CustomerEntity.builder().dni(UUID.randomUUID().toString()).build())
                .tickets(tickets)
                .reservations(new HashSet<>())
                .build();

        Set<TicketEntity> ticketsRemove = new HashSet<>();
        ticketsRemove.add(ticket2);

        TourEntity tourEntityWithTicket = TourEntity.builder()
                .id(tourId)
                .customer(tourEntity.getCustomer())
                .tickets(ticketsRemove)
                .reservations(new HashSet<>())
                .build();

       TourResponseDto tourEntityDto = TourResponseDto.builder()
                .id(tourId)
                .ticketsIds(tourEntityWithTicket.getTickets().stream().map(TicketEntity::getId).collect(Collectors.toSet()))
                .reservationIds(new HashSet<>())
                .build();



        ApiResponseDto<TourResponseDto> expectedResponse = ApiResponseDto.<TourResponseDto>builder()
                .status("success")
                .message("Ticket removed successfully")
                .data(tourEntityDto)
                .build();

        // Mocking repository behavior
        when(tourRepository.findById(tourId)).thenReturn(Optional.of(tourEntity));
        when(tourRepository.save(any(TourEntity.class))).thenReturn(tourEntityWithTicket);
        when(tourMapper.toTourResponseDto(tourEntityWithTicket)).thenReturn(tourEntityDto);
        // When
        ApiResponseDto<TourResponseDto> result = tourService.removeTicket(ticketId, tourId);

        // Then
        assertNotNull(result);
        assertEquals("success", result.getStatus());
        assertEquals("Ticket removed successfully", result.getMessage());
        assertEquals(expectedResponse.getData().getTicketsIds(), result.getData().getTicketsIds());
        assertEquals(1,result.getData().getTicketsIds().size());
        assertTrue(result.getData().getTicketsIds().contains(ticket2.getId()));
        // Verify interactions
        verify(tourRepository).findById(tourId);
        verify(tourRepository, times(1)).save(any(TourEntity.class));
    }

    @Test
    void testRemoveTicket_shouldThrowEntityNotFoundException_whenTourNotFound() {
        // Given
        UUID ticketId = UUID.randomUUID();
        Long tourId = 1L;

        // When
        when(tourRepository.findById(tourId)).thenReturn(Optional.empty());

        // Then
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            tourService.removeTicket(ticketId, tourId);
        });
        assertEquals("Tour not found", exception.getMessage());
    }

    @Test
    void testRemoveTicketWhenTicketNotFound() {
        // Given
        UUID ticketId = UUID.randomUUID();
        Long tourId = 1L;

        // Create a tour without the ticket
        TourEntity tourEntity = TourEntity.builder()
                .id(tourId)
                .customer(CustomerEntity.builder().dni(UUID.randomUUID().toString()).build())
                .tickets(new HashSet<>())
                .reservations(new HashSet<>())
                .build();

        TourEntity updatedTourEntity = TourEntity.builder()
                .id(tourId)
                .customer(tourEntity.getCustomer())
                .tickets(new HashSet<>())
                .reservations(new HashSet<>())
                .build();

        TourResponseDto tourResponseDto = TourResponseDto.builder()
                .id(tourId)
                .ticketsIds(new HashSet<>())
                .reservationIds(new HashSet<>())
                .build();

        // When
        when(tourRepository.findById(tourId)).thenReturn(Optional.of(tourEntity));
        when(tourRepository.save(any(TourEntity.class))).thenReturn(updatedTourEntity);
        when(tourMapper.toTourResponseDto(updatedTourEntity)).thenReturn(tourResponseDto);

        // Then - The method should not throw an exception and return a success response
        ApiResponseDto<TourResponseDto> result = tourService.removeTicket(ticketId, tourId);

        assertNotNull(result);
        assertEquals("success", result.getStatus());
        assertEquals("Ticket removed successfully", result.getMessage());
        assertEquals(tourResponseDto, result.getData());

        // Verify interactions
        verify(tourRepository).findById(tourId);
        // The save method is called once in the implementation
        verify(tourRepository, times(1)).save(any(TourEntity.class));
        verify(tourMapper).toTourResponseDto(updatedTourEntity);
    }


    @Test
    void testAddReservationSuccess() {
        // Given
        UUID reservationId = UUID.randomUUID();
        Long tourId = 1L;

        // Crear reserva sin tour asignado aún (simulando la base)
        ReservationEntity reservation = ReservationEntity.builder()
                .id(reservationId)
                .dateTimeReservation(LocalDateTime.now())
                .dateStart(LocalDate.now())
                .dateEnd(LocalDate.now().plusDays(5))
                .totalDays(5)
                .price(BigDecimal.valueOf(100))
                .build();

        // Crear tour sin reservas (vacío inicialmente)
        TourEntity tourEntity = TourEntity.builder()
                .id(tourId)
                .customer(CustomerEntity.builder().dni(UUID.randomUUID().toString()).build())
                .tickets(new HashSet<>())
                .reservations(new HashSet<>())  // Vacío inicialmente
                .build();

        // Aquí simulamos que después de agregar reserva, el tour tendrá esa reserva
        tourEntity.addReservation(reservation);

        TourResponseDto tourResponseDto = TourResponseDto.builder()
                .id(tourId)
                .ticketsIds(new HashSet<>())
                .reservationIds(tourEntity.getReservations().stream()
                        .map(ReservationEntity::getId)
                        .collect(Collectors.toSet()))
                .build();

        ApiResponseDto<TourResponseDto> expectedResponse = ApiResponseDto.<TourResponseDto>builder()
                .status("success")
                .message("Reservation added successfully")
                .data(tourResponseDto)
                .build();

        // Mock behavior
        when(tourRepository.findById(tourId)).thenReturn(Optional.of(tourEntity));
        when(reservationRepository.findById(reservationId)).thenReturn(Optional.of(reservation));
        when(tourRepository.save(any(TourEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(tourMapper.toTourResponseDto(any(TourEntity.class))).thenReturn(tourResponseDto);

        // When
        ApiResponseDto<TourResponseDto> result = tourService.addReservation(reservationId, tourId);

        // Then
        assertNotNull(result);
        assertEquals("success", result.getStatus());
        assertEquals("Reservation added successfully", result.getMessage());
        assertEquals(expectedResponse.getData().getReservationIds(), result.getData().getReservationIds());

        // Verifica que la reserva está en el tour
        assertTrue(tourEntity.getReservations().contains(reservation));

        // Verifica bidireccionalidad: reserva apunta al tour
        assertEquals(tourEntity, reservation.getTour());

        // Verify mocks were called correctly
        verify(tourRepository).findById(tourId);
        verify(reservationRepository).findById(reservationId);
        verify(tourRepository).save(any(TourEntity.class));
        verify(tourMapper).toTourResponseDto(any(TourEntity.class));
    }


    @Test
    void testAddReservation_shouldThrowEntityNotFoundException_whenReservationNotFound() {
        // Given
        UUID reservationId = UUID.randomUUID();
        Long tourId = 1L;

        // Create a tour
        TourEntity tourEntity = TourEntity.builder()
                .id(tourId)
                .customer(CustomerEntity.builder().dni(UUID.randomUUID().toString()).build())
                .tickets(new HashSet<>())
                .reservations(new HashSet<>())
                .build();

        // When
        when(tourRepository.findById(tourId)).thenReturn(Optional.of(tourEntity));
        when(reservationRepository.findById(reservationId)).thenReturn(Optional.empty());

        // Then
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            tourService.addReservation(reservationId, tourId);
        });
        assertEquals("Reservation not found", exception.getMessage());
    }

    @Test
    void testRemoveReservationReturnSucces(){
        // Given
        UUID reservationId = UUID.randomUUID();
        Long tourId = 1L;

        // Create a tour with the reservation
        Set<ReservationEntity> reservations = new HashSet<>();
        ReservationEntity reservation = ReservationEntity.builder()
                .id(reservationId)
                .dateTimeReservation(LocalDateTime.now())
                .dateStart(LocalDate.now())
                .dateEnd(LocalDate.now().plusDays(5))
                .totalDays(5)
                .price(BigDecimal.valueOf(100))
                .build();
        reservations.add(reservation);

        TourEntity tourEntity = TourEntity.builder()
                .id(tourId)
                .customer(CustomerEntity.builder().dni(UUID.randomUUID().toString()).build())
                .tickets(new HashSet<>())
                .reservations(reservations)
                .build();

        Set<ReservationEntity> reservationsRemove = new HashSet<>();

        TourEntity tourEntityWithReservation = TourEntity.builder()
                .id(tourId)
                .customer(tourEntity.getCustomer())
                .tickets(new HashSet<>())
                .reservations(reservationsRemove)
                .build();

        TourResponseDto tourResponseDto = TourResponseDto.builder()
                .id(tourId)
                .ticketsIds(new HashSet<>())
                .reservationIds(tourEntityWithReservation.getReservations().stream().map(ReservationEntity::getId).collect(Collectors.toSet()))
                .build();

        ApiResponseDto<TourResponseDto> expectedResponse = ApiResponseDto.<TourResponseDto>builder()
                .status("success")
                .message("Reservation removed successfully")
                .statusCode(200)
                .data(tourResponseDto)
                .build();

        // Mocking repository behavior
        when(tourRepository.findById(tourId)).thenReturn(Optional.of(tourEntity));
        when(tourRepository.save(any(TourEntity.class))).thenReturn(tourEntityWithReservation);
        when(tourMapper.toTourResponseDto(tourEntityWithReservation)).thenReturn(tourResponseDto);

        // When
        ApiResponseDto<TourResponseDto> result = tourService.removeReservation(reservationId, tourId);

        // Then
        assertNotNull(result);
        assertEquals("success", result.getStatus());
        assertEquals("Reservation removed successfully", result.getMessage());
        assertEquals(expectedResponse.getData().getReservationIds(), result.getData().getReservationIds());
        assertTrue(result.getData().getReservationIds().isEmpty());
    }

    @Test
    void testRemoveReservation_shouldThrowEntityNotFoundException_whenTourNotFound() {
        // Given
        UUID reservationId = UUID.randomUUID();
        Long tourId = 1L;

        // When
        when(tourRepository.findById(tourId)).thenReturn(Optional.empty());

        // Then
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            tourService.removeReservation(reservationId, tourId);
        });
        assertEquals("Tour not found", exception.getMessage());
    }

    @Test
    void testRemoveReservationWhenReservationNotFound() {
        // Given
        UUID reservationId = UUID.randomUUID();
        Long tourId = 1L;

        // Create a tour without the reservation
        TourEntity tourEntity = TourEntity.builder()
                .id(tourId)
                .customer(CustomerEntity.builder().dni(UUID.randomUUID().toString()).build())
                .tickets(new HashSet<>())
                .reservations(new HashSet<>())
                .build();

        TourEntity updatedTourEntity = TourEntity.builder()
                .id(tourId)
                .customer(tourEntity.getCustomer())
                .tickets(new HashSet<>())
                .reservations(new HashSet<>())
                .build();

        TourResponseDto tourResponseDto = TourResponseDto.builder()
                .id(tourId)
                .ticketsIds(new HashSet<>())
                .reservationIds(new HashSet<>())
                .build();

        // When
        when(tourRepository.findById(tourId)).thenReturn(Optional.of(tourEntity));
        when(tourRepository.save(any(TourEntity.class))).thenReturn(updatedTourEntity);
        when(tourMapper.toTourResponseDto(updatedTourEntity)).thenReturn(tourResponseDto);

        // Then - The method should not throw an exception and return a success response
        ApiResponseDto<TourResponseDto> result = tourService.removeReservation(reservationId, tourId);

        assertNotNull(result);
        assertEquals("success", result.getStatus());
        assertEquals("Reservation removed successfully", result.getMessage());
        assertEquals(tourResponseDto, result.getData());

        // Verify interactions
        verify(tourRepository).findById(tourId);
        verify(tourRepository).save(any(TourEntity.class));
        verify(tourMapper).toTourResponseDto(updatedTourEntity);
    }



}
