package me.rumenblajev.bikepartshop.services;

import lombok.RequiredArgsConstructor;
import me.rumenblajev.bikepartshop.enums.RolesEnum;
import me.rumenblajev.bikepartshop.models.entity.User;
import me.rumenblajev.bikepartshop.models.user.BikePartShopUserDetails;
import me.rumenblajev.bikepartshop.repositories.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@RequiredArgsConstructor
public class BikePartShopUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findOneByUsername(username);
        if(user.isEmpty()) {
            throw new UsernameNotFoundException("User not found");
        }

        if(user.get().getRole().getName().equals(RolesEnum.USER)) {
            return regularUserRoleMap(user.get());
        } else if(user.get().getRole().getName().equals(RolesEnum.ADMIN)){
            return adminUserRoleMap(user.get());
        }
        else {
            return null;
        }
    }

    public UserDetails regularUserRoleMap(final User user) {
        Set<GrantedAuthority> grantedAuthoritySet = new HashSet<>();
        grantedAuthoritySet.add(new SimpleGrantedAuthority("ROLE_USER"));
        return new BikePartShopUserDetails(
                user.getId(),
                user.getUsername(),
                user.getFirstName(),
                user.getLastName(),
                user.getPassword(),
                grantedAuthoritySet
        );
    }

    public UserDetails adminUserRoleMap(final User user) {
        Set<GrantedAuthority> grantedAuthoritySet = new HashSet<>();
        grantedAuthoritySet.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        return new BikePartShopUserDetails(
                user.getId(),
                user.getUsername(),
                user.getFirstName(),
                user.getLastName(),
                user.getPassword(),
                grantedAuthoritySet
        );
    }
}
