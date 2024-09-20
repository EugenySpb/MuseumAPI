package ru.novikov.museum.controllers;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

    @Autowired
    public AdminController(AdminService adminService, ModelMapper modelMapper) {
        this.adminService = adminService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/{id}")
    public AdminDTO getByAdminId(@PathVariable Long id) {
        return convertToAdminDTO(adminService.getAdminById(id));
    }

    @PostMapping("/create")
    public ResponseEntity<String> createAdmin(@RequestBody @Valid AdminCreateDTO adminCreateDTO, BindingResult bindingResult) {
        Admin adminToCreate = convertToAdmin(adminCreateDTO);

        if (bindingResult.hasErrors()) {
            returnErrorsAdminToClient(bindingResult);
        }

        adminService.createAdmin(adminToCreate);

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

    private Admin convertToAdmin(AdminCreateDTO adminCreateDTO) {
        return modelMapper.map(adminCreateDTO, Admin.class);
    }
    private AdminDTO convertToAdminDTO(Admin admin) {
        return modelMapper.map(admin, AdminDTO.class);
    }
}
