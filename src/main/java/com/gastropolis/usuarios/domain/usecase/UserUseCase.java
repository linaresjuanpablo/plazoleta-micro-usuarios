package com.gastropolis.usuarios.domain.usecase;

import com.gastropolis.usuarios.domain.api.IUserServicePort;
import com.gastropolis.usuarios.domain.exception.InvalidAgeException;
import com.gastropolis.usuarios.domain.exception.UserAlreadyExistsException;
import com.gastropolis.usuarios.domain.exception.UserNotFoundException;
import com.gastropolis.usuarios.domain.model.RoleModel;
import com.gastropolis.usuarios.domain.model.UserModel;
import com.gastropolis.usuarios.domain.spi.IPasswordEncoderPort;
import com.gastropolis.usuarios.domain.spi.IUserPersistencePort;

import java.time.LocalDate;
import java.time.Period;

public class UserUseCase implements IUserServicePort {

    private static final String ROLE_PROPIETARIO = "PROPIETARIO";
    private static final String ROLE_EMPLEADO = "EMPLEADO";
    private static final String ROLE_CLIENTE = "CLIENTE";
    private static final int MINIMUM_AGE = 18;

    private final IUserPersistencePort userPersistencePort;
    private final IPasswordEncoderPort passwordEncoderPort;

    public UserUseCase(IUserPersistencePort userPersistencePort,
                       IPasswordEncoderPort passwordEncoderPort) {
        this.userPersistencePort = userPersistencePort;
        this.passwordEncoderPort = passwordEncoderPort;
    }

    @Override
    public UserModel createOwner(UserModel userModel) {
        validateEmailUniqueness(userModel.getEmail());
        validateDniUniqueness(userModel.getDni());
        validateBirthDateRequired(userModel.getBirthDate());
        validateMinimumAge(userModel.getBirthDate());

        RoleModel role = userPersistencePort.findRoleByName(ROLE_PROPIETARIO)
                .orElseThrow(() -> new RuntimeException("Role PROPIETARIO not found"));

        userModel.setRole(role);
        userModel.setPassword(passwordEncoderPort.encode(userModel.getPassword()));
        return userPersistencePort.saveUser(userModel);
    }

    @Override
    public UserModel createEmployee(UserModel userModel) {
        validateEmailUniqueness(userModel.getEmail());
        validateDniUniqueness(userModel.getDni());

        if (userModel.getBirthDate() != null) {
            validateMinimumAge(userModel.getBirthDate());
        }

        RoleModel role = userPersistencePort.findRoleByName(ROLE_EMPLEADO)
                .orElseThrow(() -> new RuntimeException("Role EMPLEADO not found"));

        userModel.setRole(role);
        userModel.setPassword(passwordEncoderPort.encode(userModel.getPassword()));
        return userPersistencePort.saveUser(userModel);
    }

    @Override
    public UserModel createClient(UserModel userModel) {
        validateEmailUniqueness(userModel.getEmail());
        validateDniUniqueness(userModel.getDni());

        if (userModel.getBirthDate() != null) {
            validateMinimumAge(userModel.getBirthDate());
        }

        RoleModel role = userPersistencePort.findRoleByName(ROLE_CLIENTE)
                .orElseThrow(() -> new RuntimeException("Role CLIENTE not found"));

        userModel.setRole(role);
        userModel.setPassword(passwordEncoderPort.encode(userModel.getPassword()));
        return userPersistencePort.saveUser(userModel);
    }

    @Override
    public UserModel getUserById(Long id) {
        return userPersistencePort.findUserById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
    }

    @Override
    public UserModel getUserByEmail(String email) {
        return userPersistencePort.findUserByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));
    }

    @Override
    public UserModel getUserByDni(String dni) {
        return userPersistencePort.findUserByDni(dni)
                .orElseThrow(() -> new UserNotFoundException("User not found with DNI: " + dni));
    }

    private void validateEmailUniqueness(String email) {
        if (userPersistencePort.existsUserByEmail(email)) {
            throw new UserAlreadyExistsException("User already exists with email: " + email);
        }
    }

    private void validateDniUniqueness(String dni) {
        if (userPersistencePort.existsUserByDni(dni)) {
            throw new UserAlreadyExistsException("User already exists with DNI: " + dni);
        }
    }

    private void validateBirthDateRequired(LocalDate birthDate) {
        if (birthDate == null) {
            throw new InvalidAgeException("Birth date is required for owner registration");
        }
    }

    private void validateMinimumAge(LocalDate birthDate) {
        LocalDate today = LocalDate.now();
        int age = Period.between(birthDate, today).getYears();
        if (age < MINIMUM_AGE) {
            throw new InvalidAgeException("User must be at least 18 years old. Current age: " + age);
        }
    }
}
