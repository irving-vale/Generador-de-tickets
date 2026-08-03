package com.joirv.CursoSpringBoot.api.controllers;

import com.joirv.CursoSpringBoot.api.models.request.LoginRequestDto;
import com.joirv.CursoSpringBoot.api.models.responses.ApiResponseDto;
import com.joirv.CursoSpringBoot.infraestructure.security.JwtService;
import com.joirv.CursoSpringBoot.infraestructure.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class LoginController {

    private final AuthService authService;


        @PostMapping("/login")
        public ResponseEntity<ApiResponseDto<String>> login (@RequestBody LoginRequestDto loginRequestDto){
        return ResponseEntity.ok(authService.loginService(loginRequestDto));
  }

}
