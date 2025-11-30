package com.ues.edu.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.ues.edu.servicios.IUsuarioService;
@Configuration
public class SecurityConfig {

    @Autowired
    @Lazy
    private IUsuarioService usuarioService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(usuarioService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authenticationProvider(authProvider())
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
 
                .requestMatchers("/login", "/css/**", "/js/**", "/images/**", "/webjars/**").permitAll()
                
              
                .requestMatchers("/usuarios/**", "/roles/**", "/reportes/avanzados/**").hasRole("ADMIN")
                
                .requestMatchers(
                    "/barcos/**", "/navieras/**", "/modelos-barco/**",
                    "/cruceros/**", "/puertos/**", "/ciudades/**"
                ).hasAnyRole("ADMIN", "USER")
        
                .requestMatchers("/pasajeros/**").hasAnyRole("ADMIN", "USER")
                .requestMatchers("/reservas/**").hasAnyRole("ADMIN", "USER")
     
                .requestMatchers(
                    "/cruceros/lista", "/cruceros/ver/**"
                ).hasAnyRole("ADMIN")
    
                .requestMatchers("/reportes/**").hasAnyRole("ADMIN")
      
                .requestMatchers("/dashboard/**").authenticated()
                
                .anyRequest().authenticated()
            )
            .formLogin(login -> login
                .loginPage("/login")
                .permitAll()
                .defaultSuccessUrl("/dashboard", true)
                .failureUrl("/login?error=true")
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout=true")
                .permitAll()
            );

        return http.build();
    }
}