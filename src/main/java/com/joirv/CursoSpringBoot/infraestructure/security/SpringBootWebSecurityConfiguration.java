package com.joirv.CursoSpringBoot.infraestructure.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
public class SpringBootWebSecurityConfiguration {

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

        // CONFIGURATION OF AUTHORIZATION
       http.authorizeHttpRequests((requests) -> requests
               .requestMatchers(HttpMethod.GET,"/api/v1/fly/**").hasAuthority("read")
               .requestMatchers("/error").permitAll()
               .requestMatchers(HttpMethod.POST,"/api/v1/users/create").permitAll().anyRequest().authenticated());
       http.formLogin(flc ->flc.disable());
       http.httpBasic(withDefaults());
       return http.build();
   }

/*    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }*/


    @Bean
    public PasswordEncoder passwordEncoder(){
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
