package com.joirv.CursoSpringBoot.infraestructure.helper;

import com.joirv.CursoSpringBoot.domain.entities.*;
import com.joirv.CursoSpringBoot.domain.repositories.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Transactional
@Component
@AllArgsConstructor
public class EntityLoader {
    private final CustomerRepository customerRepository;
    private final FlyRepository flyRepository;
    private final HotelRepository hotelRepository;
    private final TourRepository tourRepository;
    private final ReservationRepository reservationRepository;

    public CustomerEntity findCustumerEntityById(String id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Customer not found with id: " + id));
    }

    public FlyEntity findFlyEntityById(Long id) {
        return flyRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Fly not found with id: " + id));
    }

    public HotelEntity findHotelEntityById(Long id) {
        return hotelRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Hotel not found with id: " + id));
    }

    public TourEntity findTourEntityById(Long id) {
        return this.tourRepository .findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tour not found with id: " + id));
    }

    public ReservationEntity findReservationEntityById(UUID id) {
        return this.reservationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Reservation not found with id: " + id));
    }

}
