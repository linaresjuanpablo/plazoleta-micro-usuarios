package com.gastropolis.usuarios.infrastructure.input.rest;

import com.gastropolis.usuarios.application.dto.request.LoginRequestDto;
import com.gastropolis.usuarios.application.dto.response.LoginResponseDto;
import com.gastropolis.usuarios.application.handler.IUserHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Authentication", description = "Endpoints for user authentication")
public class AuthRestController {

    private final IUserHandler userHandler;

    public AuthRestController(IUserHandler userHandler) {
        this.userHandler = userHandler;
    }

    @Operation(
            summary = "Login (HU5)",
            description = "Authenticates a user by email and password. Returns a JWT token valid for 24 hours."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login successful, JWT token returned"),
            @ApiResponse(responseCode = "401", description = "Invalid credentials"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@Valid @RequestBody LoginRequestDto loginRequestDto) {
        LoginResponseDto response = userHandler.login(loginRequestDto);
        return ResponseEntity.ok(response);
    }
}
