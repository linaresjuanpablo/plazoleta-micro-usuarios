package com.gastropolis.usuarios.infrastructure.configuration;

import com.gastropolis.usuarios.domain.api.IUserServicePort;
import com.gastropolis.usuarios.domain.spi.IPasswordEncoderPort;
import com.gastropolis.usuarios.domain.spi.IUserPersistencePort;
import com.gastropolis.usuarios.domain.usecase.UserUseCase;
import com.gastropolis.usuarios.infrastructure.out.jpa.adapter.UserJpaAdapter;
import com.gastropolis.usuarios.infrastructure.out.jpa.mapper.IUserEntityMapper;
import com.gastropolis.usuarios.infrastructure.out.jpa.repository.IRoleRepository;
import com.gastropolis.usuarios.infrastructure.out.jpa.repository.IUserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class BeanConfiguration {

    private final IUserRepository userRepository;
    private final IRoleRepository roleRepository;
    private final IUserEntityMapper userEntityMapper;

    public BeanConfiguration(IUserRepository userRepository,
                              IRoleRepository roleRepository,
                              IUserEntityMapper userEntityMapper) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userEntityMapper = userEntityMapper;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public IUserPersistencePort userPersistencePort() {
        return new UserJpaAdapter(userRepository, roleRepository, userEntityMapper);
    }

    @Bean
    public IUserServicePort userServicePort(IUserPersistencePort userPersistencePort,
                                            IPasswordEncoderPort passwordEncoderPort) {
        return new UserUseCase(userPersistencePort, passwordEncoderPort);
    }
}
