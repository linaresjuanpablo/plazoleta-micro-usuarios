package com.gastropolis.usuarios.infrastructure.out.jpa.mapper;

import com.gastropolis.usuarios.domain.model.RoleModel;
import com.gastropolis.usuarios.domain.model.UserModel;
import com.gastropolis.usuarios.infrastructure.out.jpa.entity.RoleEntity;
import com.gastropolis.usuarios.infrastructure.out.jpa.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface IUserEntityMapper {

    UserModel toUserModel(UserEntity userEntity);

    UserEntity toUserEntity(UserModel userModel);

    RoleModel toRoleModel(RoleEntity roleEntity);

    RoleEntity toRoleEntity(RoleModel roleModel);
}
