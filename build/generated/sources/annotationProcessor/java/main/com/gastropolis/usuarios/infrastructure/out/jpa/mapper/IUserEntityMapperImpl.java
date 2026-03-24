package com.gastropolis.usuarios.infrastructure.out.jpa.mapper;

import com.gastropolis.usuarios.domain.model.RoleModel;
import com.gastropolis.usuarios.domain.model.UserModel;
import com.gastropolis.usuarios.infrastructure.out.jpa.entity.RoleEntity;
import com.gastropolis.usuarios.infrastructure.out.jpa.entity.UserEntity;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-03-23T16:33:56-0500",
    comments = "version: 1.5.5.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.2.jar, environment: Java 17.0.9 (Amazon.com Inc.)"
)
@Component
public class IUserEntityMapperImpl implements IUserEntityMapper {

    @Override
    public UserModel toUserModel(UserEntity userEntity) {
        if ( userEntity == null ) {
            return null;
        }

        UserModel userModel = new UserModel();

        userModel.setId( userEntity.getId() );
        userModel.setName( userEntity.getName() );
        userModel.setLastName( userEntity.getLastName() );
        userModel.setDni( userEntity.getDni() );
        userModel.setPhone( userEntity.getPhone() );
        userModel.setBirthDate( userEntity.getBirthDate() );
        userModel.setEmail( userEntity.getEmail() );
        userModel.setPassword( userEntity.getPassword() );
        userModel.setRole( toRoleModel( userEntity.getRole() ) );

        return userModel;
    }

    @Override
    public UserEntity toUserEntity(UserModel userModel) {
        if ( userModel == null ) {
            return null;
        }

        UserEntity userEntity = new UserEntity();

        userEntity.setId( userModel.getId() );
        userEntity.setName( userModel.getName() );
        userEntity.setLastName( userModel.getLastName() );
        userEntity.setDni( userModel.getDni() );
        userEntity.setPhone( userModel.getPhone() );
        userEntity.setBirthDate( userModel.getBirthDate() );
        userEntity.setEmail( userModel.getEmail() );
        userEntity.setPassword( userModel.getPassword() );
        userEntity.setRole( toRoleEntity( userModel.getRole() ) );

        return userEntity;
    }

    @Override
    public RoleModel toRoleModel(RoleEntity roleEntity) {
        if ( roleEntity == null ) {
            return null;
        }

        RoleModel roleModel = new RoleModel();

        roleModel.setId( roleEntity.getId() );
        roleModel.setName( roleEntity.getName() );

        return roleModel;
    }

    @Override
    public RoleEntity toRoleEntity(RoleModel roleModel) {
        if ( roleModel == null ) {
            return null;
        }

        RoleEntity roleEntity = new RoleEntity();

        roleEntity.setId( roleModel.getId() );
        roleEntity.setName( roleModel.getName() );

        return roleEntity;
    }
}
