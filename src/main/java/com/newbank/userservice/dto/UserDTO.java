package com.newbank.userservice.dto;

import com.newbank.userservice.model.UserEntity;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class UserDTO {
    private String id;
    private String name;
    private String email;

    private String password;

    private LocalDateTime created;

    private LocalDateTime lastLogin;

    private boolean isActive;
    private List<PhoneDTO> phones;
    public static UserDTO fromEntity(UserEntity userEntity){
        UserDTO dto = new UserDTO();
        dto.setId(userEntity.getId());
        dto.setName(userEntity.getName());
        dto.setEmail(userEntity.getEmail());
        dto.setPassword(userEntity.getPassword());
        dto.setCreated(userEntity.getCreated());
        dto.setLastLogin(userEntity.getLastLogin());
        dto.setActive(userEntity.isActive());
        return dto;
    }

}

