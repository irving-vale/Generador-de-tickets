package com.joirv.CursoSpringBoot.infraestructure.helper;

import com.joirv.CursoSpringBoot.domain.repositories.CustomerRepository;
import com.joirv.CursoSpringBoot.infraestructure.exceptions.personalExceptions.CustomerCounterException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Transactional
@Component
@AllArgsConstructor
public class CustumerIncreasTicketsAndReservations {

    private final CustomerRepository customerRepository;
    private final EntityLoader entityLoader;

    public void increaseTicketsAndReservations(String customerId, Class<?> entityType) {
       var custumerUpdate = this.entityLoader.findCustumerEntityById(customerId);
       switch (entityType.getSimpleName()) {
           case "TourService" -> custumerUpdate.setTotalTours(custumerUpdate.getTotalTours() + 1);
           case "TicketService" -> custumerUpdate.setTotalFlights(custumerUpdate.getTotalFlights() + 1);
           case "ReservationService" -> custumerUpdate.setTotalLodgings(custumerUpdate.getTotalLodgings() + 1);
           default -> throw new CustomerCounterException("Unsupported entity type: " + entityType.getSimpleName());
       }
         this.customerRepository.save(custumerUpdate);


    }
}
