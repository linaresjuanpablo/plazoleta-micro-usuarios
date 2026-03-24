package com.gastropolis.usuarios.infrastructure.input.rest;

import com.gastropolis.usuarios.application.dto.request.CreateUserRequestDto;
import com.gastropolis.usuarios.application.dto.response.UserResponseDto;
import com.gastropolis.usuarios.application.handler.IUserHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserRestControllerTest {

    @Mock
    private IUserHandler userHandler;

    @InjectMocks
    private UserRestController userRestController;

    private CreateUserRequestDto createUserRequestDto;
    private UserResponseDto userResponseDto;

    @BeforeEach
    void setUp() {
        createUserRequestDto = new CreateUserRequestDto();
        createUserRequestDto.setName("Juan");
        createUserRequestDto.setLastName("Perez");
        createUserRequestDto.setDni("12345678");
        createUserRequestDto.setPhone("+573001234567");
        createUserRequestDto.setBirthDate(LocalDate.now().minusYears(25));
        createUserRequestDto.setEmail("juan@example.com");
        createUserRequestDto.setPassword("password123");

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
    void createOwner_success_returns201WithBody() {
        when(userHandler.createOwner(createUserRequestDto)).thenReturn(userResponseDto);

        ResponseEntity<UserResponseDto> response = userRestController.createOwner(createUserRequestDto);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getId());
        assertEquals("Juan", response.getBody().getName());
        assertEquals("PROPIETARIO", response.getBody().getRoleName());
        verify(userHandler).createOwner(createUserRequestDto);
    }

    // ===== createEmployee tests =====

    @Test
    void createEmployee_success_returns201WithBody() {
        UserResponseDto employeeResponseDto = new UserResponseDto();
        employeeResponseDto.setId(2L);
        employeeResponseDto.setName("Juan");
        employeeResponseDto.setEmail("juan@example.com");
        employeeResponseDto.setRoleName("EMPLEADO");

        when(userHandler.createEmployee(createUserRequestDto)).thenReturn(employeeResponseDto);

        ResponseEntity<UserResponseDto> response = userRestController.createEmployee(createUserRequestDto);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2L, response.getBody().getId());
        assertEquals("EMPLEADO", response.getBody().getRoleName());
        verify(userHandler).createEmployee(createUserRequestDto);
    }

    // ===== createClient tests =====

    @Test
    void createClient_success_returns201WithBody() {
        UserResponseDto clientResponseDto = new UserResponseDto();
        clientResponseDto.setId(3L);
        clientResponseDto.setName("Juan");
        clientResponseDto.setEmail("juan@example.com");
        clientResponseDto.setRoleName("CLIENTE");

        when(userHandler.createClient(createUserRequestDto)).thenReturn(clientResponseDto);

        ResponseEntity<UserResponseDto> response = userRestController.createClient(createUserRequestDto);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(3L, response.getBody().getId());
        assertEquals("CLIENTE", response.getBody().getRoleName());
        verify(userHandler).createClient(createUserRequestDto);
    }

    // ===== getUserById tests =====

    @Test
    void getUserById_success_returns200WithBody() {
        when(userHandler.getUserById(1L)).thenReturn(userResponseDto);

        ResponseEntity<UserResponseDto> response = userRestController.getUserById(1L);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getId());
        assertEquals("juan@example.com", response.getBody().getEmail());
        verify(userHandler).getUserById(1L);
    }
}
