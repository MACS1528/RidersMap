package com.ridersmap.mapa_riders_backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .cors(Customizer.withDefaults())
            .csrf(csrf -> csrf.disable()) 
            // IMPORTANTE: Habilitamos Basic Auth para que reconozca el usuario/pass enviado desde JS
            .httpBasic(Customizer.withDefaults())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                // Permitimos opciones de CORS
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                // El registro es abierto para todos
                .requestMatchers("/api/usuarios/register").permitAll()

                // REGLA DE BORRADO PARA ADMIN (Opción Authority) ---
                // Usamos hasAuthority("ADMIN") para que coincida exactamente con "ADMIN" de la base de datos
                // Solo el administrador puede borrar usuarios
                .requestMatchers(HttpMethod.DELETE, "/api/usuarios/**").hasAuthority("ADMIN")

                // Permitimos que cualquier usuario AUTENTICADO (User o Admin) pida el borrado de sus cosas.
                // La validación de si es el dueño o el admin la hace api.js y el controlador.
                .requestMatchers(HttpMethod.DELETE, "/api/motos/**").authenticated()
                .requestMatchers(HttpMethod.DELETE, "/api/rutas/**").authenticated()
                .requestMatchers(HttpMethod.DELETE, "/api/encuentros/**").authenticated()

                // El resto de la API de usuarios y demás requiere estar autenticado
                .requestMatchers("/api/usuarios/**").authenticated()
                .requestMatchers("/api/motos/**").authenticated()
                .requestMatchers("/api/rutas/**").authenticated()
                .requestMatchers("/api/encuentros/**").authenticated()
                .anyRequest().authenticated()
            );
        
        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        // Cubrimos todos los puertos comunes de Live Server
        config.setAllowedOrigins(Arrays.asList(
            "http://127.0.0.1:5500", "http://localhost:5500", 
            "http://127.0.0.1:5501", "http://localhost:5501"
        ));
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        // MODIFICADO: Añadidos encabezados adicionales para evitar bloqueos del navegador
        config.setAllowedHeaders(Arrays.asList("Content-Type", "Authorization", "Accept", "X-Requested-With"));
        config.setAllowCredentials(true);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
