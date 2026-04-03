package com.gastropolis.usuarios.infrastructure.input.rest;

import com.gastropolis.usuarios.application.dto.request.CreateUserRequestDto;
import com.gastropolis.usuarios.application.dto.response.UserResponseDto;
import com.gastropolis.usuarios.application.handler.IUserHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
@Tag(name = "User Management", description = "Endpoints for managing users")
public class UserRestController {

    private final IUserHandler userHandler;

    public UserRestController(IUserHandler userHandler) {
        this.userHandler = userHandler;
    }

    @Operation(
            summary = "Create Owner (HU1)",
            description = "Creates a new owner user. Requires ADMINISTRADOR role. BirthDate is mandatory and user must be 18+."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Owner created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data or user under 18"),
            @ApiResponse(responseCode = "409", description = "User already exists with given email or DNI"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @PostMapping("/")
    @PreAuthorize("hasAuthority('ADMINISTRADOR')")
    public ResponseEntity<UserResponseDto> createOwner(@Valid @RequestBody CreateUserRequestDto createUserRequestDto) {
        UserResponseDto response = userHandler.createOwner(createUserRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(
            summary = "Create Employee (HU6)",
            description = "Creates a new employee user. Requires PROPIETARIO role. BirthDate is optional."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Employee created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "409", description = "User already exists with given email or DNI"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @PostMapping("/owner/employee")
    @PreAuthorize("hasAuthority('PROPIETARIO')")
    public ResponseEntity<UserResponseDto> createEmployee(@Valid @RequestBody CreateUserRequestDto createUserRequestDto) {
        UserResponseDto response = userHandler.createEmployee(createUserRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(
            summary = "Create Client (HU8)",
            description = "Creates a new client user. Public endpoint. BirthDate is optional."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Client created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "409", description = "User already exists with given email or DNI")
    })
    @PostMapping("/client")
    public ResponseEntity<UserResponseDto> createClient(@Valid @RequestBody CreateUserRequestDto createUserRequestDto) {
        UserResponseDto response = userHandler.createClient(createUserRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(
            summary = "Get user by ID",
            description = "Retrieves a user by their ID. Used internally by other microservices. Requires authentication."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable Long id) {
        UserResponseDto response = userHandler.getUserById(id);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Get user by DNI",
            description = "Retrieves a user by their DNI. Used internally by other microservices. Requires authentication."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @GetMapping("/dni/{dni}")
    public ResponseEntity<UserResponseDto> getUserByDni(@PathVariable String dni) {
        UserResponseDto response = userHandler.getUserByDni(dni);
        return ResponseEntity.ok(response);
    }
}
