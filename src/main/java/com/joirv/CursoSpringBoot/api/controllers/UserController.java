package com.joirv.CursoSpringBoot.api.controllers;

import com.joirv.CursoSpringBoot.api.models.request.UserRequestDto;
import com.joirv.CursoSpringBoot.api.models.responses.ApiResponseDto;
import com.joirv.CursoSpringBoot.domain.entities.UsersEntity;
import com.joirv.CursoSpringBoot.infraestructure.services.UserCreateServices;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserCreateServices user;

    @PostMapping("/create")
    public ResponseEntity<ApiResponseDto<String>> UserCreate(@RequestBody UserRequestDto userDto) {
        return ResponseEntity.ok(user.savedUserPasswordEncoder(userDto));
    }


}
