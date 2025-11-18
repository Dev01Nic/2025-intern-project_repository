package com.portal.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;

    public SecurityConfig(CustomUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers("/hod/**").hasRole("HEAD_OF_DEPARTMENT")
                .requestMatchers("/aofficer/**").hasRole("ASSISTING_OFFICER")
                .requestMatchers("/hoo/create", "/hoo/save").permitAll()
                .requestMatchers("/hoo/view/**").hasAnyRole("HEAD_OF_OFFICE","ADMIN","HEAD_OF_DEPARTMENT")
                .requestMatchers("/hoo/**").hasRole("HEAD_OF_OFFICE")
                .requestMatchers("/department/**").hasRole("DEPARTMENT_USER")
                .requestMatchers("/department/projects").hasRole("DEPARTMENT_USER")// ✅ Add before anyRequest
                .anyRequest().permitAll()                                       // ✅ Last
            )
            .formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .successHandler((request, response, authentication) -> {
                    String role = authentication.getAuthorities().iterator().next().getAuthority();
                    switch (role) {
                        case "ROLE_ADMIN": response.sendRedirect("/admin"); break;
                        case "ROLE_HEAD_OF_DEPARTMENT": response.sendRedirect("/hod"); break;
                        case "ROLE_ASSISTING_OFFICER": response.sendRedirect("/aofficer"); break;
                        case "ROLE_HEAD_OF_OFFICE": response.sendRedirect("/hoo"); break;
                        case "ROLE_DEPARTMENT_USER": response.sendRedirect("/department/dashboard"); break;
                        default: response.sendRedirect("/");
                    }
                })
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout")
                .permitAll()
            )
            .csrf(csrf -> csrf.disable());

        return http.build();
    }
}