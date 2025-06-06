package com.joirv.CursoSpringBoot.infraestructure.services;

import com.joirv.CursoSpringBoot.api.models.request.TourFlyRequestDto;
import com.joirv.CursoSpringBoot.api.models.request.TourHotelRequest;
import com.joirv.CursoSpringBoot.api.models.request.TourRequestDto;
import com.joirv.CursoSpringBoot.api.models.responses.ApiResponseDto;
import com.joirv.CursoSpringBoot.api.models.responses.TourResponseDto;
import com.joirv.CursoSpringBoot.domain.entities.*;
import com.joirv.CursoSpringBoot.domain.mappers.TourMapper;
import com.joirv.CursoSpringBoot.domain.repositories.*;
import com.joirv.CursoSpringBoot.infraestructure.abstract_services.ITourService;
import com.joirv.CursoSpringBoot.infraestructure.helper.TourHelper;
import com.joirv.CursoSpringBoot.util.AeroLinea;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TourServiceSpringTest {

    @Mock
    private TourRepository tourRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private FlyRepository flyRepository;

    @Mock
    private HotelRepository hotelRepository;

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private TourHelper tourHelper;

    @Mock
    private TourMapper tourMapper;

    @InjectMocks
    private TourService tourService;

    @Test
    void testCreateTour() {
        // Given
        String customerId = UUID.randomUUID().toString();
        Long flyId = 1L;
        Long hotelId = 1L;

        CustomerEntity customer = CustomerEntity.builder()
                .dni(customerId)
                .fullName("Test Customer")
                .build();

        FlyEntity fly = FlyEntity.builder()
                .id(flyId)
                .originName("Origin")
                .destinyName("Destiny")
                .aeroLine(AeroLinea.aero_gold)
                .price(BigDecimal.valueOf(100))
                .build();

        HotelEntity hotel = HotelEntity.builder()
                .id(hotelId)
                .name("Test Hotel")
                .address("Test Address")
                .rating(5)
                .price(BigDecimal.valueOf(100))
                .build();

        TourRequestDto request = new TourRequestDto();
        request.setIdClient(customerId);

        Set<TourFlyRequestDto> flyRequests = new HashSet<>();
        TourFlyRequestDto flyRequest = new TourFlyRequestDto();
        flyRequest.setIdFly(flyId);
        flyRequests.add(flyRequest);
        request.setIdFlys(flyRequests);

        Set<TourHotelRequest> hotelRequests = new HashSet<>();
        TourHotelRequest hotelRequest = new TourHotelRequest();
        hotelRequest.setIdHotel(hotelId);
        hotelRequest.setTotalDays(5);
        hotelRequests.add(hotelRequest);
        request.setIdHotels(hotelRequests);

        // Mock repository responses
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        when(flyRepository.findById(flyId)).thenReturn(Optional.of(fly));
        when(hotelRepository.findById(hotelId)).thenReturn(Optional.of(hotel));

        // Mock ticket and reservation creation
        Set<TicketEntity> tickets = new HashSet<>();
        TicketEntity ticket = TicketEntity.builder()
                .id(UUID.randomUUID())
                .fly(fly)
                .customer(customer)
                .price(fly.getPrice())
                .purchaseDate(LocalDate.now())
                .departureDate(LocalDateTime.now().plusDays(1))
                .arrivalDate(LocalDateTime.now().plusDays(1))
                .build();
        tickets.add(ticket);

        Set<ReservationEntity> reservations = new HashSet<>();
        ReservationEntity reservation = ReservationEntity.builder()
                .id(UUID.randomUUID())
                .hotel(hotel)
                .customer(customer)
                .dateTimeReservation(LocalDateTime.now())
                .dateStart(LocalDate.now())
                .dateEnd(LocalDate.now().plusDays(5))
                .totalDays(5)
                .price(BigDecimal.valueOf(500))
                .build();
        reservations.add(reservation);

        when(tourHelper.createTickets(any(), any())).thenReturn(tickets);
        when(tourHelper.createReservations(any(), any())).thenReturn(reservations);

        // Mock tour entity creation and save
        TourEntity tourEntity = TourEntity.builder()
                .id(1L)
                .customer(customer)
                .tickets(tickets)
                .reservations(reservations)
                .build();
        when(tourRepository.save(any(TourEntity.class))).thenReturn(tourEntity);

        // Mock mapper response
        TourResponseDto tourResponseDto = TourResponseDto.builder()
                .id(1L)
                .ticketsIds(tickets.stream().map(TicketEntity::getId).collect(Collectors.toSet()))
                .reservationIds(reservations.stream().map(ReservationEntity::getId).collect(Collectors.toSet()))
                .build();
        when(tourMapper.toTourResponseDto(any(TourEntity.class))).thenReturn(tourResponseDto);

        // When
        ApiResponseDto<TourResponseDto> response = tourService.create(request);

        // Then
        assertNotNull(response);
        assertEquals("success", response.getStatus());
        assertEquals("Tour created successfully", response.getMessage());
        assertNotNull(response.getData());
        assertNotNull(response.getData().getId());
        assertFalse(response.getData().getTicketsIds().isEmpty());
        assertFalse(response.getData().getReservationIds().isEmpty());

        // Verify interactions
        verify(customerRepository).findById(customerId);
        verify(flyRepository).findById(flyId);
        verify(hotelRepository).findById(hotelId);
        verify(tourHelper).createTickets(any(), any());
        verify(tourHelper).createReservations(any(), any());
        verify(tourRepository).save(any(TourEntity.class));
        verify(tourMapper).toTourResponseDto(any(TourEntity.class));
    }

    @Test
    void testReadTour() {
        // Given
        Long tourId = 1L;

        // Create a tour entity
        CustomerEntity customer = CustomerEntity.builder()
                .dni(UUID.randomUUID().toString())
                .fullName("Test Customer")
                .build();

        Set<TicketEntity> tickets = new HashSet<>();
        TicketEntity ticket = TicketEntity.builder()
                .id(UUID.randomUUID())
                .customer(customer)
                .price(BigDecimal.valueOf(100))
                .purchaseDate(LocalDate.now())
                .departureDate(LocalDateTime.now().plusDays(1))
                .arrivalDate(LocalDateTime.now().plusDays(1))
                .build();
        tickets.add(ticket);

        Set<ReservationEntity> reservations = new HashSet<>();
        ReservationEntity reservation = ReservationEntity.builder()
                .id(UUID.randomUUID())
                .customer(customer)
                .dateTimeReservation(LocalDateTime.now())
                .dateStart(LocalDate.now())
                .dateEnd(LocalDate.now().plusDays(5))
                .totalDays(5)
                .price(BigDecimal.valueOf(500))
                .build();
        reservations.add(reservation);

        TourEntity tourEntity = TourEntity.builder()
                .id(tourId)
                .customer(customer)
                .tickets(tickets)
                .reservations(reservations)
                .build();

        // Create expected response
        TourResponseDto tourResponseDto = TourResponseDto.builder()
                .id(tourId)
                .ticketsIds(tickets.stream().map(TicketEntity::getId).collect(Collectors.toSet()))
                .reservationIds(reservations.stream().map(ReservationEntity::getId).collect(Collectors.toSet()))
                .build();

        // Mock repository and mapper
        when(tourRepository.findById(tourId)).thenReturn(Optional.of(tourEntity));
        when(tourMapper.toTourResponseDto(tourEntity)).thenReturn(tourResponseDto);

        // When
        ApiResponseDto<TourResponseDto> response = tourService.read(tourId);

        // Then
        assertNotNull(response);
        assertEquals("success", response.getStatus());
        assertEquals("Tour retrieved successfully", response.getMessage());
        assertNotNull(response.getData());
        assertEquals(tourId, response.getData().getId());

        // Verify interactions
        verify(tourRepository).findById(tourId);
        verify(tourMapper).toTourResponseDto(tourEntity);
    }

    @Test
    void testUpdateTour() {
        // Given
        Long tourId = 1L;
        String customerId = UUID.randomUUID().toString();
        Long flyId = 1L;
        Long hotelId = 1L;

        // Create existing tour entity
        CustomerEntity oldCustomer = CustomerEntity.builder()
                .dni(UUID.randomUUID().toString())
                .fullName("Old Customer")
                .build();

        TourEntity existingTour = TourEntity.builder()
                .id(tourId)
                .customer(oldCustomer)
                .tickets(new HashSet<>())
                .reservations(new HashSet<>())
                .build();

        // Create new entities for update
        CustomerEntity newCustomer = CustomerEntity.builder()
                .dni(customerId)
                .fullName("New Customer")
                .build();

        FlyEntity newFly = FlyEntity.builder()
                .id(flyId)
                .originName("New Origin")
                .destinyName("New Destiny")
                .aeroLine(AeroLinea.aero_gold)
                .price(BigDecimal.valueOf(200))
                .build();

        HotelEntity newHotel = HotelEntity.builder()
                .id(hotelId)
                .name("New Hotel")
                .address("New Address")
                .rating(4)
                .price(BigDecimal.valueOf(150))
                .build();

        // Create request
        TourRequestDto request = new TourRequestDto();
        request.setIdClient(customerId);

        Set<TourFlyRequestDto> flyRequests = new HashSet<>();
        TourFlyRequestDto flyRequest = new TourFlyRequestDto();
        flyRequest.setIdFly(flyId);
        flyRequests.add(flyRequest);
        request.setIdFlys(flyRequests);

        Set<TourHotelRequest> hotelRequests = new HashSet<>();
        TourHotelRequest hotelRequest = new TourHotelRequest();
        hotelRequest.setIdHotel(hotelId);
        hotelRequest.setTotalDays(7);
        hotelRequests.add(hotelRequest);
        request.setIdHotels(hotelRequests);

        // Create new tickets and reservations
        Set<TicketEntity> newTickets = new HashSet<>();
        TicketEntity newTicket = TicketEntity.builder()
                .id(UUID.randomUUID())
                .fly(newFly)
                .customer(newCustomer)
                .price(newFly.getPrice())
                .purchaseDate(LocalDate.now())
                .departureDate(LocalDateTime.now().plusDays(1))
                .arrivalDate(LocalDateTime.now().plusDays(1))
                .build();
        newTickets.add(newTicket);

        Set<ReservationEntity> newReservations = new HashSet<>();
        ReservationEntity newReservation = ReservationEntity.builder()
                .id(UUID.randomUUID())
                .hotel(newHotel)
                .customer(newCustomer)
                .dateTimeReservation(LocalDateTime.now())
                .dateStart(LocalDate.now())
                .dateEnd(LocalDate.now().plusDays(7))
                .totalDays(7)
                .price(BigDecimal.valueOf(1050))
                .build();
        newReservations.add(newReservation);

        // Create updated tour entity
        TourEntity updatedTour = TourEntity.builder()
                .id(tourId)
                .customer(newCustomer)
                .tickets(newTickets)
                .reservations(newReservations)
                .build();

        // Create expected response
        TourResponseDto tourResponseDto = TourResponseDto.builder()
                .id(tourId)
                .ticketsIds(newTickets.stream().map(TicketEntity::getId).collect(Collectors.toSet()))
                .reservationIds(newReservations.stream().map(ReservationEntity::getId).collect(Collectors.toSet()))
                .build();

        // Mock repository responses
        when(tourRepository.findById(tourId)).thenReturn(Optional.of(existingTour));
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(newCustomer));
        when(flyRepository.findById(flyId)).thenReturn(Optional.of(newFly));
        when(hotelRepository.findById(hotelId)).thenReturn(Optional.of(newHotel));
        when(tourHelper.createTickets(any(), any())).thenReturn(newTickets);
        when(tourHelper.createReservations(any(), any())).thenReturn(newReservations);
        when(tourRepository.save(any(TourEntity.class))).thenReturn(updatedTour);
        when(tourMapper.toTourResponseDto(updatedTour)).thenReturn(tourResponseDto);

        // When
        ApiResponseDto<TourResponseDto> response = tourService.update(tourId, request);

        // Then
        assertNotNull(response);
        assertEquals("success", response.getStatus());
        assertEquals("Tour update successfully", response.getMessage());
        assertNotNull(response.getData());
        assertEquals(tourId, response.getData().getId());

        // Verify interactions
        verify(tourRepository).findById(tourId);
        verify(customerRepository).findById(customerId);
        verify(flyRepository).findById(flyId);
        verify(hotelRepository).findById(hotelId);
        verify(tourHelper).createTickets(any(), any());
        verify(tourHelper).createReservations(any(), any());
        verify(tourRepository).save(any(TourEntity.class));
        verify(tourMapper).toTourResponseDto(updatedTour);
    }

    @Test
    void testDeleteTour() {
        // Given
        Long tourId = 1L;

        // Create tour entity
        CustomerEntity customer = CustomerEntity.builder()
                .dni(UUID.randomUUID().toString())
                .fullName("Test Customer")
                .build();

        TourEntity tourEntity = TourEntity.builder()
                .id(tourId)
                .customer(customer)
                .tickets(new HashSet<>())
                .reservations(new HashSet<>())
                .build();

        // Mock repository response
        when(tourRepository.findById(tourId)).thenReturn(Optional.of(tourEntity));
        doNothing().when(tourRepository).delete(tourEntity);

        // When
        ApiResponseDto<Void> response = tourService.delete(tourId);

        // Then
        assertNotNull(response);
        assertEquals("success", response.getStatus());
        assertEquals("Tour deleted successfully", response.getMessage());

        // Verify interactions
        verify(tourRepository).findById(tourId);
        verify(tourRepository).delete(tourEntity);
    }

    @Test
    void testAddTicket() {
        // Given
        Long tourId = 1L;
        Long flyId = 2L;

        // Create customer entity
        CustomerEntity customer = CustomerEntity.builder()
                .dni(UUID.randomUUID().toString())
                .fullName("Test Customer")
                .build();

        // Create fly entity
        FlyEntity flyEntity = FlyEntity.builder()
                .id(flyId)
                .originName("Origin")
                .destinyName("Destiny")
                .aeroLine(AeroLinea.aero_gold)
                .price(BigDecimal.valueOf(100))
                .build();

        // Create initial tickets
        Set<TicketEntity> initialTickets = new HashSet<>();
        TicketEntity initialTicket = TicketEntity.builder()
                .id(UUID.randomUUID())
                .customer(customer)
                .price(BigDecimal.valueOf(100))
                .purchaseDate(LocalDate.now())
                .departureDate(LocalDateTime.now().plusDays(1))
                .arrivalDate(LocalDateTime.now().plusDays(1))
                .build();
        initialTickets.add(initialTicket);

        // Create tour entity
        TourEntity tourEntity = TourEntity.builder()
                .id(tourId)
                .customer(customer)
                .tickets(initialTickets)
                .reservations(new HashSet<>())
                .build();

        // Create new ticket to be added
        Set<TicketEntity> newTickets = new HashSet<>();
        TicketEntity newTicket = TicketEntity.builder()
                .id(UUID.randomUUID())
                .fly(flyEntity)
                .customer(customer)
                .price(flyEntity.getPrice())
                .purchaseDate(LocalDate.now())
                .departureDate(LocalDateTime.now().plusDays(2))
                .arrivalDate(LocalDateTime.now().plusDays(2))
                .build();
        newTickets.add(newTicket);

        // Create updated tour with both tickets
        Set<TicketEntity> allTickets = new HashSet<>(initialTickets);
        allTickets.addAll(newTickets);

        TourEntity updatedTourEntity = TourEntity.builder()
                .id(tourId)
                .customer(customer)
                .tickets(allTickets)
                .reservations(new HashSet<>())
                .build();

        // Create expected response
        TourResponseDto tourResponseDto = TourResponseDto.builder()
                .id(tourId)
                .ticketsIds(allTickets.stream().map(TicketEntity::getId).collect(Collectors.toSet()))
                .reservationIds(new HashSet<>())
                .build();

        // Mock repository responses
        when(tourRepository.findById(tourId)).thenReturn(Optional.of(tourEntity));
        when(flyRepository.findById(flyId)).thenReturn(Optional.of(flyEntity));
        when(tourHelper.createTickets(any(), any())).thenReturn(newTickets);
        when(tourRepository.save(any(TourEntity.class))).thenReturn(updatedTourEntity);
        when(tourMapper.toTourResponseDto(updatedTourEntity)).thenReturn(tourResponseDto);

        // When
        ApiResponseDto<TourResponseDto> response = tourService.addTicket(flyId, tourId);

        // Then
        assertNotNull(response);
        assertEquals("success", response.getStatus());
        assertEquals("Ticket added successfully", response.getMessage());
        assertNotNull(response.getData());
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
    void testRemoveTicket() {
        // Given
        Long tourId = 1L;
        UUID ticketId = UUID.randomUUID();

        // Create customer entity
        CustomerEntity customer = CustomerEntity.builder()
                .dni(UUID.randomUUID().toString())
                .fullName("Test Customer")
                .build();

        // Create ticket to be removed
        TicketEntity ticket = TicketEntity.builder()
                .id(ticketId)
                .customer(customer)
                .price(BigDecimal.valueOf(100))
                .purchaseDate(LocalDate.now())
                .departureDate(LocalDateTime.now().plusDays(1))
                .arrivalDate(LocalDateTime.now().plusDays(1))
                .build();

        // Create another ticket that will remain
        TicketEntity remainingTicket = TicketEntity.builder()
                .id(UUID.randomUUID())
                .customer(customer)
                .price(BigDecimal.valueOf(150))
                .purchaseDate(LocalDate.now())
                .departureDate(LocalDateTime.now().plusDays(2))
                .arrivalDate(LocalDateTime.now().plusDays(2))
                .build();

        // Create initial tickets set with both tickets
        Set<TicketEntity> initialTickets = new HashSet<>();
        initialTickets.add(ticket);
        initialTickets.add(remainingTicket);

        // Create tour entity with both tickets
        TourEntity tourEntity = TourEntity.builder()
                .id(tourId)
                .customer(customer)
                .tickets(initialTickets)
                .reservations(new HashSet<>())
                .build();

        // Create updated tour with only the remaining ticket
        Set<TicketEntity> updatedTickets = new HashSet<>();
        updatedTickets.add(remainingTicket);

        TourEntity updatedTourEntity = TourEntity.builder()
                .id(tourId)
                .customer(customer)
                .tickets(updatedTickets)
                .reservations(new HashSet<>())
                .build();

        // Create expected response
        TourResponseDto tourResponseDto = TourResponseDto.builder()
                .id(tourId)
                .ticketsIds(updatedTickets.stream().map(TicketEntity::getId).collect(Collectors.toSet()))
                .reservationIds(new HashSet<>())
                .build();

        // Mock repository responses
        when(tourRepository.findById(tourId)).thenReturn(Optional.of(tourEntity));
        when(tourRepository.save(any(TourEntity.class))).thenReturn(updatedTourEntity);
        when(tourMapper.toTourResponseDto(updatedTourEntity)).thenReturn(tourResponseDto);

        // When
        ApiResponseDto<TourResponseDto> response = tourService.removeTicket(ticketId, tourId);

        // Then
        assertNotNull(response);
        assertEquals("success", response.getStatus());
        assertEquals("Ticket removed successfully", response.getMessage());
        assertNotNull(response.getData());

        // Verify interactions
        verify(tourRepository).findById(tourId);
        verify(tourRepository).save(any(TourEntity.class));
        verify(tourMapper).toTourResponseDto(updatedTourEntity);

        // Verify the ticket was removed from the response
        assertFalse(response.getData().getTicketsIds().contains(ticketId));
    }

    @Test
    void testAddReservation() {
        // Given
        Long tourId = 1L;
        UUID reservationId = UUID.randomUUID();

        CustomerEntity customer = CustomerEntity.builder()
                .dni(UUID.randomUUID().toString())
                .fullName("Test Customer")
                .build();

        HotelEntity hotel = HotelEntity.builder()
                .id(1L)
                .name("Test Hotel")
                .address("Test Address")
                .rating(5)
                .price(BigDecimal.valueOf(100))
                .build();

        // Reserva ya existente
        ReservationEntity existingReservation = ReservationEntity.builder()
                .id(UUID.randomUUID())
                .hotel(hotel)
                .customer(customer)
                .dateTimeReservation(LocalDateTime.now())
                .dateStart(LocalDate.now())
                .dateEnd(LocalDate.now().plusDays(3))
                .totalDays(3)
                .price(BigDecimal.valueOf(300))
                .build();
        Set<ReservationEntity> existingReservations = new HashSet<>();
        existingReservations.add(existingReservation);

        // Tour con reservas existentes
        TourEntity tourEntity = TourEntity.builder()
                .id(tourId)
                .customer(customer)
                .tickets(new HashSet<>())
                .reservations(existingReservations)
                .build();

        // Nueva reserva que se va a añadir
        ReservationEntity newReservation = ReservationEntity.builder()
                .id(reservationId)
                .hotel(hotel)
                .customer(customer)
                .dateTimeReservation(LocalDateTime.now())
                .dateStart(LocalDate.now().plusDays(5))
                .dateEnd(LocalDate.now().plusDays(10))
                .totalDays(5)
                .price(BigDecimal.valueOf(500))
                .build();

        // Tour esperado después de agregar la nueva reserva
        Set<ReservationEntity> updatedReservations = new HashSet<>(existingReservations);
        updatedReservations.add(newReservation);

        TourEntity updatedTourEntity = TourEntity.builder()
                .id(tourId)
                .customer(customer)
                .tickets(new HashSet<>())
                .reservations(updatedReservations)
                .build();

        // DTO esperado
        TourResponseDto tourResponseDto = TourResponseDto.builder()
                .id(tourId)
                .ticketsIds(new HashSet<>())
                .reservationIds(updatedReservations.stream().map(ReservationEntity::getId).collect(Collectors.toSet()))
                .build();

        // Mocks
        when(tourRepository.findById(tourId)).thenReturn(Optional.of(tourEntity));
        when(reservationRepository.findById(reservationId)).thenReturn(Optional.of(newReservation));
        // Al guardar, simulamos que devuelve el tour con la nueva reserva añadida
        when(tourRepository.save(any(TourEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(tourMapper.toTourResponseDto(any(TourEntity.class))).thenReturn(tourResponseDto);

        // When
        ApiResponseDto<TourResponseDto> response = tourService.addReservation(reservationId, tourId);

        // Then
        assertNotNull(response);
        assertEquals("success", response.getStatus());
        assertEquals("Reservation added successfully", response.getMessage());
        assertNotNull(response.getData());

        // Verificar que la nueva reserva fue añadida a la entidad tour
        Set<UUID> reservationIdsInTour = tourEntity.getReservations().stream()
                .map(ReservationEntity::getId)
                .collect(Collectors.toSet());
        assertTrue(reservationIdsInTour.contains(reservationId), "La nueva reserva debe estar en el tour");

        // Verificar bidireccionalidad: la reserva apunta al tour
        assertEquals(tourEntity, newReservation.getTour());

        // Verificar que el response contiene la nueva reserva
        assertTrue(response.getData().getReservationIds().contains(reservationId));

        // Verificar interacciones
        verify(tourRepository).findById(tourId);
        verify(reservationRepository).findById(reservationId);
        verify(tourRepository).save(any(TourEntity.class));
        verify(tourMapper).toTourResponseDto(any(TourEntity.class));
    }


    @Test
    void testRemoveReservation() {
        // Given
        Long tourId = 1L;
        UUID reservationIdToRemove = UUID.randomUUID();
        UUID remainingReservationId = UUID.randomUUID();

        // Create customer entity
        CustomerEntity customer = CustomerEntity.builder()
                .dni(UUID.randomUUID().toString())
                .fullName("Test Customer")
                .build();

        // Create hotel entity
        HotelEntity hotel = HotelEntity.builder()
                .id(1L)
                .name("Test Hotel")
                .address("Test Address")
                .rating(5)
                .price(BigDecimal.valueOf(100))
                .build();

        // Create reservations
        Set<ReservationEntity> initialReservations = new HashSet<>();

        // Reservation to be removed
        ReservationEntity reservationToRemove = ReservationEntity.builder()
                .id(reservationIdToRemove)
                .hotel(hotel)
                .customer(customer)
                .dateTimeReservation(LocalDateTime.now())
                .dateStart(LocalDate.now())
                .dateEnd(LocalDate.now().plusDays(3))
                .totalDays(3)
                .price(BigDecimal.valueOf(300))
                .build();
        initialReservations.add(reservationToRemove);

        // Reservation that will remain
        ReservationEntity remainingReservation = ReservationEntity.builder()
                .id(remainingReservationId)
                .hotel(hotel)
                .customer(customer)
                .dateTimeReservation(LocalDateTime.now())
                .dateStart(LocalDate.now().plusDays(5))
                .dateEnd(LocalDate.now().plusDays(10))
                .totalDays(5)
                .price(BigDecimal.valueOf(500))
                .build();
        initialReservations.add(remainingReservation);

        // Create tour entity with both reservations
        TourEntity tourEntity = TourEntity.builder()
                .id(tourId)
                .customer(customer)
                .tickets(new HashSet<>())
                .reservations(initialReservations)
                .build();

        // Create updated tour with only the remaining reservation
        Set<ReservationEntity> updatedReservations = new HashSet<>();
        updatedReservations.add(remainingReservation);

        TourEntity updatedTourEntity = TourEntity.builder()
                .id(tourId)
                .customer(customer)
                .tickets(new HashSet<>())
                .reservations(updatedReservations)
                .build();

        // Create expected response
        TourResponseDto tourResponseDto = TourResponseDto.builder()
                .id(tourId)
                .ticketsIds(new HashSet<>())
                .reservationIds(updatedReservations.stream().map(ReservationEntity::getId).collect(Collectors.toSet()))
                .build();

        // Mock repository responses
        when(tourRepository.findById(tourId)).thenReturn(Optional.of(tourEntity));
        when(tourRepository.save(any(TourEntity.class))).thenReturn(updatedTourEntity);
        when(tourMapper.toTourResponseDto(updatedTourEntity)).thenReturn(tourResponseDto);

        // When
        ApiResponseDto<TourResponseDto> response = tourService.removeReservation(reservationIdToRemove, tourId);

        // Then
        assertNotNull(response);
        assertEquals("success", response.getStatus());
        assertEquals("Reservation removed successfully", response.getMessage());
        assertNotNull(response.getData());

        // Verify interactions
        verify(tourRepository).findById(tourId);
        verify(tourRepository).save(any(TourEntity.class));
        verify(tourMapper).toTourResponseDto(updatedTourEntity);

        // Verify the reservation was removed from the response
        assertFalse(response.getData().getReservationIds().contains(reservationIdToRemove));
        assertTrue(response.getData().getReservationIds().contains(remainingReservationId));
    }
}
