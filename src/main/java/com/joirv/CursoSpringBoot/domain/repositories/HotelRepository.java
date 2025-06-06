package com.joirv.CursoSpringBoot.domain.repositories;

import com.joirv.CursoSpringBoot.domain.entities.CustomerEntity;
import com.joirv.CursoSpringBoot.domain.entities.HotelEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface HotelRepository extends JpaRepository<HotelEntity,Long> {
}
