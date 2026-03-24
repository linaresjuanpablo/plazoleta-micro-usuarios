package com.gastropolis.usuarios.infrastructure.out.jpa.adapter;

import com.gastropolis.usuarios.domain.model.RoleModel;
import com.gastropolis.usuarios.domain.model.UserModel;
import com.gastropolis.usuarios.infrastructure.out.jpa.entity.RoleEntity;
import com.gastropolis.usuarios.infrastructure.out.jpa.entity.UserEntity;
import com.gastropolis.usuarios.infrastructure.out.jpa.mapper.IUserEntityMapper;
import com.gastropolis.usuarios.infrastructure.out.jpa.repository.IRoleRepository;
import com.gastropolis.usuarios.infrastructure.out.jpa.repository.IUserRepository;
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
class UserJpaAdapterTest {

    @Mock
    private IUserRepository userRepository;

    @Mock
    private IRoleRepository roleRepository;

    @Mock
    private IUserEntityMapper userEntityMapper;

    @InjectMocks
    private UserJpaAdapter userJpaAdapter;

    private UserModel userModel;
    private UserEntity userEntity;
    private UserEntity savedUserEntity;
    private RoleModel roleModel;
    private RoleEntity roleEntity;

    @BeforeEach
    void setUp() {
        roleModel = new RoleModel(1L, "PROPIETARIO");
        roleEntity = new RoleEntity(1L, "PROPIETARIO");

        userModel = new UserModel(null, "Juan", "Perez", "12345678", "+573001234567",
                LocalDate.now().minusYears(25), "juan@example.com", "encodedPassword", roleModel);

        userEntity = new UserEntity();
        userEntity.setName("Juan");
        userEntity.setLastName("Perez");
        userEntity.setDni("12345678");
        userEntity.setPhone("+573001234567");
        userEntity.setBirthDate(LocalDate.now().minusYears(25));
        userEntity.setEmail("juan@example.com");
        userEntity.setPassword("encodedPassword");
        userEntity.setRole(roleEntity);

        savedUserEntity = new UserEntity();
        savedUserEntity.setId(1L);
        savedUserEntity.setName("Juan");
        savedUserEntity.setLastName("Perez");
        savedUserEntity.setDni("12345678");
        savedUserEntity.setPhone("+573001234567");
        savedUserEntity.setBirthDate(LocalDate.now().minusYears(25));
        savedUserEntity.setEmail("juan@example.com");
        savedUserEntity.setPassword("encodedPassword");
        savedUserEntity.setRole(roleEntity);
    }

    // ===== saveUser tests =====

    @Test
    void saveUser_success_returnsSavedUserModel() {
        UserModel expectedResult = new UserModel(1L, "Juan", "Perez", "12345678", "+573001234567",
                LocalDate.now().minusYears(25), "juan@example.com", "encodedPassword", roleModel);

        when(userEntityMapper.toUserEntity(userModel)).thenReturn(userEntity);
        when(userRepository.save(userEntity)).thenReturn(savedUserEntity);
        when(userEntityMapper.toUserModel(savedUserEntity)).thenReturn(expectedResult);

        UserModel result = userJpaAdapter.saveUser(userModel);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Juan", result.getName());
        verify(userEntityMapper).toUserEntity(userModel);
        verify(userRepository).save(userEntity);
        verify(userEntityMapper).toUserModel(savedUserEntity);
    }

    // ===== findUserById tests =====

    @Test
    void findUserById_userExists_returnsOptionalWithUser() {
        UserModel expectedUser = new UserModel(1L, "Juan", "Perez", "12345678", "+573001234567",
                LocalDate.now().minusYears(25), "juan@example.com", "encodedPassword", roleModel);

        when(userRepository.findById(1L)).thenReturn(Optional.of(savedUserEntity));
        when(userEntityMapper.toUserModel(savedUserEntity)).thenReturn(expectedUser);

        Optional<UserModel> result = userJpaAdapter.findUserById(1L);

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
        verify(userRepository).findById(1L);
        verify(userEntityMapper).toUserModel(savedUserEntity);
    }

    @Test
    void findUserById_userNotExists_returnsEmptyOptional() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<UserModel> result = userJpaAdapter.findUserById(99L);

