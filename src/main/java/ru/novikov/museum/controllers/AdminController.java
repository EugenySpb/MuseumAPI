package ru.novikov.museum.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.novikov.museum.dto.AdminCreateDTO;
import ru.novikov.museum.dto.AdminDTO;
import ru.novikov.museum.models.Admin;
import ru.novikov.museum.services.AdminService;

import static ru.novikov.museum.util.ErrorsUtil.returnErrorsAdminToClient;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;
    private final ModelMapper modelMapper;
    private final SecurityContextLogoutHandler securityContextLogoutHandler = new SecurityContextLogoutHandler();
    private static final Logger LOGGER = LoggerFactory.getLogger(AdminController.class);

    @Autowired
    public AdminController(AdminService adminService, ModelMapper modelMapper) {
        this.adminService = adminService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/{id}")
    public AdminDTO getByAdminId(@PathVariable Long id) {
        LOGGER.info("Запрос на получение администратора с ID: {}", id);
        return convertToAdminDTO(adminService.getAdminById(id));
    }

    @PostMapping("/create")
    public ResponseEntity<String> createAdmin(@RequestBody @Valid AdminCreateDTO adminCreateDTO, BindingResult bindingResult) {
        Admin adminToCreate = convertToAdmin(adminCreateDTO);
        LOGGER.info("Создание администратора: {}", adminCreateDTO);
        if (bindingResult.hasErrors()) {
            LOGGER.error("Ошибки в данных администратора: {}", bindingResult.getAllErrors());
            returnErrorsAdminToClient(bindingResult);
        }

        adminService.createAdmin(adminToCreate);
        LOGGER.info("Администратор успешно создан");
        return ResponseEntity.ok("Администратор успешно создан");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateAdmin(@PathVariable Long id, @RequestBody @Valid AdminCreateDTO adminCreateDTO, BindingResult bindingResult) {
        Admin adminToUpdate = convertToAdmin(adminCreateDTO);

        if (bindingResult.hasErrors()) {
            returnErrorsAdminToClient(bindingResult);
        }
        adminService.updateAdmin(id, adminToUpdate);
        return ResponseEntity.ok("Администратор успешно изменен");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAdmin(@PathVariable Long id) {
        adminService.deleteAdmin(id);
        return ResponseEntity.ok("Администратор успешно удален");
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        LOGGER.info("Запрос на выход администратора");
        securityContextLogoutHandler.logout(request, response, authentication);
        return ResponseEntity.noContent().build();
    }


    private Admin convertToAdmin(AdminCreateDTO adminCreateDTO) {
        return modelMapper.map(adminCreateDTO, Admin.class);
    }

    private AdminDTO convertToAdminDTO(Admin admin) {
        return modelMapper.map(admin, AdminDTO.class);
    }
}
