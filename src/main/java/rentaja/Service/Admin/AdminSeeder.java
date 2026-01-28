package rentaja.Service.Admin;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import rentaja.Entity.User;
import rentaja.Entity.Enums.Role;
import rentaja.Repository.UserRepository;

@Configuration
public class AdminSeeder implements CommandLineRunner {
    private final UserRepository repo;
    private final PasswordEncoder encoder;

    public AdminSeeder(UserRepository repo, PasswordEncoder encoder) {
        this.repo = repo;
        this.encoder = encoder;
    }

    @Override
    public void run(String... args) throws Exception {
        if (repo.findByEmail("admin@rentaja.com").isEmpty()) {
            User admin = new User();
            admin.setEmail("admin@rentaja.com");
            admin.setPassword(encoder.encode("Admin123!"));
            admin.setRole(Role.ROLE_ADMIN);

            repo.save(admin);
        }
    }
}
