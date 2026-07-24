package com.joirv.CursoSpringBoot.infraestructure.security;

import com.joirv.CursoSpringBoot.domain.entities.UsersEntity;
import com.joirv.CursoSpringBoot.domain.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class TicketsGenerationUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;


    /**
     * Locates the user based on the username. In the actual implementation, the search
     * may possibly be case sensitive, or case insensitive depending on how the
     * implementation instance is configured. In this case, the <code>UserDetails</code>
     * object that comes back may have a username that is of a different case than what
     * was actually requested..
     *
     * @param username the username identifying the user whose data is required.
     * @return a fully populated user record (never <code>null</code>)
     * @throws UsernameNotFoundException if the user could not be found or the user has no
     *                                   GrantedAuthority
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UsersEntity users = userRepository.findByEmail(username)
                .orElseThrow(()-> new UsernameNotFoundException("User details not found the user:" + username));

//        List<GrantedAuthority> authorities = users.getRoles()
//                .stream()
//                .flatMap(role -> role.getAuthorities().stream())
//                .map(authority -> (GrantedAuthority) new SimpleGrantedAuthority(
//                        authority.getAuthorityName()
//                )).toList();

      List <GrantedAuthority> authorities = users.getRoles().getAuthorities()
              .stream()
              .map(authority -> (GrantedAuthority) new SimpleGrantedAuthority(
                      authority.getAuthorityName()
              )).toList();

        return User.withUsername(users.getEmail())
                .password(users.getPwd())
                .authorities(authorities)
                .disabled(!users.getEnabled())
                .build();

    }
}
