package com.newbank.userservice.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


@Entity
@Table(name = "users")
@Getter
@Setter
public class UserEntity {
    @Id
    private String id = UUID.randomUUID().toString();


    private String name;


    @Column(unique = true, nullable = false)
    private String email;


    private String password;


    private LocalDateTime created;


    private LocalDateTime lastLogin;


    private boolean isActive = true;


    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private List<PhoneEntity> phones;
}
