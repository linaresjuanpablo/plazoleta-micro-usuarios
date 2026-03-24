package com.gastropolis.usuarios.domain.spi;

import com.gastropolis.usuarios.domain.model.RoleModel;
import com.gastropolis.usuarios.domain.model.UserModel;

import java.util.Optional;

public interface IUserPersistencePort {

    UserModel saveUser(UserModel userModel);

    Optional<UserModel> findUserById(Long id);

    Optional<UserModel> findUserByEmail(String email);

    Optional<UserModel> findUserByDni(String dni);

    Optional<RoleModel> findRoleByName(String roleName);

    boolean existsUserByEmail(String email);

    boolean existsUserByDni(String dni);
}
