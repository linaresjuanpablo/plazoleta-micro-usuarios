package com.gastropolis.usuarios.application.handler.impl;

import com.gastropolis.usuarios.application.dto.request.CreateUserRequestDto;
import com.gastropolis.usuarios.application.dto.request.LoginRequestDto;
import com.gastropolis.usuarios.application.dto.response.LoginResponseDto;
import com.gastropolis.usuarios.application.dto.response.UserResponseDto;
import com.gastropolis.usuarios.application.mapper.IUserRequestMapper;
import com.gastropolis.usuarios.application.mapper.IUserResponseMapper;
import com.gastropolis.usuarios.domain.api.IUserServicePort;
import com.gastropolis.usuarios.domain.exception.InvalidCredentialsException;
import com.gastropolis.usuarios.domain.model.RoleModel;
import com.gastropolis.usuarios.domain.model.UserModel;
import com.gastropolis.usuarios.domain.spi.IPasswordEncoderPort;
import com.gastropolis.usuarios.infrastructure.security.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserHandlerTest {

    @Mock
    private IUserServicePort userServicePort;

    @Mock
    private IUserRequestMapper userRequestMapper;

    @Mock
    private IUserResponseMapper userResponseMapper;

    @Mock
    private JwtService jwtService;

    @Mock
    private IPasswordEncoderPort passwordEncoderPort;

    @InjectMocks
    private UserHandler userHandler;

    private CreateUserRequestDto createUserRequestDto;
    private UserModel userModel;
    private UserModel savedUserModel;
    private UserResponseDto userResponseDto;
    private RoleModel roleModel;

    @BeforeEach
    void setUp() {
        roleModel = new RoleModel(1L, "PROPIETARIO");

        createUserRequestDto = new CreateUserRequestDto();
        createUserRequestDto.setName("Juan");
        createUserRequestDto.setLastName("Perez");
        createUserRequestDto.setDni("12345678");
        createUserRequestDto.setPhone("+573001234567");
        createUserRequestDto.setBirthDate(LocalDate.now().minusYears(25));
        createUserRequestDto.setEmail("juan@example.com");
        createUserRequestDto.setPassword("password123");

        userModel = new UserModel(null, "Juan", "Perez", "12345678", "+573001234567",
                LocalDate.now().minusYears(25), "juan@example.com", "password123", null);

        savedUserModel = new UserModel(1L, "Juan", "Perez", "12345678", "+573001234567",
                LocalDate.now().minusYears(25), "juan@example.com", "encodedPassword", roleModel);

        userResponseDto = new UserResponseDto();
        userResponseDto.setId(1L);
        userResponseDto.setName("Juan");
        userResponseDto.setLastName("Perez");
        userResponseDto.setDni("12345678");
        userResponseDto.setPhone("+573001234567");
        userResponseDto.setBirthDate(LocalDate.now().minusYears(25));
        userResponseDto.setEmail("juan@example.com");
        userResponseDto.setRoleName("PROPIETARIO");
    }

    // ===== createOwner tests =====

    @Test
    void createOwner_success_returnsUserResponseDto() {
        when(userRequestMapper.toUserModel(createUserRequestDto)).thenReturn(userModel);
        when(userServicePort.createOwner(userModel)).thenReturn(savedUserModel);
        when(userResponseMapper.toUserResponseDto(savedUserModel)).thenReturn(userResponseDto);

        UserResponseDto result = userHandler.createOwner(createUserRequestDto);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Juan", result.getName());
        assertEquals("PROPIETARIO", result.getRoleName());
        verify(userRequestMapper).toUserModel(createUserRequestDto);
        verify(userServicePort).createOwner(userModel);
        verify(userResponseMapper).toUserResponseDto(savedUserModel);
    }

    // ===== createEmployee tests =====

    @Test
    void createEmployee_success_returnsUserResponseDto() {
        RoleModel empleadoRole = new RoleModel(2L, "EMPLEADO");
        UserModel savedEmployee = new UserModel(2L, "Juan", "Perez", "12345678", "+573001234567",
                LocalDate.now().minusYears(25), "juan@example.com", "encodedPassword", empleadoRole);
        UserResponseDto employeeResponseDto = new UserResponseDto();
        employeeResponseDto.setId(2L);
        employeeResponseDto.setRoleName("EMPLEADO");

        when(userRequestMapper.toUserModel(createUserRequestDto)).thenReturn(userModel);
        when(userServicePort.createEmployee(userModel)).thenReturn(savedEmployee);
        when(userResponseMapper.toUserResponseDto(savedEmployee)).thenReturn(employeeResponseDto);

        UserResponseDto result = userHandler.createEmployee(createUserRequestDto);

        assertNotNull(result);
        assertEquals(2L, result.getId());
        assertEquals("EMPLEADO", result.getRoleName());
        verify(userRequestMapper).toUserModel(createUserRequestDto);
        verify(userServicePort).createEmployee(userModel);
        verify(userResponseMapper).toUserResponseDto(savedEmployee);
    }

    // ===== createClient tests =====

    @Test
    void createClient_success_returnsUserResponseDto() {
        RoleModel clienteRole = new RoleModel(3L, "CLIENTE");
        UserModel savedClient = new UserModel(3L, "Juan", "Perez", "12345678", "+573001234567",
                LocalDate.now().minusYears(25), "juan@example.com", "encodedPassword", clienteRole);
        UserResponseDto clientResponseDto = new UserResponseDto();
        clientResponseDto.setId(3L);
        clientResponseDto.setRoleName("CLIENTE");

        when(userRequestMapper.toUserModel(createUserRequestDto)).thenReturn(userModel);
        when(userServicePort.createClient(userModel)).thenReturn(savedClient);
        when(userResponseMapper.toUserResponseDto(savedClient)).thenReturn(clientResponseDto);

        UserResponseDto result = userHandler.createClient(createUserRequestDto);

        assertNotNull(result);
        assertEquals(3L, result.getId());
        assertEquals("CLIENTE", result.getRoleName());
        verify(userRequestMapper).toUserModel(createUserRequestDto);
        verify(userServicePort).createClient(userModel);
        verify(userResponseMapper).toUserResponseDto(savedClient);
    }

    // ===== getUserById tests =====

    @Test
    void getUserById_success_returnsUserResponseDto() {
        when(userServicePort.getUserById(1L)).thenReturn(savedUserModel);
        when(userResponseMapper.toUserResponseDto(savedUserModel)).thenReturn(userResponseDto);

        UserResponseDto result = userHandler.getUserById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(userServicePort).getUserById(1L);
        verify(userResponseMapper).toUserResponseDto(savedUserModel);
    }

    // ===== login tests =====

    @Test
    void login_success_returnsLoginResponseDto() {
        LoginRequestDto loginRequestDto = new LoginRequestDto();
        loginRequestDto.setEmail("juan@example.com");
        loginRequestDto.setPassword("password123");

        UserModel userWithEncodedPassword = new UserModel(1L, "Juan", "Perez", "12345678",
                "+573001234567", LocalDate.now().minusYears(25), "juan@example.com",
                "encodedPassword", roleModel);

        when(userServicePort.getUserByEmail("juan@example.com")).thenReturn(userWithEncodedPassword);
        when(passwordEncoderPort.matches("password123", "encodedPassword")).thenReturn(true);
        when(jwtService.generateToken(userWithEncodedPassword)).thenReturn("jwt.token.here");

        LoginResponseDto result = userHandler.login(loginRequestDto);

        assertNotNull(result);
        assertEquals("jwt.token.here", result.getToken());
        assertEquals("Bearer", result.getTokenType());
        verify(userServicePort).getUserByEmail("juan@example.com");
        verify(passwordEncoderPort).matches("password123", "encodedPassword");
        verify(jwtService).generateToken(userWithEncodedPassword);
    }

    @Test
    void login_invalidPassword_throwsInvalidCredentialsException() {
        LoginRequestDto loginRequestDto = new LoginRequestDto();
        loginRequestDto.setEmail("juan@example.com");
        loginRequestDto.setPassword("wrongPassword");

        UserModel userWithEncodedPassword = new UserModel(1L, "Juan", "Perez", "12345678",
                "+573001234567", LocalDate.now().minusYears(25), "juan@example.com",
                "encodedPassword", roleModel);

        when(userServicePort.getUserByEmail("juan@example.com")).thenReturn(userWithEncodedPassword);
        when(passwordEncoderPort.matches("wrongPassword", "encodedPassword")).thenReturn(false);

        assertThrows(InvalidCredentialsException.class,
                () -> userHandler.login(loginRequestDto));

        verify(jwtService, never()).generateToken(any());
    }
}
