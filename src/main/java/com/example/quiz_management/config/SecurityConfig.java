package com.example.quiz_management.config;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig {
    private UserDetailsService userDetailsService;
    private CustomSuccessHandler successHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity.csrf(csrf-> csrf.disable())
                .authorizeHttpRequests(auth -> auth

                        // Public pages
                        .requestMatchers("/", "/index", "/register", "/register/**",
                                "/static/**", "/css/**", "/js/**", "/images/**", "/posters/**").permitAll()

                        // Only USER can book tickets
                        .requestMatchers("/participants/**").hasRole("PARTICIPANT")

                        // Only ADMIN can manage movies
                        .requestMatchers("/admin/**").hasRole("ADMIN")

                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")                 // thymeleaf login
                        .loginProcessingUrl("/login")      // post login handle by spring security
                        .successHandler(successHandler)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                );

        return httpSecurity.build();
    }
}


