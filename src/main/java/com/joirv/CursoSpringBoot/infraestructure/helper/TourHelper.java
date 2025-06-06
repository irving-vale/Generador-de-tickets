package com.joirv.CursoSpringBoot.infraestructure.helper;

import com.joirv.CursoSpringBoot.domain.entities.*;
import com.joirv.CursoSpringBoot.domain.repositories.ReservationRepository;
import com.joirv.CursoSpringBoot.domain.repositories.TicketRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Transactional
@Component
@AllArgsConstructor
public class TourHelper {
    private final TicketRepository ticketRepository;
    private final ReservationRepository reservationRepository;

    public Set<TicketEntity> createTickets(Set<FlyEntity> flights, CustomerEntity customer) {
        var response = new HashSet<TicketEntity>(flights.size());
        flights.forEach(flight -> {
            var ticket = TicketEntity.builder()
                    .id(UUID.randomUUID())
                    .fly(flight).customer(customer)
                    .price(calculatePriceWithTax(flight.getPrice()))
                    .purchaseDate(LocalDate.now())
                    .arrivalDate(LocalDateTime.now())
                    .departureDate(LocalDateTime.now())
                    .build();
            response.add(this.ticketRepository.save(ticket));

        });
        return response;
    }

    public Set<ReservationEntity> createReservations(HashMap<HotelEntity, Integer> hotels, CustomerEntity customer) {
        var response = new HashSet<ReservationEntity>(hotels.size());
        hotels.forEach((hotel, days) -> {
            var reservation = ReservationEntity.builder()
                    .id(UUID.randomUUID())
                    .dateTimeReservation(LocalDateTime.now())
                    .dateStart(LocalDate.now())
                    .dateEnd(LocalDate.now().plusDays(days))
                    .totalDays(days)
                    .price(calculatePriceWithTax(hotel.getPrice())).hotel(hotel)
                    .customer(customer).build();
            response.add(this.reservationRepository.save(reservation));

        });
        return response;
    }


    private BigDecimal calculatePriceWithTax(BigDecimal basePrice) {
        return basePrice.add(basePrice.multiply(BigDecimal.valueOf(0.25)));
    }


}
