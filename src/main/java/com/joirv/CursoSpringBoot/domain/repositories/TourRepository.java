package com.joirv.CursoSpringBoot.domain.repositories;

import com.joirv.CursoSpringBoot.domain.entities.TourEntity;
import org.springframework.data.repository.CrudRepository;

public interface TourRepository extends CrudRepository<TourEntity, Long> {

}
