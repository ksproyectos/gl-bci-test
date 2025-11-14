package com.newbank.userservice.service;

import com.newbank.userservice.dto.UserDTO;
import com.newbank.userservice.exception.BusinessException;
import com.newbank.userservice.model.UserEntity;
import com.newbank.userservice.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository repository;

    @InjectMocks
    private UserService userService;

    private UserEntity sampleEntity;

    @BeforeEach
    void setUp() {
        sampleEntity = new UserEntity();
        sampleEntity.setName("Alice");
        sampleEntity.setEmail("alice@example.com");
        sampleEntity.setPassword("encoded");
        sampleEntity.setCreated(LocalDateTime.now());
        sampleEntity.setLastLogin(LocalDateTime.now());
        sampleEntity.setActive(true);
    }

    @Test
    void userExists_whenPresent_returnsTrue() {
        when(repository.findByEmail("alice@example.com")).thenReturn(Optional.of(sampleEntity));

        boolean exists = userService.userExists("alice@example.com");

        assertThat(exists).isTrue();
    }

    @Test
    void userExists_whenNotPresent_returnsFalse() {
        when(repository.findByEmail("bob@example.com")).thenReturn(Optional.empty());

        boolean exists = userService.userExists("bob@example.com");

        assertThat(exists).isFalse();
    }

    @Test
    void createUser_success_savesAndReturnsDto() {
        // repository returns empty -> user does not exist
        when(repository.findByEmail("new@example.com")).thenReturn(Optional.empty());

        // mimic save: return the same entity passed
        when(repository.save(any(UserEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UserDTO request = new UserDTO();
        request.setName("New User");
        request.setEmail("new@example.com");
        request.setPassword("Password1");

        UserDTO created = userService.createUser(request);

        assertThat(created).isNotNull();
        assertThat(created.getEmail()).isEqualTo("new@example.com");
        assertThat(created.getName()).isEqualTo("New User");
        assertThat(created.isActive()).isTrue();

        ArgumentCaptor<UserEntity> captor = ArgumentCaptor.forClass(UserEntity.class);
        verify(repository).save(captor.capture());
        UserEntity saved = captor.getValue();
        assertThat(saved.getEmail()).isEqualTo("new@example.com");
        assertThat(saved.getPassword()).isNotNull();
    }

    @Test
    void createUser_whenExists_throwsBusinessException() {
        when(repository.findByEmail("exists@example.com")).thenReturn(Optional.of(sampleEntity));

        UserDTO request = new UserDTO();
        request.setEmail("exists@example.com");

        assertThatThrownBy(() -> userService.createUser(request))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("El usuario ya existe");
    }

    @Test
    void updateUserLastLogin_success_updatesAndReturns() {
        UserEntity stored = new UserEntity();
        stored.setEmail("stored@example.com");
        stored.setName("Stored");
        stored.setCreated(LocalDateTime.now().minusDays(1));
        stored.setLastLogin(LocalDateTime.now().minusDays(1));

        when(repository.findByEmail("stored@example.com")).thenReturn(Optional.of(stored));
        when(repository.save(any(UserEntity.class))).thenAnswer(i -> i.getArgument(0));

        var dto = userService.updateUserLastLogin("stored@example.com");

        assertThat(dto).isNotNull();
        assertThat(dto.getLastLogin()).isNotNull();
        verify(repository).save(any(UserEntity.class));
    }

    @Test
    void updateUserLastLogin_notFound_throws() {
        when(repository.findByEmail("nope@example.com")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.updateUserLastLogin("nope@example.com"))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("Usuario no encontrado");
    }
}

