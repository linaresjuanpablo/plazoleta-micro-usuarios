package com.gastropolis.usuarios.domain.usecase;

import com.gastropolis.usuarios.domain.exception.InvalidAgeException;
import com.gastropolis.usuarios.domain.exception.UserAlreadyExistsException;
import com.gastropolis.usuarios.domain.exception.UserNotFoundException;
import com.gastropolis.usuarios.domain.model.RoleModel;
import com.gastropolis.usuarios.domain.model.UserModel;
import com.gastropolis.usuarios.domain.spi.IPasswordEncoderPort;
import com.gastropolis.usuarios.domain.spi.IUserPersistencePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserUseCaseTest {

    @Mock
    private IUserPersistencePort userPersistencePort;

    @Mock
    private IPasswordEncoderPort passwordEncoderPort;

    @InjectMocks
    private UserUseCase userUseCase;

    private UserModel validOwnerModel;
    private UserModel validEmployeeModel;
    private UserModel validClientModel;
    private RoleModel propietarioRole;
    private RoleModel empleadoRole;
    private RoleModel clienteRole;

    @BeforeEach
    void setUp() {
        propietarioRole = new RoleModel(1L, "PROPIETARIO");
        empleadoRole = new RoleModel(2L, "EMPLEADO");
        clienteRole = new RoleModel(3L, "CLIENTE");

        validOwnerModel = new UserModel(
                null, "Juan", "Perez", "12345678", "+573001234567",
                LocalDate.now().minusYears(25), "juan@example.com", "password123", null
        );

        validEmployeeModel = new UserModel(
                null, "Maria", "Lopez", "87654321", "+573007654321",
                LocalDate.now().minusYears(22), "maria@example.com", "password123", null
        );

        validClientModel = new UserModel(
                null, "Carlos", "Garcia", "11223344", "+573001122334",
                LocalDate.now().minusYears(30), "carlos@example.com", "password123", null
        );
    }

    // ===== createOwner tests =====

    @Test
    void createOwner_happyPath_returnsSavedUser() {
        UserModel savedUser = new UserModel(1L, "Juan", "Perez", "12345678", "+573001234567",
                LocalDate.now().minusYears(25), "juan@example.com", "encodedPassword", propietarioRole);

        when(userPersistencePort.existsUserByEmail("juan@example.com")).thenReturn(false);
        when(userPersistencePort.existsUserByDni("12345678")).thenReturn(false);
        when(userPersistencePort.findRoleByName("PROPIETARIO")).thenReturn(Optional.of(propietarioRole));
        when(passwordEncoderPort.encode("password123")).thenReturn("encodedPassword");
        when(userPersistencePort.saveUser(any(UserModel.class))).thenReturn(savedUser);

        UserModel result = userUseCase.createOwner(validOwnerModel);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("encodedPassword", result.getPassword());
        assertEquals(propietarioRole, result.getRole());
        verify(userPersistencePort).saveUser(any(UserModel.class));
    }

    @Test
    void createOwner_emailAlreadyExists_throwsUserAlreadyExistsException() {
        when(userPersistencePort.existsUserByEmail("juan@example.com")).thenReturn(true);

        assertThrows(UserAlreadyExistsException.class,
                () -> userUseCase.createOwner(validOwnerModel));

        verify(userPersistencePort, never()).saveUser(any());
    }

    @Test
    void createOwner_dniAlreadyExists_throwsUserAlreadyExistsException() {
        when(userPersistencePort.existsUserByEmail("juan@example.com")).thenReturn(false);
        when(userPersistencePort.existsUserByDni("12345678")).thenReturn(true);

        assertThrows(UserAlreadyExistsException.class,
                () -> userUseCase.createOwner(validOwnerModel));

        verify(userPersistencePort, never()).saveUser(any());
    }

    @Test
    void createOwner_birthDateNull_throwsInvalidAgeException() {
        validOwnerModel.setBirthDate(null);
        when(userPersistencePort.existsUserByEmail("juan@example.com")).thenReturn(false);
        when(userPersistencePort.existsUserByDni("12345678")).thenReturn(false);

        assertThrows(InvalidAgeException.class,
                () -> userUseCase.createOwner(validOwnerModel));

        verify(userPersistencePort, never()).saveUser(any());
    }

    @Test
    void createOwner_ageLessThan18_throwsInvalidAgeException() {
        validOwnerModel.setBirthDate(LocalDate.now().minusYears(16));
        when(userPersistencePort.existsUserByEmail("juan@example.com")).thenReturn(false);
        when(userPersistencePort.existsUserByDni("12345678")).thenReturn(false);

        assertThrows(InvalidAgeException.class,
                () -> userUseCase.createOwner(validOwnerModel));

        verify(userPersistencePort, never()).saveUser(any());
    }

    @Test
    void createOwner_roleNotFound_throwsRuntimeException() {
        when(userPersistencePort.existsUserByEmail("juan@example.com")).thenReturn(false);
        when(userPersistencePort.existsUserByDni("12345678")).thenReturn(false);
        when(userPersistencePort.findRoleByName("PROPIETARIO")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> userUseCase.createOwner(validOwnerModel));

        verify(userPersistencePort, never()).saveUser(any());
    }

    // ===== createEmployee tests =====

    @Test
    void createEmployee_happyPath_withBirthDate_returnsSavedUser() {
        UserModel savedUser = new UserModel(2L, "Maria", "Lopez", "87654321", "+573007654321",
                LocalDate.now().minusYears(22), "maria@example.com", "encodedPassword", empleadoRole);

        when(userPersistencePort.existsUserByEmail("maria@example.com")).thenReturn(false);
        when(userPersistencePort.existsUserByDni("87654321")).thenReturn(false);
        when(userPersistencePort.findRoleByName("EMPLEADO")).thenReturn(Optional.of(empleadoRole));
        when(passwordEncoderPort.encode("password123")).thenReturn("encodedPassword");
        when(userPersistencePort.saveUser(any(UserModel.class))).thenReturn(savedUser);

        UserModel result = userUseCase.createEmployee(validEmployeeModel);

        assertNotNull(result);
        assertEquals(2L, result.getId());
        assertEquals(empleadoRole, result.getRole());
        verify(userPersistencePort).saveUser(any(UserModel.class));
    }

    @Test
    void createEmployee_happyPath_noBirthDate_returnsSavedUser() {
        validEmployeeModel.setBirthDate(null);
        UserModel savedUser = new UserModel(2L, "Maria", "Lopez", "87654321", "+573007654321",
                null, "maria@example.com", "encodedPassword", empleadoRole);

        when(userPersistencePort.existsUserByEmail("maria@example.com")).thenReturn(false);
        when(userPersistencePort.existsUserByDni("87654321")).thenReturn(false);
        when(userPersistencePort.findRoleByName("EMPLEADO")).thenReturn(Optional.of(empleadoRole));
        when(passwordEncoderPort.encode("password123")).thenReturn("encodedPassword");
        when(userPersistencePort.saveUser(any(UserModel.class))).thenReturn(savedUser);

        UserModel result = userUseCase.createEmployee(validEmployeeModel);

        assertNotNull(result);
        assertEquals(empleadoRole, result.getRole());
    }

    @Test
    void createEmployee_emailAlreadyExists_throwsUserAlreadyExistsException() {
        when(userPersistencePort.existsUserByEmail("maria@example.com")).thenReturn(true);

        assertThrows(UserAlreadyExistsException.class,
                () -> userUseCase.createEmployee(validEmployeeModel));

        verify(userPersistencePort, never()).saveUser(any());
    }

    @Test
    void createEmployee_dniAlreadyExists_throwsUserAlreadyExistsException() {
        when(userPersistencePort.existsUserByEmail("maria@example.com")).thenReturn(false);
        when(userPersistencePort.existsUserByDni("87654321")).thenReturn(true);

        assertThrows(UserAlreadyExistsException.class,
                () -> userUseCase.createEmployee(validEmployeeModel));

        verify(userPersistencePort, never()).saveUser(any());
    }

    @Test
    void createEmployee_ageLessThan18_throwsInvalidAgeException() {
        validEmployeeModel.setBirthDate(LocalDate.now().minusYears(16));
        when(userPersistencePort.existsUserByEmail("maria@example.com")).thenReturn(false);
        when(userPersistencePort.existsUserByDni("87654321")).thenReturn(false);

        assertThrows(InvalidAgeException.class,
                () -> userUseCase.createEmployee(validEmployeeModel));

        verify(userPersistencePort, never()).saveUser(any());
    }

    @Test
    void createEmployee_roleNotFound_throwsRuntimeException() {
        when(userPersistencePort.existsUserByEmail("maria@example.com")).thenReturn(false);
        when(userPersistencePort.existsUserByDni("87654321")).thenReturn(false);
        when(userPersistencePort.findRoleByName("EMPLEADO")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> userUseCase.createEmployee(validEmployeeModel));

        verify(userPersistencePort, never()).saveUser(any());
    }

    // ===== createClient tests =====

    @Test
    void createClient_happyPath_withBirthDate_returnsSavedUser() {
        UserModel savedUser = new UserModel(3L, "Carlos", "Garcia", "11223344", "+573001122334",
                LocalDate.now().minusYears(30), "carlos@example.com", "encodedPassword", clienteRole);

        when(userPersistencePort.existsUserByEmail("carlos@example.com")).thenReturn(false);
        when(userPersistencePort.existsUserByDni("11223344")).thenReturn(false);
        when(userPersistencePort.findRoleByName("CLIENTE")).thenReturn(Optional.of(clienteRole));
        when(passwordEncoderPort.encode("password123")).thenReturn("encodedPassword");
        when(userPersistencePort.saveUser(any(UserModel.class))).thenReturn(savedUser);

        UserModel result = userUseCase.createClient(validClientModel);

        assertNotNull(result);
        assertEquals(3L, result.getId());
        assertEquals(clienteRole, result.getRole());
        verify(userPersistencePort).saveUser(any(UserModel.class));
    }

    @Test
    void createClient_happyPath_noBirthDate_returnsSavedUser() {
        validClientModel.setBirthDate(null);
        UserModel savedUser = new UserModel(3L, "Carlos", "Garcia", "11223344", "+573001122334",
                null, "carlos@example.com", "encodedPassword", clienteRole);

        when(userPersistencePort.existsUserByEmail("carlos@example.com")).thenReturn(false);
        when(userPersistencePort.existsUserByDni("11223344")).thenReturn(false);
        when(userPersistencePort.findRoleByName("CLIENTE")).thenReturn(Optional.of(clienteRole));
        when(passwordEncoderPort.encode("password123")).thenReturn("encodedPassword");
        when(userPersistencePort.saveUser(any(UserModel.class))).thenReturn(savedUser);

        UserModel result = userUseCase.createClient(validClientModel);

        assertNotNull(result);
        assertEquals(clienteRole, result.getRole());
    }

    @Test
    void createClient_emailAlreadyExists_throwsUserAlreadyExistsException() {
        when(userPersistencePort.existsUserByEmail("carlos@example.com")).thenReturn(true);

        assertThrows(UserAlreadyExistsException.class,
                () -> userUseCase.createClient(validClientModel));

        verify(userPersistencePort, never()).saveUser(any());
    }

    @Test
    void createClient_dniAlreadyExists_throwsUserAlreadyExistsException() {
        when(userPersistencePort.existsUserByEmail("carlos@example.com")).thenReturn(false);
        when(userPersistencePort.existsUserByDni("11223344")).thenReturn(true);

        assertThrows(UserAlreadyExistsException.class,
                () -> userUseCase.createClient(validClientModel));

        verify(userPersistencePort, never()).saveUser(any());
    }

    @Test
    void createClient_ageLessThan18_throwsInvalidAgeException() {
        validClientModel.setBirthDate(LocalDate.now().minusYears(16));
        when(userPersistencePort.existsUserByEmail("carlos@example.com")).thenReturn(false);
        when(userPersistencePort.existsUserByDni("11223344")).thenReturn(false);

        assertThrows(InvalidAgeException.class,
                () -> userUseCase.createClient(validClientModel));

        verify(userPersistencePort, never()).saveUser(any());
    }

    @Test
    void createClient_roleNotFound_throwsRuntimeException() {
        when(userPersistencePort.existsUserByEmail("carlos@example.com")).thenReturn(false);
        when(userPersistencePort.existsUserByDni("11223344")).thenReturn(false);
        when(userPersistencePort.findRoleByName("CLIENTE")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> userUseCase.createClient(validClientModel));

        verify(userPersistencePort, never()).saveUser(any());
    }

    // ===== getUserById tests =====

    @Test
    void getUserById_userFound_returnsUser() {
        UserModel expectedUser = new UserModel(1L, "Juan", "Perez", "12345678", "+573001234567",
                LocalDate.now().minusYears(25), "juan@example.com", "encodedPassword", propietarioRole);
        when(userPersistencePort.findUserById(1L)).thenReturn(Optional.of(expectedUser));

        UserModel result = userUseCase.getUserById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Juan", result.getName());
    }

    @Test
    void getUserById_userNotFound_throwsUserNotFoundException() {
        when(userPersistencePort.findUserById(99L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
                () -> userUseCase.getUserById(99L));
    }

    // ===== getUserByEmail tests =====

    @Test
    void getUserByEmail_userFound_returnsUser() {
        UserModel expectedUser = new UserModel(1L, "Juan", "Perez", "12345678", "+573001234567",
                LocalDate.now().minusYears(25), "juan@example.com", "encodedPassword", propietarioRole);
        when(userPersistencePort.findUserByEmail("juan@example.com")).thenReturn(Optional.of(expectedUser));

        UserModel result = userUseCase.getUserByEmail("juan@example.com");

        assertNotNull(result);
        assertEquals("juan@example.com", result.getEmail());
    }

    @Test
    void getUserByEmail_userNotFound_throwsUserNotFoundException() {
        when(userPersistencePort.findUserByEmail("unknown@example.com")).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
                () -> userUseCase.getUserByEmail("unknown@example.com"));
    }
}
