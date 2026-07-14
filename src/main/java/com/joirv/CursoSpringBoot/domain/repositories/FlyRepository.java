package com.joirv.CursoSpringBoot.domain.repositories;

import com.joirv.CursoSpringBoot.domain.entities.FlyEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

public interface FlyRepository extends JpaRepository<FlyEntity,Long> {

	Page<FlyEntity> findAll(Pageable pageable);

	@Query(value = "SELECT f FROM fly f LEFT JOIN FETCH f.tickets",countQuery = "SELECT COUNT(f) FROM fly f")
	Page<FlyEntity> findAllMejorado(Pageable pageable);

	Page<FlyEntity> findByPriceLessThanEqual(Pageable pageable,BigDecimal price );

	List<FlyEntity> findByPriceBetween(BigDecimal min, BigDecimal max);

	Set<FlyEntity> findByOriginNameAndDestinyName(String origin, String destiny);
}
