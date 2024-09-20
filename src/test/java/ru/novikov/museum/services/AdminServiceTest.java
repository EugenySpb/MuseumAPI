package ru.novikov.museum.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.novikov.museum.models.Admin;
import ru.novikov.museum.repositories.AdminRepository;

import java.util.Optional;

@SpringBootTest
class AdminServiceTest {

    @Autowired
    private AdminService adminService;

    @MockBean
    private AdminRepository adminRepository;

    @MockBean
    private PasswordEncoder passwordEncoder;

    private Admin admin;
    private Admin updatedAdmin;

    @BeforeEach
    void setUp() {
        admin = new Admin(1L, "testUser", "password", "ROLE_ADMIN");
        updatedAdmin = new Admin(1L, "updatedUser", "newPassword", "ROLE_ADMIN");
    }

    @Test
    void getAdminById() {
        Mockito.when(adminRepository.findById(1L)).thenReturn(Optional.of(admin));

        Admin foundAdmin = adminService.getAdminById(1L);
        Assertions.assertEquals("testUser", foundAdmin.getUsername());
    }

    @Test
    void createAdmin() {
        Mockito.when(passwordEncoder.encode(admin.getPassword())).thenReturn("encodedPassword");

        adminService.createAdmin(admin);

        Mockito.verify(adminRepository, Mockito.times(1)).save(admin);
        Assertions.assertEquals("encodedPassword", admin.getPassword());
    }

    @Test
    void updateAdmin() {
        Mockito.when(adminRepository.findById(1L)).thenReturn(Optional.of(admin));
        Mockito.when(passwordEncoder.encode(updatedAdmin.getPassword())).thenReturn("encodedNewPassword");

        adminService.updateAdmin(1L, updatedAdmin);

        Assertions.assertEquals("updatedUser", admin.getUsername());
        Assertions.assertEquals("encodedNewPassword", admin.getPassword());
        Assertions.assertEquals("ROLE_ADMIN", admin.getRole());

        Mockito.verify(adminRepository, Mockito.times(1)).save(admin);
    }

    @Test
    void deleteAdmin() {
        Mockito.when(adminRepository.findById(1L)).thenReturn(Optional.of(admin));

        adminService.deleteAdmin(1L);

        Mockito.verify(adminRepository, Mockito.times(1)).delete(admin);
    }
}