package rentaja.Config.Security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import rentaja.Exception.Exceptions.NotFoundException;
import rentaja.Repository.UserRepository;

@Service
public class EmailDetailsService implements UserDetailsService {
    private final UserRepository repo;

    public EmailDetailsService(UserRepository repo) {
        this.repo = repo;
    }

    // Custom jwt userdetails, dari username ke email
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return repo.findByEmail(email).orElseThrow(() -> new NotFoundException("Email is not found"));
    }

}