        assertFalse(result.isPresent());
        verify(userRepository).findById(99L);
        verify(userEntityMapper, never()).toUserModel(any(UserEntity.class));
    }

    // ===== findUserByEmail tests =====

    @Test
    void findUserByEmail_userExists_returnsOptionalWithUser() {
        UserModel expectedUser = new UserModel(1L, "Juan", "Perez", "12345678", "+573001234567",
                LocalDate.now().minusYears(25), "juan@example.com", "encodedPassword", roleModel);

        when(userRepository.findByEmail("juan@example.com")).thenReturn(Optional.of(savedUserEntity));
        when(userEntityMapper.toUserModel(savedUserEntity)).thenReturn(expectedUser);

        Optional<UserModel> result = userJpaAdapter.findUserByEmail("juan@example.com");

        assertTrue(result.isPresent());
        assertEquals("juan@example.com", result.get().getEmail());
        verify(userRepository).findByEmail("juan@example.com");
    }

    @Test
    void findUserByEmail_userNotExists_returnsEmptyOptional() {
        when(userRepository.findByEmail("unknown@example.com")).thenReturn(Optional.empty());

        Optional<UserModel> result = userJpaAdapter.findUserByEmail("unknown@example.com");

        assertFalse(result.isPresent());
        verify(userRepository).findByEmail("unknown@example.com");
        verify(userEntityMapper, never()).toUserModel(any(UserEntity.class));
    }

    // ===== findUserByDni tests =====

    @Test
    void findUserByDni_userExists_returnsOptionalWithUser() {
        UserModel expectedUser = new UserModel(1L, "Juan", "Perez", "12345678", "+573001234567",
                LocalDate.now().minusYears(25), "juan@example.com", "encodedPassword", roleModel);

        when(userRepository.findByDni("12345678")).thenReturn(Optional.of(savedUserEntity));
        when(userEntityMapper.toUserModel(savedUserEntity)).thenReturn(expectedUser);

        Optional<UserModel> result = userJpaAdapter.findUserByDni("12345678");

        assertTrue(result.isPresent());
        assertEquals("12345678", result.get().getDni());
        verify(userRepository).findByDni("12345678");
    }

    @Test
    void findUserByDni_userNotExists_returnsEmptyOptional() {
        when(userRepository.findByDni("99999999")).thenReturn(Optional.empty());

        Optional<UserModel> result = userJpaAdapter.findUserByDni("99999999");

        assertFalse(result.isPresent());
        verify(userRepository).findByDni("99999999");
        verify(userEntityMapper, never()).toUserModel(any(UserEntity.class));
    }

    // ===== findRoleByName tests =====

    @Test
    void findRoleByName_roleExists_returnsOptionalWithRole() {
        when(roleRepository.findByName("PROPIETARIO")).thenReturn(Optional.of(roleEntity));
        when(userEntityMapper.toRoleModel(roleEntity)).thenReturn(roleModel);

        Optional<RoleModel> result = userJpaAdapter.findRoleByName("PROPIETARIO");

        assertTrue(result.isPresent());
        assertEquals("PROPIETARIO", result.get().getName());
        verify(roleRepository).findByName("PROPIETARIO");
        verify(userEntityMapper).toRoleModel(roleEntity);
    }

    @Test
    void findRoleByName_roleNotExists_returnsEmptyOptional() {
        when(roleRepository.findByName("UNKNOWN_ROLE")).thenReturn(Optional.empty());

        Optional<RoleModel> result = userJpaAdapter.findRoleByName("UNKNOWN_ROLE");

        assertFalse(result.isPresent());
        verify(roleRepository).findByName("UNKNOWN_ROLE");
        verify(userEntityMapper, never()).toRoleModel(any(RoleEntity.class));
    }

    // ===== existsUserByEmail tests =====

    @Test
    void existsUserByEmail_emailExists_returnsTrue() {
        when(userRepository.existsByEmail("juan@example.com")).thenReturn(true);

        boolean result = userJpaAdapter.existsUserByEmail("juan@example.com");

        assertTrue(result);
        verify(userRepository).existsByEmail("juan@example.com");
    }

    @Test
    void existsUserByEmail_emailNotExists_returnsFalse() {
        when(userRepository.existsByEmail("unknown@example.com")).thenReturn(false);

        boolean result = userJpaAdapter.existsUserByEmail("unknown@example.com");

        assertFalse(result);
        verify(userRepository).existsByEmail("unknown@example.com");
    }

    // ===== existsUserByDni tests =====

    @Test
    void existsUserByDni_dniExists_returnsTrue() {
        when(userRepository.existsByDni("12345678")).thenReturn(true);

        boolean result = userJpaAdapter.existsUserByDni("12345678");

        assertTrue(result);
        verify(userRepository).existsByDni("12345678");
    }

    @Test
    void existsUserByDni_dniNotExists_returnsFalse() {
        when(userRepository.existsByDni("99999999")).thenReturn(false);

        boolean result = userJpaAdapter.existsUserByDni("99999999");

        assertFalse(result);
        verify(userRepository).existsByDni("99999999");
    }
}
