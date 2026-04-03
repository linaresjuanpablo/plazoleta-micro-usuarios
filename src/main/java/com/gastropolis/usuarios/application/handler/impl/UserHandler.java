package com.gastropolis.usuarios.application.handler.impl;

import com.gastropolis.usuarios.application.dto.request.CreateUserRequestDto;
import com.gastropolis.usuarios.application.dto.request.LoginRequestDto;
import com.gastropolis.usuarios.application.dto.response.LoginResponseDto;
import com.gastropolis.usuarios.application.dto.response.UserResponseDto;
import com.gastropolis.usuarios.application.handler.IUserHandler;
import com.gastropolis.usuarios.application.mapper.IUserRequestMapper;
import com.gastropolis.usuarios.application.mapper.IUserResponseMapper;
import com.gastropolis.usuarios.domain.api.IUserServicePort;
import com.gastropolis.usuarios.domain.exception.InvalidCredentialsException;
import com.gastropolis.usuarios.domain.model.UserModel;
import com.gastropolis.usuarios.domain.spi.IPasswordEncoderPort;
import com.gastropolis.usuarios.infrastructure.security.JwtService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserHandler implements IUserHandler {

    private final IUserServicePort userServicePort;
    private final IUserRequestMapper userRequestMapper;
    private final IUserResponseMapper userResponseMapper;
    private final JwtService jwtService;
    private final IPasswordEncoderPort passwordEncoderPort;

    public UserHandler(IUserServicePort userServicePort,
                       IUserRequestMapper userRequestMapper,
                       IUserResponseMapper userResponseMapper,
                       JwtService jwtService,
                       IPasswordEncoderPort passwordEncoderPort) {
        this.userServicePort = userServicePort;
        this.userRequestMapper = userRequestMapper;
        this.userResponseMapper = userResponseMapper;
        this.jwtService = jwtService;
        this.passwordEncoderPort = passwordEncoderPort;
    }

    @Override
    public UserResponseDto createOwner(CreateUserRequestDto createUserRequestDto) {
        UserModel userModel = userRequestMapper.toUserModel(createUserRequestDto);
        UserModel savedUser = userServicePort.createOwner(userModel);
        return userResponseMapper.toUserResponseDto(savedUser);
    }

    @Override
    public UserResponseDto createEmployee(CreateUserRequestDto createUserRequestDto) {
        UserModel userModel = userRequestMapper.toUserModel(createUserRequestDto);
        UserModel savedUser = userServicePort.createEmployee(userModel);
        return userResponseMapper.toUserResponseDto(savedUser);
    }

    @Override
    public UserResponseDto createClient(CreateUserRequestDto createUserRequestDto) {
        UserModel userModel = userRequestMapper.toUserModel(createUserRequestDto);
        UserModel savedUser = userServicePort.createClient(userModel);
        return userResponseMapper.toUserResponseDto(savedUser);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponseDto getUserById(Long id) {
        UserModel userModel = userServicePort.getUserById(id);
        return userResponseMapper.toUserResponseDto(userModel);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponseDto getUserByDni(String dni) {
        UserModel userModel = userServicePort.getUserByDni(dni);
        return userResponseMapper.toUserResponseDto(userModel);
    }

    @Override
    @Transactional(readOnly = true)
    public LoginResponseDto login(LoginRequestDto loginRequestDto) {
        UserModel userModel = userServicePort.getUserByEmail(loginRequestDto.getEmail());

        if (!passwordEncoderPort.matches(loginRequestDto.getPassword(), userModel.getPassword())) {
            throw new InvalidCredentialsException("Invalid email or password");
        }

        String token = jwtService.generateToken(userModel);
        return new LoginResponseDto(token);
    }
}
