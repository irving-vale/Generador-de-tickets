package com.joirv.CursoSpringBoot.infraestructure.services;

import com.joirv.CursoSpringBoot.api.models.responses.ApiResponseDto;
import com.joirv.CursoSpringBoot.api.models.responses.FlyResponseDto;
import com.joirv.CursoSpringBoot.api.models.responses.Meta;
import com.joirv.CursoSpringBoot.domain.entities.FlyEntity;
import com.joirv.CursoSpringBoot.domain.mappers.FlyMapper;
import com.joirv.CursoSpringBoot.domain.repositories.FlyRepository;
import com.joirv.CursoSpringBoot.infraestructure.abstract_services.IFlyService;
import com.joirv.CursoSpringBoot.util.SortType;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

@Transactional
@Service
@Slf4j
@AllArgsConstructor
public class FlyService implements IFlyService {


	private final FlyRepository flyRepository;
	private final FlyMapper flyMapper;

	@Override
	public ApiResponseDto<List<FlyResponseDto>> findAllPagination(Integer page, Integer size, SortType sortType) {
	Sort sortValue =sort(sortType);

		Pageable pageable = PageRequest.of(page - 1, size, sortValue );
		Page<FlyEntity> fly = flyRepository.findAll(pageable);
		log.info("Vuelos obtenidos de la busqueda : {}",fly.getContent());
		if (fly.isEmpty()) {
			return ApiResponseDto.<List<FlyResponseDto>>builder()
					.status("success")
					.message("No Flys found")
					.data(List.of())
					.statusCode(200)
					.meta(Meta.builder()
							.totalItems(0L)
							.totalPages(0)
							.currentPage(page - 1)
							.pageSize(size)
							.build())
					.build();
		}
		log.info("Flys retrieved successfully: {}", fly.getNumberOfElements());
		Page<FlyResponseDto> flyToDto = fly.map(flyMapper::toDto);
		log.info("Flys converted to DTOs successfully: {}", flyToDto.getContent());

		return ApiResponseDto.<List<FlyResponseDto>>builder()
				.status("success")
				.message("Flys retrieved successfully")
				.data(flyToDto.getContent())
		        		.statusCode(200)
		       		 .meta(Meta.builder()
			      		.totalItems(flyToDto.getTotalElements())
			     		 .totalPages(flyToDto.getTotalPages())
				           .currentPage(flyToDto.getNumber())
			     		 .pageSize(flyToDto.getSize())
					.build())
		        		.build();

	}



	@Override
	public ApiResponseDto<List<FlyResponseDto>> readLessPrice(Integer page, Integer size, SortType sortType,BigDecimal price) {
		Sort sortValue =sort(sortType);
		Pageable pageable = PageRequest.of(page - 1, size, sortValue );
		var flyCheap = flyRepository.findByPriceLessThanEqual(pageable, price);
		if (flyCheap.isEmpty()) {
			log.warn("No Flys found with price less than or equal to: {}", price);
			return ApiResponseDto.<List<FlyResponseDto>>builder()
					.status("success")
					.message("No Flys found with price less than or equal to " + price)
					.data(List.of())
					.statusCode(200)
					.meta(Meta.builder()
							.totalItems(0L)
							.totalPages(0)
							.currentPage(page - 1)
							.pageSize(size)
							.build())
					.build();
		}
		log.info("Flys retrieved successfully with price less than or equal to: {}", price);
		Page<FlyResponseDto> flyToDto = flyCheap.map(flyMapper::toDto);
		log.info("Flys converted to DTOs successfully: {}", flyToDto);
				return ApiResponseDto.<List<FlyResponseDto>>builder()
				.status("success")
				.message("Flys retrieved successfully with price less than or equal to " + price)
				.data(flyToDto.getContent())
				.statusCode(200)
				.meta(Meta.builder()
						.totalItems(flyToDto.getTotalElements())
						.totalPages(flyToDto.getTotalPages())
						.currentPage(flyToDto.getNumber())
						.pageSize(flyToDto.getSize())
						.build())
				.build();

	}


	@Override
	public ApiResponseDto<List<FlyResponseDto>> readBetweenPrice(BigDecimal min, BigDecimal max) {
		var flyEntity = flyRepository.findByPriceBetween(min, max).stream()
		        .map(flyMapper::toDto)
			      .collect(toList());
		log.info("Flys found between prices: {}", flyEntity);
		if (flyEntity.isEmpty()) {
			log.warn("No Flys found between prices: {} and {}", min, max);
			return ApiResponseDto.<List<FlyResponseDto>>builder()
					.status("success")
					.message("No Flys found between prices " + min + " and " + max)
					.data(List.of())
					.statusCode(200)
					.meta(Meta.builder()
							.totalItems(0L)
							.totalPages(0)
							.currentPage(0)
							.pageSize(0)
							.build())
					.build();
		}

		return ApiResponseDto.<List<FlyResponseDto>>builder()
				.status("success")
				.message("Flys found between prices " + min + " and " + max)
				.data(flyEntity)
				.statusCode(200)
				.meta(Meta.builder()
						.totalItems((long) flyEntity.size())
						.totalPages(1)
						.currentPage(1)
						.pageSize(1)
						.build())
				.build();
	}


	@Override
	public ApiResponseDto<Set<FlyResponseDto>> readByOriginDestiny(String origin, String destiny) {
		String originUpper = origin.substring(0,1).toUpperCase() + origin.substring(1).toLowerCase();
		String destinyUpper = destiny.substring(0,1).toUpperCase() + destiny.substring(1).toLowerCase();
		var fly = flyRepository.findByOriginNameAndDestinyName(originUpper, destinyUpper)
				.stream()
				.map(flyMapper::toDto)
				.collect(toSet());
		log.info("Flys found with origin: {}", fly );
		if (fly.isEmpty()) {
			log.warn("No Flys found with origin: {} and destiny: {}", originUpper, destinyUpper);
			return ApiResponseDto.<Set<FlyResponseDto>>builder()
			        .status("success")
			        .message("No Flys found with origin: " + origin + " and destiny: " + destiny)
			        .data(Set.of())
			        .statusCode(200)
			        .meta(Meta.builder()
				      .totalItems(0L)
				      .totalPages(0)
				      .currentPage(0)
				      .pageSize(0)
				      .build())
			        .build();

		}
		log.info("Flys found with origin: {} and destiny: {}", originUpper, destinyUpper);
		return ApiResponseDto.<Set<FlyResponseDto>>builder()
		        .status("success")
		        .message("Flys found with origin: " + originUpper + " and destiny: " + destinyUpper)
		        .data(fly)
		        .statusCode(200)
		        .meta(Meta.builder()
			      .totalItems((long) fly.size())
			      .totalPages(1)
			      .currentPage(1)
			      .pageSize(1)
			      .build())
		        .build();
	}


	// metodo para ordenar los resultados de la consulta
	private Sort sort(SortType sortType) {
		Sort sort = Sort.by("price");
		if (sortType == SortType.LOWER) {
			sort = sort.ascending();
		} else if (sortType == SortType.UPPER) {
			sort = sort.descending();
		} else if (sortType == SortType.NONE) {
			sort = Sort.unsorted();
		}
		return sort;
	}

}
