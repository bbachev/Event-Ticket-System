package eventticketsystem.gateway.security;

import eventticketsystem.gateway.dto.gateway.UserRole;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthFilter jwtAuthFilter) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, "/gateway/register", "/gateway/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/preferences/**", "/bookings/**").hasRole(UserRole.USER.toString())
                        .requestMatchers(HttpMethod.POST, "/events/**").hasRole(UserRole.ADMIN.toString())
                        .requestMatchers(HttpMethod.PUT, "/events/**").hasRole(UserRole.ADMIN.toString())
                        .requestMatchers(HttpMethod.DELETE, "/events/**").hasRole(UserRole.ADMIN.toString())
                        .requestMatchers(HttpMethod.GET, "/events/**", "/preferences/**", "/bookings/**")
                        .hasAnyRole(UserRole.USER.toString(), UserRole.ADMIN.toString())
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                ).addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}