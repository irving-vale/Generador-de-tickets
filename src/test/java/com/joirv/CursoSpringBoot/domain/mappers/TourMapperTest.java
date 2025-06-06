package com.joirv.CursoSpringBoot.domain.mappers;

import com.joirv.CursoSpringBoot.api.models.responses.TourResponseDto;
import com.joirv.CursoSpringBoot.domain.entities.CustomerEntity;
import com.joirv.CursoSpringBoot.domain.entities.FlyEntity;
import com.joirv.CursoSpringBoot.domain.entities.HotelEntity;
import com.joirv.CursoSpringBoot.domain.entities.ReservationEntity;
import com.joirv.CursoSpringBoot.domain.entities.TicketEntity;
import com.joirv.CursoSpringBoot.domain.entities.TourEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class TourMapperTest {

    @Spy
    private TourMapperImpl tourMapper;

    @Test
    void testToTourResponseDto_withCompleteEntity() {
        // Given
        UUID ticketId = UUID.randomUUID();
        UUID reservationId = UUID.randomUUID();
        Long tourId = 1L;
        
        // Create a customer entity
        CustomerEntity customer = CustomerEntity.builder()
                .dni(UUID.randomUUID().toString())
                .fullName("John Doe")
                .creditCard("1234-5678-9012-3456")
                .phoneNumber("123456789")
                .totalFlights(0)
                .totalLodgings(0)
                .totalTours(0)
                .build();
        
        // Create a fly entity
        FlyEntity fly = FlyEntity.builder()
                .id(1L)
                .price(BigDecimal.valueOf(100))
                .originName("Madrid")
                .destinyName("Barcelona")
                .originLat(40.4168)
                .originLng(-3.7038)
                .destinyLat(41.3851)
                .destinyLng(2.1734)
                .build();
        
        // Create a hotel entity
        HotelEntity hotel = HotelEntity.builder()
                .id(1L)
                .name("Test Hotel")
                .address("Test Address")
                .rating(4)
                .price(BigDecimal.valueOf(200))
                .build();
        
        // Create a ticket entity
        TicketEntity ticket = TicketEntity.builder()
                .id(ticketId)
                .fly(fly)
                .customer(customer)
                .price(fly.getPrice().multiply(BigDecimal.valueOf(1.25)))
                .purchaseDate(LocalDate.now())
                .arrivalDate(LocalDateTime.now())
                .departureDate(LocalDateTime.now())
                .build();
        
        // Create a reservation entity
        ReservationEntity reservation = ReservationEntity.builder()
                .id(reservationId)
                .hotel(hotel)
                .customer(customer)
                .dateTimeReservation(LocalDateTime.now())
                .dateStart(LocalDate.now())
                .dateEnd(LocalDate.now().plusDays(5))
                .totalDays(5)
                .price(hotel.getPrice().multiply(BigDecimal.valueOf(1.25)))
                .build();
        
        // Create sets for tickets and reservations
        Set<TicketEntity> tickets = new HashSet<>();
        tickets.add(ticket);
        
        Set<ReservationEntity> reservations = new HashSet<>();
        reservations.add(reservation);
        
        // Create a tour entity
        TourEntity tourEntity = TourEntity.builder()
                .id(tourId)
                .customer(customer)
                .tickets(tickets)
                .reservations(reservations)
                .build();
        
        // When
        TourResponseDto result = tourMapper.toTourResponseDto(tourEntity);
        
        // Then
        assertNotNull(result);
        assertEquals(tourId, result.getId());
        assertEquals(1, result.getTicketsIds().size());
        assertEquals(1, result.getReservationIds().size());
        assertTrue(result.getTicketsIds().contains(ticketId));
        assertTrue(result.getReservationIds().contains(reservationId));
    }
    
    @Test
    void testToTourResponseDto_withNullCollections() {
        // Given
        Long tourId = 1L;
        
        // Create a tour entity with null collections
        TourEntity tourEntity = TourEntity.builder()
                .id(tourId)
                .customer(CustomerEntity.builder().dni(UUID.randomUUID().toString()).build())
                .tickets(null)
                .reservations(null)
                .build();
        
        // When
        TourResponseDto result = tourMapper.toTourResponseDto(tourEntity);
        
        // Then
        assertNotNull(result);
        assertEquals(tourId, result.getId());
        assertNotNull(result.getTicketsIds());
        assertNotNull(result.getReservationIds());
        assertTrue(result.getTicketsIds().isEmpty());
        assertTrue(result.getReservationIds().isEmpty());
    }
    
    @Test
    void testToTicketIds_withValidTickets() {
        // Given
        UUID ticketId1 = UUID.randomUUID();
        UUID ticketId2 = UUID.randomUUID();
        
        Set<TicketEntity> tickets = new HashSet<>();
        tickets.add(TicketEntity.builder().id(ticketId1).build());
        tickets.add(TicketEntity.builder().id(ticketId2).build());
        
        // When
        Set<UUID> result = tourMapper.toTicketIds(tickets);
        
        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(ticketId1));
        assertTrue(result.contains(ticketId2));
    }
    
    @Test
    void testToTicketIds_withNullTickets() {
        // When
        Set<UUID> result = tourMapper.toTicketIds(null);
        
        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
    
    @Test
    void testToReservationIds_withValidReservations() {
        // Given
        UUID reservationId1 = UUID.randomUUID();
        UUID reservationId2 = UUID.randomUUID();
        
        Set<ReservationEntity> reservations = new HashSet<>();
        reservations.add(ReservationEntity.builder().id(reservationId1).build());
        reservations.add(ReservationEntity.builder().id(reservationId2).build());
        
        // When
        Set<UUID> result = tourMapper.toReservationIds(reservations);
        
        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(reservationId1));
        assertTrue(result.contains(reservationId2));
    }
    
    @Test
    void testToReservationIds_withNullReservations() {
        // When
        Set<UUID> result = tourMapper.toReservationIds(null);
        
        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}