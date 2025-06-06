package com.joirv.CursoSpringBoot.api.models.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Set;

@NoArgsConstructor
@Data
@AllArgsConstructor
@Builder
public class TourRequestDto implements Serializable {
    private String idClient;
    private Set<TourFlyRequestDto> idFlys;
    private Set<TourHotelRequest> idHotels;

}
