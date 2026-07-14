package com.joirv.CursoSpringBoot.domain.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@NoArgsConstructor
@Data
@AllArgsConstructor
@Builder
public class UsersEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Generate automatically ID Progressive
    @Column(name = "id")
    private Long id;
    @Column(name = "email")
    private String email;
    @Column(name = "pwd")
    private String pwd;
    @Column(name = "role")
    private String role;

}
