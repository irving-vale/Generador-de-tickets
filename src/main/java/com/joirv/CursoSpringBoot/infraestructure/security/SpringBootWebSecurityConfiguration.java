package com.joirv.CursoSpringBoot.infraestructure.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.server.SecurityWebFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SpringBootWebSecurityConfiguration {

    @Bean
   SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
       http.authorizeHttpRequests((requests) -> requests
               .requestMatchers("/api/v1/fly/**").authenticated()
               .requestMatchers("/error").permitAll());
       http.formLogin(flc ->flc.disable());
       http.httpBasic(withDefaults());
       return http.build();
   }
}
