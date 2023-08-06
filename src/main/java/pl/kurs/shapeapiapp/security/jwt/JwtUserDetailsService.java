package pl.kurs.shapeapiapp.security.jwt;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pl.kurs.shapeapiapp.model.User;
import pl.kurs.shapeapiapp.repository.UserRepository;

@Service
public class JwtUserDetailsService implements UserDetailsService {
    private UserRepository userRepository;

    public JwtUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = userRepository.findByUsernameFetchRoles(username)
                .orElseThrow(
                        () -> new UsernameNotFoundException("USERNAME_NOT_FOUND")
                );
        return new UserPrincipal(user);
    }
}
