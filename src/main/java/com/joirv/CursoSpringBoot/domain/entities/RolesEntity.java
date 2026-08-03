package com.joirv.CursoSpringBoot.domain.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity(name = "roles")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class RolesEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 50, nullable = false)
    private String roleName;


    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "role_authorities"
            , joinColumns = @JoinColumn(name = "role_id")
            , inverseJoinColumns = @JoinColumn(name = "authority_id")
    )
    private Set<AuthoritiesEntity> authorities= new HashSet<>();
}
