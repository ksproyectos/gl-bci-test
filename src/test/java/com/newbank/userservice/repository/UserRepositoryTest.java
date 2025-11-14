package com.newbank.userservice.repository;

import com.newbank.userservice.model.UserEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void saveAndFindByEmail_and_existsByEmail() {
        UserEntity entity = new UserEntity();
        entity.setName("RepoUser");
        entity.setEmail("repo@example.com");
        entity.setPassword("secret");
        entity.setCreated(LocalDateTime.now());
        entity.setLastLogin(LocalDateTime.now());

        userRepository.save(entity);

        assertThat(userRepository.findByEmail("repo@example.com")).isPresent();
        assertThat(userRepository.existsByEmail("repo@example.com")).isTrue();
    }
}
