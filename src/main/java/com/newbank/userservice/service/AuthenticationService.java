package com.newbank.userservice.service;


import com.newbank.userservice.dto.*;
import com.newbank.userservice.exception.BusinessException;
import com.newbank.userservice.security.JwtUtil;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class AuthenticationService{

    private final String EMAIL_FORMAT_ERROR_MESSAGE = "Email format is invalid";
    private final String PASSWORD_FORMAT_ERROR_MESSAGE = "Password does not meet the required format";

    private final UserService userService;
    private final JwtUtil jwtUtil;

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^(?=.*[A-Z])(?!.*[A-Z].*[A-Z])(?=(?:.*\\d){2})(?!.*\\d.*\\d.*\\d)[A-Za-z0-9]{8,12}$");

    public AuthenticationService(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }


    @Transactional
    public SignUpResponseDTO signUp(SignUpRequestDTO request) {

        validateEmail(request.getEmail());
        validatePassword(request.getPassword());

        UserDTO userDTO = toUserDTO(request);

        userDTO = userService.createUser(userDTO);

        SignUpResponseDTO responseDTO = toSignUpResponseDTO(userDTO);

        responseDTO.setToken(jwtUtil.generateToken(userDTO.getEmail()));

        return responseDTO;
    }


    //TODO: Cambiar el nombre de este metodo, si es para consultar un usuario no deberia llamarse login
    @Transactional
    public LoginResponseDTO login(LoginRequestDTO requestDTO){

        jwtUtil.isTokenValid(requestDTO.getToken());

        String email = jwtUtil.extractUsername(requestDTO.getToken());

        UserDTO userDTO = userService.updateUserLastLogin(email);

        LoginResponseDTO loginResponseDTO = toLoginResponse(userDTO);

        loginResponseDTO.setToken(jwtUtil.generateToken(userDTO.getEmail()));

        return loginResponseDTO;
    }


    private void validateEmail(String email) {
        if (email == null || !EMAIL_PATTERN.matcher(email).matches()) {
            throw new BusinessException(EMAIL_FORMAT_ERROR_MESSAGE);
        }
    }

    private void validatePassword(String password) {
        if (password == null || !PASSWORD_PATTERN.matcher(password).matches()) {
            throw new BusinessException(PASSWORD_FORMAT_ERROR_MESSAGE);
        }
    }


    private SignUpResponseDTO toSignUpResponseDTO(UserDTO entity) {
        SignUpResponseDTO dto = new SignUpResponseDTO();

        dto.setId(entity.getId().toString());
        dto.setCreated(entity.getCreated());
        dto.setLastLogin(entity.getLastLogin());
        dto.setToken(jwtUtil.generateToken(entity.getEmail()));
        dto.setActive(entity.isActive());

        return dto;
    }

    private UserDTO toUserDTO(SignUpRequestDTO source ){
        UserDTO dto = new UserDTO();

        dto.setName(source.getName());
        dto.setEmail(source.getEmail());
        dto.setPassword(source.getPassword());

        if (source.getPhones() != null) {
            dto.setPhones(
                    source.getPhones().stream().map(p -> new PhoneDTO(
                            p.getNumber(),
                            p.getCitycode(),
                            p.getContrycode()
                    )).collect(Collectors.toList())
            );
        }

        return dto;
    }

    private LoginResponseDTO toLoginResponse(UserDTO userDTO) {
        LoginResponseDTO dto = new LoginResponseDTO();

        dto.setId(userDTO.getId().toString());
        dto.setCreated(userDTO.getCreated());
        dto.setLastLogin(userDTO.getLastLogin());
        dto.setToken(jwtUtil.generateToken(userDTO.getName()));
        dto.setActive(userDTO.isActive());
        dto.setName(userDTO.getName());
        dto.setEmail(userDTO.getEmail());
        dto.setPassword(userDTO.getPassword());

        if (userDTO.getPhones() != null) {
            dto.setPhones(
                    userDTO.getPhones().stream().map(p -> new LoginResponseDTO.PhoneDTO(
                            p.getNumber(),
                            p.getCitycode(),
                            p.getContrycode()
                    )).collect(Collectors.toList())
            );
        }

        return dto;
    }
}
