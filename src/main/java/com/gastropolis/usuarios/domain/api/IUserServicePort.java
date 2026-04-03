package com.gastropolis.usuarios.domain.api;

import com.gastropolis.usuarios.domain.model.UserModel;

public interface IUserServicePort {

    UserModel createOwner(UserModel userModel);

    UserModel createEmployee(UserModel userModel);

    UserModel createClient(UserModel userModel);

    UserModel getUserById(Long id);

    UserModel getUserByEmail(String email);

    UserModel getUserByDni(String dni);
}
