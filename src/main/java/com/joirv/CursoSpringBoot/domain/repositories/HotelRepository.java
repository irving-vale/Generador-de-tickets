package com.joirv.CursoSpringBoot.domain.repositories;

import com.joirv.CursoSpringBoot.domain.entities.HotelEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HotelRepository extends JpaRepository<HotelEntity,Long> {
}
