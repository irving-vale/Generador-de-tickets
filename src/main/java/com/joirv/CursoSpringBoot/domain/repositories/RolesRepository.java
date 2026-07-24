package com.joirv.CursoSpringBoot.domain.repositories;

import com.joirv.CursoSpringBoot.domain.entities.RolesEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RolesRepository extends JpaRepository<RolesEntity,Long> {
}
