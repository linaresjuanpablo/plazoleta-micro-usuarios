package com.gastropolis.usuarios.infrastructure.configuration;

import com.gastropolis.usuarios.infrastructure.out.jpa.entity.RoleEntity;
import com.gastropolis.usuarios.infrastructure.out.jpa.entity.UserEntity;
import com.gastropolis.usuarios.infrastructure.out.jpa.repository.IRoleRepository;
import com.gastropolis.usuarios.infrastructure.out.jpa.repository.IUserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private final IRoleRepository roleRepository;
    private final IUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(IRoleRepository roleRepository,
                           IUserRepository userRepository,
                           PasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        seedRoles();
        seedAdminUser();
    }

    private void seedRoles() {
        List<String> roles = List.of("ADMINISTRADOR", "PROPIETARIO", "EMPLEADO", "CLIENTE");
        for (String roleName : roles) {
            if (roleRepository.findByName(roleName).isEmpty()) {
                RoleEntity role = new RoleEntity();
                role.setName(roleName);
                roleRepository.save(role);
            }
        }
    }

    private void seedAdminUser() {
        String adminEmail = "admin@plazoleta.com";
        if (!userRepository.existsByEmail(adminEmail)) {
            RoleEntity adminRole = roleRepository.findByName("ADMINISTRADOR")
                    .orElseThrow(() -> new IllegalStateException("Rol ADMINISTRADOR no encontrado"));

            UserEntity admin = new UserEntity();
            admin.setName("Admin");
            admin.setLastName("Sistema");
            admin.setDni("0000000001");
            admin.setPhone("3000000000");
            admin.setBirthDate(LocalDate.of(1990, 1, 1));
            admin.setEmail(adminEmail);
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRole(adminRole);

            userRepository.save(admin);
        }
    }
}
