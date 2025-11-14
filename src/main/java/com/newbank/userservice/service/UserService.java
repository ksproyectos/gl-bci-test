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

    private final String USER_EXISTS_ERROR_MESSAGE = "User already exists";

    private final String USER_NOT_FOUND_ERROR_MESSAGE = "User not found";


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
            throw new BusinessException(USER_EXISTS_ERROR_MESSAGE);
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
            new BusinessException(USER_NOT_FOUND_ERROR_MESSAGE));

        userEntity.setLastLogin(LocalDateTime.now());

        repository.save(userEntity);

        return UserDTO.fromEntity(userEntity);
    }
}
