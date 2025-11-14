package com.newbank.userservice.service;

import com.newbank.userservice.dto.UserDTO;
import com.newbank.userservice.exception.BusinessException;
import com.newbank.userservice.model.UserEntity;
import com.newbank.userservice.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserService {

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    UserRepository repository;

    public UserService(UserRepository repository){
        this.repository = repository;
    }

    public Boolean userExists(String email) {
        return !repository.findByEmail(email).isEmpty();
    }

    public UserDTO createUser(UserDTO userDTO){

        if(userExists(userDTO.getEmail())){
            throw new BusinessException("El usuario ya existe");
        };

        UserEntity userEntity = new UserEntity();
        userEntity.setName(userDTO.getName());
        userEntity.setEmail(userDTO.getEmail());
        userEntity.setCreated(LocalDateTime.now());
        userEntity.setLastLogin(LocalDateTime.now());
        userEntity.setActive(true);

        String encodedPassword = passwordEncoder.encode(userDTO.getPassword());
        userEntity.setPassword(encodedPassword);

        repository.save(userEntity);

        return UserDTO.fromEntity(userEntity);

    }
    public UserDTO updateUserLastLogin(String email) {

        UserEntity userEntity =  repository.findByEmail(email).orElseThrow( () ->
            new BusinessException("Usuario no encontrado"));

        userEntity.setLastLogin(LocalDateTime.now());

        repository.save(userEntity);

        return UserDTO.fromEntity(userEntity);
    }
}
