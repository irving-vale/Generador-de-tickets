package com.joirv.CursoSpringBoot.domain.entities;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

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
    @Column(name = "email", unique = true , nullable = false)
    private String email;
    @Column(name = "pwd", nullable = false)
    private String pwd;
    @Column(name = "enabled")
    private  Boolean enabled = true;
    @Column(name = "created_at")
    private Timestamp createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", nullable = false)
    private RolesEntity roles;




}
