package com.joirv.CursoSpringBoot.infraestructure.services;

import com.joirv.CursoSpringBoot.api.models.request.UserRequestDto;
import com.joirv.CursoSpringBoot.api.models.responses.ApiResponseDto;
import com.joirv.CursoSpringBoot.domain.entities.UsersEntity;
import com.joirv.CursoSpringBoot.domain.mappers.UserMapper;
import com.joirv.CursoSpringBoot.domain.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserCreateServices {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Transactional
   public ApiResponseDto<String> savedUserPasswordEncoder(UserRequestDto userDto){
        UsersEntity userEntity = userMapper.toEntity(userDto);
//        Optional<UsersEntity> users = userRepository.findByEmail(userEntity.getEmail());
//        if(users.isPresent()){
//            return ApiResponseDto.<String>builder()
//                    .status("error")
//                    .statusCode(409)
//                    .data(null)
//                    .message("El correo electrónico ya se encuentra registrado")
//                    .build();
//        }
       userRepository.findByEmail(userEntity.getEmail())
                .ifPresent(user ->{
                    throw new DuplicateKeyException("El correo ya se encuentra registrado");
                } );

        String pwd = passwordEncoder.encode(userEntity.getPwd());
        userEntity.setPwd(pwd);
        UsersEntity savedUser = userRepository.save(userEntity);

        return ApiResponseDto.<String>builder()
                .status("success")
                .message("Usuario Registrado Exitosamente")
                .statusCode(201)
                .data(savedUser.getId().toString())
                .build();
    }
}
