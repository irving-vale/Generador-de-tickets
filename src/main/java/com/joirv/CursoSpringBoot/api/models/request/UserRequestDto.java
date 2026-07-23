package com.joirv.CursoSpringBoot.api.models.request;

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
    private String role;
}
