package com.joirv.CursoSpringBoot.domain.repositories;

import com.joirv.CursoSpringBoot.domain.entities.TicketEntity;
import com.joirv.CursoSpringBoot.domain.entities.TourEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface TourRepository extends CrudRepository<TourEntity, Long> {

}
