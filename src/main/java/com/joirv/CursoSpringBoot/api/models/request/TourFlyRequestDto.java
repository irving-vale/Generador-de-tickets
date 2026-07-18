package com.joirv.CursoSpringBoot.api.models.request;

import lombok.*;

import java.io.Serializable;

@NoArgsConstructor
@Getter
@AllArgsConstructor
@Builder
public class TourFlyRequestDto implements Serializable {
    private Long idFly;

}
