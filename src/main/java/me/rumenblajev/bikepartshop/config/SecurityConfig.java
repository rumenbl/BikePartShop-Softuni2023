package me.rumenblajev.bikepartshop.config;

import me.rumenblajev.bikepartshop.enums.RolesEnum;
import me.rumenblajev.bikepartshop.repositories.UserRepository;
import me.rumenblajev.bikepartshop.services.BikePartShopUserDetailsService;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.parameters.P;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests(authorizeRequests -> authorizeRequests
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                .requestMatchers("/").permitAll()
                .requestMatchers("/users/login", "/users/register", "/users/about").anonymous()
                .requestMatchers("/administration/**").hasRole(RolesEnum.ADMIN.name())
                .requestMatchers("/home","/users/profile").authenticated()
                .anyRequest()
                .authenticated())
            .formLogin(formLogin -> formLogin.loginPage("/users/login")
                .usernameParameter(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_USERNAME_KEY)
                .passwordParameter(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_PASSWORD_KEY)
                .defaultSuccessUrl("/users/home")
                .failureForwardUrl("/users/login-failed"))
            .logout(logout -> logout
                .logoutUrl("/users/logout")
                .logoutSuccessUrl("/")
                .invalidateHttpSession(true).
                deleteCookies("JSESSIONID"));

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepository) {
        return new BikePartShopUserDetailsService(userRepository);
    }
}