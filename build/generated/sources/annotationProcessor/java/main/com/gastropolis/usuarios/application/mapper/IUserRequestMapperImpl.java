package com.gastropolis.usuarios.application.mapper;

import com.gastropolis.usuarios.application.dto.request.CreateUserRequestDto;
import com.gastropolis.usuarios.domain.model.UserModel;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-03-23T16:33:56-0500",
    comments = "version: 1.5.5.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.2.jar, environment: Java 17.0.9 (Amazon.com Inc.)"
)
@Component
public class IUserRequestMapperImpl implements IUserRequestMapper {

    @Override
    public UserModel toUserModel(CreateUserRequestDto createUserRequestDto) {
        if ( createUserRequestDto == null ) {
            return null;
        }

        UserModel userModel = new UserModel();

        userModel.setName( createUserRequestDto.getName() );
        userModel.setLastName( createUserRequestDto.getLastName() );
        userModel.setDni( createUserRequestDto.getDni() );
        userModel.setPhone( createUserRequestDto.getPhone() );
        userModel.setBirthDate( createUserRequestDto.getBirthDate() );
        userModel.setEmail( createUserRequestDto.getEmail() );
        userModel.setPassword( createUserRequestDto.getPassword() );

        return userModel;
    }
}
