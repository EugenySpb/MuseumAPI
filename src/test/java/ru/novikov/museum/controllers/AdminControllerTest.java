package ru.novikov.museum.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ru.novikov.museum.dto.AdminCreateDTO;
import ru.novikov.museum.models.Admin;
import ru.novikov.museum.services.AdminService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AdminService adminService;

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void getByAdminId() throws Exception{
        Admin admin = new Admin(1L, "testUser", "password", "ROLE_ADMIN");

        Mockito.when(adminService.getAdminById(1L)).thenReturn(admin);

        mockMvc.perform(get("/api/admin/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("testUser"))
                .andExpect(jsonPath("$.role").value("ROLE_ADMIN"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void createAdmin() throws Exception{
        AdminCreateDTO adminCreateDTO = new AdminCreateDTO("newUser", "password", "ROLE_ADMIN");

        mockMvc.perform(post("/api/admin/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(adminCreateDTO)))
                .andExpect(status().isOk())
                .andExpect(content().string("Администратор успешно создан"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void updateAdmin() throws Exception{
        AdminCreateDTO adminCreateDTO = new AdminCreateDTO("updatedUser", "newPassword", "ROLE_ADMIN");

        mockMvc.perform(put("/api/admin/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(adminCreateDTO)))
                .andExpect(status().isOk())
                .andExpect(content().string("Администратор успешно изменен"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void deleteAdmin() throws Exception {
        mockMvc.perform(delete("/api/admin/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Администратор успешно удален"));
    }
}