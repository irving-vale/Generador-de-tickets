package com.joirv.CursoSpringBoot.infraestructure.services;

import com.joirv.CursoSpringBoot.api.models.request.LoginRequestDto;
import com.joirv.CursoSpringBoot.api.models.responses.ApiResponseDto;
import com.joirv.CursoSpringBoot.infraestructure.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public ApiResponseDto<String> loginService(LoginRequestDto loginRequestDto){

        // 1. Autenticamos el usuario
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequestDto.getEmail(),
                        loginRequestDto.getPwd()
                )
        );

        // 2. Extraemos el UserDetails autenticado
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        // 3. Generamos el token JWT usando tu JwtService
        String jwtToken = jwtService.generateToken(userDetails);
        // 4. Retornamos la respuesta con el token
        return ApiResponseDto.<String>builder()
                .status("success")
                .statusCode(200)
                .message("Login successful")
                .data(jwtToken)
                .build();

    }
}
