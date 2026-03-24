package com.gastropolis.usuarios.infrastructure.input.rest;

import com.gastropolis.usuarios.application.dto.request.LoginRequestDto;
import com.gastropolis.usuarios.application.dto.response.LoginResponseDto;
import com.gastropolis.usuarios.application.handler.IUserHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthRestControllerTest {

    @Mock
    private IUserHandler userHandler;

    @InjectMocks
    private AuthRestController authRestController;

    private LoginRequestDto loginRequestDto;
    private LoginResponseDto loginResponseDto;

    @BeforeEach
    void setUp() {
        loginRequestDto = new LoginRequestDto();
        loginRequestDto.setEmail("juan@example.com");
        loginRequestDto.setPassword("password123");

        loginResponseDto = new LoginResponseDto("jwt.token.here");
    }

    // ===== login tests =====

    @Test
    void login_success_returns200WithToken() {
        when(userHandler.login(loginRequestDto)).thenReturn(loginResponseDto);

        ResponseEntity<LoginResponseDto> response = authRestController.login(loginRequestDto);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("jwt.token.here", response.getBody().getToken());
        assertEquals("Bearer", response.getBody().getTokenType());
        verify(userHandler).login(loginRequestDto);
    }
}
