package com.ridersmap.mapa_riders_backend.service;

import com.ridersmap.mapa_riders_backend.model.Usuario;
import com.ridersmap.mapa_riders_backend.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        
        // 1. Buscar el usuario por email (que es el "username" para Spring Security)
        Usuario usuario = usuarioRepository.findByEmail(email) 
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con email: " + email));

        // 2. Definir los roles (autoridades)
        // MODIFICADO: Ahora cargamos el rol real de la base de datos para habilitar permisos de ADMIN
        List<GrantedAuthority> authorities = new ArrayList<>();
        
        // Mantenemos la estructura original pero usamos usuario.getRol()
        // Esto permite que Spring vea "ADMIN" o "USER" según lo que haya en MySQL
        if (usuario.getRol() != null) {
            authorities.add(new SimpleGrantedAuthority(usuario.getRol()));
        } else {
            // Fallback de seguridad en caso de rol nulo
            authorities.add(new SimpleGrantedAuthority("USER")); 
        }
        
        // 3. Devolver un objeto UserDetails (de Spring Security)
        return new org.springframework.security.core.userdetails.User(
            usuario.getEmail(),
            usuario.getPassword(), // Contraseña cifrada de la DB
            authorities // ¡Roles asignados dinámicamente!
        );
    }
}