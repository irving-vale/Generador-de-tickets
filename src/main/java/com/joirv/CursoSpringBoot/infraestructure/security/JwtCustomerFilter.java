package com.joirv.CursoSpringBoot.infraestructure.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.joirv.CursoSpringBoot.api.models.responses.ApiResponseDto;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtCustomerFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper;


    /**
     * Same contract as for {@code doFilter}, but guaranteed to be
     * just invoked once per request within a single request thread.
     * See {@link #shouldNotFilterAsyncDispatch()} for details.
     * <p>Provides HttpServletRequest and HttpServletResponse arguments instead of the
     * default ServletRequest and ServletResponse ones.
     *
     * @param request
     * @param response
     * @param filterChain
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

       String authorizationHeader = request.getHeader("Authorization");
       if(authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")){
           ApiResponseDto<Void> responseDto = ApiResponseDto.<Void>builder()
                   .status("error")
                   .message("JWT token is required")
                   .statusCode(HttpServletResponse.SC_UNAUTHORIZED)
                   .data(null)
                   .build();
           response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
           response.setContentType("application/json");
           response.getWriter().write(objectMapper.writeValueAsString(responseDto));
           return;
       }

       filterChain.doFilter(request, response);
    }
}
