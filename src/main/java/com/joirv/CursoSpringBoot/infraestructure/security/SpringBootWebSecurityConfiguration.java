package com.joirv.CursoSpringBoot.infraestructure.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
@RequiredArgsConstructor
public class SpringBootWebSecurityConfiguration {



    private final JwtCustomerFilter jwtCustomerFilter;

    @Bean
   SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        // 1. CONFIGURATION OF CORS
        http.cors(cors -> cors.configurationSource(request -> {
            var config = new CorsConfiguration();
            config.setAllowedOrigins(List.of("http://localhost:4200")); // origin frontend
            config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
            config.setAllowedHeaders(List.of("*"));
            config.setAllowCredentials(true);
            return config;
        }));

        // CONFIGURATION OF CSRF
        http.csrf(csrf -> csrf.disable());

        // CONFIGURATION OF SESSION STATEless
        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // CONFIGURATION OF AUTHORIZATION
       http.authorizeHttpRequests((requests) -> requests
               .requestMatchers(HttpMethod.GET,"/api/v1/fly/**").hasAuthority("read")
               .requestMatchers("/error").permitAll()
                       .requestMatchers(HttpMethod.POST,"/api/v1/auth/login").permitAll()
               .requestMatchers(HttpMethod.POST,"/api/v1/users/create").permitAll().anyRequest().authenticated())
                       .addFilterBefore(jwtCustomerFilter, UsernamePasswordAuthenticationFilter.class);
       return http.build();
   }

/*    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }*/

//Para Registrar Usuarios se puede usar inyectado en servicios como en UserCreateServices
    @Bean
    public PasswordEncoder passwordEncoder(){
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception{
        return config.getAuthenticationManager();
    }
}
