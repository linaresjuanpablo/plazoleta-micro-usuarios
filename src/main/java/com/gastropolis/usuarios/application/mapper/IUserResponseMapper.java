package com.gastropolis.usuarios.application.mapper;

import com.gastropolis.usuarios.application.dto.response.UserResponseDto;
import com.gastropolis.usuarios.domain.model.UserModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface IUserResponseMapper {

    @Mapping(source = "role.name", target = "roleName")
    UserResponseDto toUserResponseDto(UserModel userModel);
}
