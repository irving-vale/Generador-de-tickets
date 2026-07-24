package com.joirv.CursoSpringBoot.domain.repositories;

import com.joirv.CursoSpringBoot.domain.entities.RolesEntity;
import com.joirv.CursoSpringBoot.domain.entities.UsersEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface UserRepository extends CrudRepository<UsersEntity,Long> {

    Optional<UsersEntity> findByEmail(String email);
}
