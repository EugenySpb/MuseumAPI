package ru.novikov.museum.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.novikov.museum.models.Admin;
import ru.novikov.museum.repositories.AdminRepository;
import ru.novikov.museum.util.ResourceNotFoundException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminService implements UserDetailsService {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return adminRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Не удалось получить пользователя: " + username));
    }

    public Admin getAdminById(Long id) {
        return adminRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Администратор не найден"));
    }

    @Transactional
    public void createAdmin(Admin admin) {
        admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        adminRepository.save(admin);
    }

    @Transactional
    public void updateAdmin(Long id, Admin adminDetails) {
        Admin admin = adminRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("Админ с идентификатором " + id + " не найден"));

        admin.setUsername(adminDetails.getUsername());
        admin.setPassword(passwordEncoder.encode(adminDetails.getPassword()));
        admin.setRole(adminDetails.getRole());

        adminRepository.save(admin);
    }

    @Transactional
    public void deleteAdmin(Long id) {
        Admin admin = adminRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("Админ с идентификатором " + id + " не найден"));
        adminRepository.delete(admin);
    }

    public Admin getCurrentAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            String username = ((UserDetails) authentication.getPrincipal()).getUsername();
            return adminRepository.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("Администратор с именем пользователя " + username + " не найден"));
        }
        throw new UsernameNotFoundException("Не найден авторизованный администратор");
    }
}
