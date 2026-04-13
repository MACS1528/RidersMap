package com.ridersmap.mapa_riders_backend.service;

import com.ridersmap.mapa_riders_backend.model.Encuentro; // Importación necesaria
import com.ridersmap.mapa_riders_backend.model.Ruta;
import com.ridersmap.mapa_riders_backend.model.Usuario;
import com.ridersmap.mapa_riders_backend.repository.RutaRepository;
import com.ridersmap.mapa_riders_backend.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RutaRepository rutaRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public Usuario registrarUsuario(Usuario usuario) {
        // 1. Encriptamos la contraseña
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        
        // 2. ASIGNAMOS ROL POR DEFECTO (Esto quita el Error 403)
        // MODIFICADO: Usamos "USER" directamente para que coincida con .hasAuthority("USER")
        // Mantenemos tus comentarios originales por integridad del archivo
        // Spring Security necesita que los roles empiecen por "ROLE_" -> (Ahora usamos Authority sin prefijo)
        
        if (usuario.getRol() == null || usuario.getRol().isEmpty()) {
            usuario.setRol("USER"); 
        }
        
        // 3. Guardamos en la base de datos
        return usuarioRepository.save(usuario);
    }

    @Transactional
    public Usuario agregarRutaAFavoritos(Long usuarioId, Long rutaId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        Ruta ruta = rutaRepository.findById(rutaId)
                .orElseThrow(() -> new RuntimeException("Ruta no encontrada"));

        // Añadimos la ruta a la lista de favoritas del usuario
        usuario.getRutasFavoritas().add(ruta);
        
        return usuarioRepository.save(usuario);
    }

    // --- NUEVO MÉTODO: ELIMINAR USUARIO CON LIMPIEZA DE RELACIONES ---
    @Transactional
    public void eliminarUsuario(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));
        
        // 1. Limpiamos la lista de favoritos manualmente para romper el vínculo en la tabla intermedia
        // Esto evita errores de Foreign Key en la tabla 'usuarios_rutas_favoritas'
        usuario.getRutasFavoritas().clear();

        // --- CORRECCIÓN BASADA EN TU USUARIO.JAVA ---
        // 2. Limpiamos los encuentros creados por el usuario
        // Al tener orphanRemoval=true en Usuario.java, esto ayudará a limpiar las referencias
        if (usuario.getEncuentros() != null) {
            usuario.getEncuentros().clear();
        }

        // --- AÑADIDO: LIMPIEZA DE ASISTENCIAS A OTROS ENCUENTROS ---
        // Esto soluciona el error de Foreign Key en la tabla 'asistencias_encuentros'
        if (usuario.getEncuentrosAsistidos() != null) {
            for (Encuentro encuentro : usuario.getEncuentrosAsistidos()) {
                encuentro.getAsistentes().remove(usuario);
            }
            usuario.getEncuentrosAsistidos().clear();
        }
        
        usuarioRepository.save(usuario);
        
        // 3. Borramos el usuario definitivamente
        // Gracias a orphanRemoval=true y CascadeType.ALL en la Entidad, esto borrará sus motos, rutas y encuentros
        usuarioRepository.delete(usuario);
    }
}