package com.gastropolis.usuarios.application.mapper;

import com.gastropolis.usuarios.application.dto.request.CreateUserRequestDto;
import com.gastropolis.usuarios.domain.model.UserModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface IUserRequestMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "role", ignore = true)
    UserModel toUserModel(CreateUserRequestDto createUserRequestDto);
}
