package com.joirv.CursoSpringBoot.api.models.request;

import lombok.*;

import java.io.Serializable;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
@Builder
public class TourFlyRequestDto implements Serializable {
    private Long idFly;

}
