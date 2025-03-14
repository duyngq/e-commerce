package com.store.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {
    private JwtRequestFilter jwtRequestFilter;

    public SecurityConfig(JwtRequestFilter jwtFilter) {
        this.jwtRequestFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**", "/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html").permitAll()
                        .requestMatchers("/api/v1/products/**", "/api/v1/discounts/**").hasAuthority("ADMIN")
                        .requestMatchers("/api/v1/customers/**").hasAuthority("CUSTOMER")
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

//    @Bean
//    public AuthenticationProvider authenticationProvider() {
//        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
//        provider.setUserDetailsService(userDetailsService());
//        provider.setPasswordEncoder(new BCryptPasswordEncoder());
//        return provider;
//    }
//
//    @Bean
//    public UserDetailsService userDetailsService() {
//        return username -> userRepository.findByUsername(username)
//                .map(user -> new User(user.getUsername(), user.getPassword(),
//                        List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()))))
//                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
//    }
}
