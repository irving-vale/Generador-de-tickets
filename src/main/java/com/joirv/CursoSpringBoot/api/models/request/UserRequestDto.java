package com.joirv.CursoSpringBoot.api.models.request;

import com.joirv.CursoSpringBoot.domain.entities.RolesEntity;
import jakarta.persistence.Column;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserRequestDto {

    private String email;
    private String pwd;
    private Boolean enabled = true;
    private Long role;
}
