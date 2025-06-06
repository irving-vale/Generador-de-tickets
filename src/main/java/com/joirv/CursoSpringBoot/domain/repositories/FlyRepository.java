package com.joirv.CursoSpringBoot.domain.repositories;

import com.joirv.CursoSpringBoot.api.models.responses.ApiResponseDto;
import com.joirv.CursoSpringBoot.api.models.responses.FlyResponseDto;
import com.joirv.CursoSpringBoot.domain.entities.CustomerEntity;
import com.joirv.CursoSpringBoot.domain.entities.FlyEntity;
import com.joirv.CursoSpringBoot.util.SortType;
import org.springframework.data.domain.Limit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

public interface FlyRepository extends JpaRepository<FlyEntity,Long> {

	Page<FlyEntity> findAll(Pageable pageable);

	Page<FlyEntity> findByPriceLessThanEqual(Pageable pageable,BigDecimal price );

	List<FlyEntity> findByPriceBetween(BigDecimal min, BigDecimal max);

	Set<FlyEntity> findByOriginNameAndDestinyName(String origin, String destiny);
}
