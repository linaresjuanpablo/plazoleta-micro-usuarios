package com.gastropolis.usuarios.application.mapper;

import com.gastropolis.usuarios.application.dto.response.UserResponseDto;
import com.gastropolis.usuarios.domain.model.RoleModel;
import com.gastropolis.usuarios.domain.model.UserModel;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-03-31T16:16:57-0500",
    comments = "version: 1.5.5.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.2.jar, environment: Java 17.0.9 (Amazon.com Inc.)"
)
@Component
public class IUserResponseMapperImpl implements IUserResponseMapper {

    @Override
    public UserResponseDto toUserResponseDto(UserModel userModel) {
        if ( userModel == null ) {
            return null;
        }

        UserResponseDto userResponseDto = new UserResponseDto();

        userResponseDto.setRoleName( userModelRoleName( userModel ) );
        userResponseDto.setId( userModel.getId() );
        userResponseDto.setName( userModel.getName() );
        userResponseDto.setLastName( userModel.getLastName() );
        userResponseDto.setDni( userModel.getDni() );
        userResponseDto.setPhone( userModel.getPhone() );
        userResponseDto.setBirthDate( userModel.getBirthDate() );
        userResponseDto.setEmail( userModel.getEmail() );

        return userResponseDto;
    }

    private String userModelRoleName(UserModel userModel) {
        if ( userModel == null ) {
            return null;
        }
        RoleModel role = userModel.getRole();
        if ( role == null ) {
            return null;
        }
        String name = role.getName();
        if ( name == null ) {
            return null;
        }
        return name;
    }
}
