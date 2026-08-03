package com.joirv.CursoSpringBoot.infraestructure.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.joirv.CursoSpringBoot.api.models.responses.ApiResponseDto;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtCustomerFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper;
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;


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

       String token = authorizationHeader.substring(7);
       String username = jwtService.extractUsername(token);

       if(username != null && SecurityContextHolder.getContext().getAuthentication() == null){

           UserDetails userDetails = userDetailsService.loadUserByUsername(username);

           if(jwtService.isTokenValid(token,userDetails)){

               UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
               SecurityContextHolder.getContext().setAuthentication(authenticationToken);
           }
       }

       filterChain.doFilter(request, response);
    }

    /**
     * Can be overridden in subclasses for custom filtering control,
     * returning {@code true} to avoid filtering of the given request.
     * <p>The default implementation always returns {@code false}.
     *
     * @param request current HTTP request
     * @return whether the given request should <i>not</i> be filtered
     * @throws ServletException in case of errors
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getServletPath();
        return path.startsWith("/auth/login") || path.startsWith("/users/create") || path.startsWith("/error");
    }
}
