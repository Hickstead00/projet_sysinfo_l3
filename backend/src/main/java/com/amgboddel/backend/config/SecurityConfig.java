package com.amgboddel.backend.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        // Auth ouvert à tous
                        .requestMatchers("/api/auth/**").permitAll()
                        // Swagger accessible sans login
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        // Maquettes : RESPONSABLE et ADMIN uniquement
                        .requestMatchers("/api/maquettes/**").hasAnyRole("RESPONSABLE", "ADMIN")
                        // Tout le reste de /api/** : il faut être connecté (peu importe le rôle)
                        .requestMatchers("/api/**").authenticated()
                )

                .exceptionHandling(ex -> ex
                        // Non connecté 401
                        .authenticationEntryPoint((request, response, authException) ->
                                response.sendError(HttpStatus.UNAUTHORIZED.value(), "Non authentifié"))
                        // Connecté mais pas le bon rôle 403
                        .accessDeniedHandler((request, response, accessDeniedException) ->
                                response.sendError(HttpStatus.FORBIDDEN.value(), "Accès refusé"))
                );

        return http.build();
    }

    // Je reprend le Cors qui avait été établi de base car il aurait été overwrite par celui ci quoi qu'il arrive.
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:4200"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/**", config);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
