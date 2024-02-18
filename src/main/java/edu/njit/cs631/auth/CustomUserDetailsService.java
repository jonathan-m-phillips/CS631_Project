package edu.njit.cs631.auth;

import edu.njit.cs631.models.CustomUser;
import edu.njit.cs631.repositories.CustomUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private CustomUserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<CustomUser> user = repository.findByEmail(email);
        if (user.isPresent()) {
            var userObject = user.get();
            return User.builder()
                    .username(userObject.getEmail())
                    .password(userObject.getPassword())
                    .roles(getRoles(userObject))
                    .build();
        }

        throw new UsernameNotFoundException("User not found with email: " + email);
    }

    private String getRoles(CustomUser user) {
        return user.getRole();
    }
}
