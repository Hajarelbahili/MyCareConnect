package com.project.mycareconnect.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {


    private final UserDetailsService userDetailsService;

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(UserDetailsService userDetailsService,
                          JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.userDetailsService = userDetailsService;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);  // force 12, tu peux adapter
    }

    @Bean
    public AuthenticationProvider authProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(new BCryptPasswordEncoder(12));
        return provider;
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                                // ================= ADMIN =================
                                .requestMatchers("/api/doctors/create").hasRole("ADMIN")
                                .requestMatchers("/api/assistants/create").hasRole("ADMIN")
                                .requestMatchers("/api/assistants/update/**").hasRole("ADMIN")
                                .requestMatchers("/api/update/**").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.DELETE, "/users/delete/**").hasRole("ADMIN")
                                .requestMatchers("/api/departments/create").hasRole("ADMIN")
                                .requestMatchers("/api/departments/update/**").hasRole("ADMIN")
                                .requestMatchers("/api/departments/delete/**").hasRole("ADMIN")
                                .requestMatchers("/api/departments/changestatus/**").hasRole("ADMIN")
                                .requestMatchers("/api/specialities/create").hasRole("ADMIN")
                                .requestMatchers("/api/specialities/update/**").hasRole("ADMIN")
                                .requestMatchers("/api/specialities/changestatus/**").hasRole("ADMIN")

                             // ================= ASSISTANT =================
                                .requestMatchers("/api/assistants/update-me/**").hasRole("ASSISTANT")
                                .requestMatchers("/api/assistants/getme").hasRole("ASSISTANT")
                                .requestMatchers("/api/assistants/uploadDoc").hasRole("ASSISTANT")
                                .requestMatchers("/api/appointments/createByassistant").hasRole("ASSISTANT")
                                .requestMatchers("/api/dataroom/upload").hasRole("ASSISTANT")
                                .requestMatchers("/api/dataroom/delete/**").hasRole("ASSISTANT")
                                .requestMatchers("/api/patients/delete/**").hasAnyRole("ADMIN","ASSISTANT")
                                .requestMatchers("/api/patients/update/**").hasRole("ASSISTANT")

                                   // ================= PATIENT =================
                                .requestMatchers("/api/patients/create").hasAnyRole("PATIENT","ASSISTANT")
                                .requestMatchers("/api/appointments/create").hasRole("PATIENT")

                                      // ================= DOCTOR =================
                                .requestMatchers("/api/doctors/update-me").hasRole("DOCTOR")
                                .requestMatchers("/api/availabilities/**").hasRole("DOCTOR")
                                .requestMatchers("/api/doctors/appointments/**").hasRole("DOCTOR")

                                          // ================= PUBLIC / AUTHENTIFIÉ =================

                                .requestMatchers("/api/auth/**").permitAll()

                                .requestMatchers("/api/appointments/search").permitAll()

                                .anyRequest().authenticated()
                )
                .authenticationProvider(authProvider())
        //permet à ton JwtAuthenticationFilter de s’exécuter avant Spring Security ne vérifie l’authentification standard (UsernamePasswordAuthenticationFilter).
        //
        //Cela garantit que Spring Security connaît déjà l’utilisateur authentifié via JWT avant de sécuriser les endpoints.
        .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
