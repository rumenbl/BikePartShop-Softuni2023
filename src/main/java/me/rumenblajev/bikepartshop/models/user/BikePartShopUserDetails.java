package me.rumenblajev.bikepartshop.models.user;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class BikePartShopUserDetails implements UserDetails {
    private final Long id;
    @Getter
    private final String username;
    @Getter
    private final String firstName;
    @Getter
    private final String lastName;
    @Getter
    private final String password;
    private final Collection<GrantedAuthority> authorities;

    public BikePartShopUserDetails(Long id,
                                   String username,
                                   String firstName,
                                   String lastName,
                                   String password,
                                   Collection<GrantedAuthority> authorities) {
        this.id = id;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.authorities = authorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
