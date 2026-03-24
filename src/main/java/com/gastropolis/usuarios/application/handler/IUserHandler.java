package com.gastropolis.usuarios.application.handler;

import com.gastropolis.usuarios.application.dto.request.CreateUserRequestDto;
import com.gastropolis.usuarios.application.dto.request.LoginRequestDto;
import com.gastropolis.usuarios.application.dto.response.LoginResponseDto;
import com.gastropolis.usuarios.application.dto.response.UserResponseDto;

public interface IUserHandler {

    UserResponseDto createOwner(CreateUserRequestDto createUserRequestDto);

    UserResponseDto createEmployee(CreateUserRequestDto createUserRequestDto);

    UserResponseDto createClient(CreateUserRequestDto createUserRequestDto);

    UserResponseDto getUserById(Long id);

    LoginResponseDto login(LoginRequestDto loginRequestDto);
}
