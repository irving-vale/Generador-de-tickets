package com.joirv.CursoSpringBoot.infraestructure.abstract_services;

import com.joirv.CursoSpringBoot.api.models.responses.ApiResponseDto;
import com.joirv.CursoSpringBoot.util.SortType;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

public interface CatalogoService<R> {

   ApiResponseDto<List<R>>findAllPagination(Integer page, Integer size, SortType sortType);

   ApiResponseDto< List<R> >readLessPrice(Integer page, Integer size, SortType sortType, BigDecimal price);

   ApiResponseDto< List<R> > readBetweenPrice(BigDecimal min, BigDecimal max);

    String FIELD_BY_SORT = "price";
}
