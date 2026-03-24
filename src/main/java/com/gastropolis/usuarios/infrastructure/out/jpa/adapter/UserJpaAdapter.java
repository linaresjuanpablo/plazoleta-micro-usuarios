package com.gastropolis.usuarios.infrastructure.out.jpa.adapter;

import com.gastropolis.usuarios.domain.model.RoleModel;
import com.gastropolis.usuarios.domain.model.UserModel;
import com.gastropolis.usuarios.domain.spi.IUserPersistencePort;
import com.gastropolis.usuarios.infrastructure.out.jpa.entity.UserEntity;
import com.gastropolis.usuarios.infrastructure.out.jpa.mapper.IUserEntityMapper;
import com.gastropolis.usuarios.infrastructure.out.jpa.repository.IRoleRepository;
import com.gastropolis.usuarios.infrastructure.out.jpa.repository.IUserRepository;

import java.util.Optional;

public class UserJpaAdapter implements IUserPersistencePort {

    private final IUserRepository userRepository;
    private final IRoleRepository roleRepository;
    private final IUserEntityMapper userEntityMapper;

    public UserJpaAdapter(IUserRepository userRepository,
                          IRoleRepository roleRepository,
                          IUserEntityMapper userEntityMapper) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userEntityMapper = userEntityMapper;
    }

    @Override
    public UserModel saveUser(UserModel userModel) {
        UserEntity userEntity = userEntityMapper.toUserEntity(userModel);
        UserEntity savedEntity = userRepository.save(userEntity);
        return userEntityMapper.toUserModel(savedEntity);
    }

    @Override
    public Optional<UserModel> findUserById(Long id) {
        return userRepository.findById(id)
                .map(userEntityMapper::toUserModel);
    }

    @Override
    public Optional<UserModel> findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(userEntityMapper::toUserModel);
    }

    @Override
    public Optional<UserModel> findUserByDni(String dni) {
        return userRepository.findByDni(dni)
                .map(userEntityMapper::toUserModel);
    }

    @Override
    public Optional<RoleModel> findRoleByName(String roleName) {
        return roleRepository.findByName(roleName)
                .map(userEntityMapper::toRoleModel);
    }

    @Override
    public boolean existsUserByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public boolean existsUserByDni(String dni) {
        return userRepository.existsByDni(dni);
    }
}
