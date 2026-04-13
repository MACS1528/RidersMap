package com.ridersmap.mapa_riders_backend.controller;

import com.ridersmap.mapa_riders_backend.model.Encuentro;
import com.ridersmap.mapa_riders_backend.model.Usuario;
import com.ridersmap.mapa_riders_backend.repository.EncuentroRepository;
import com.ridersmap.mapa_riders_backend.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/encuentros")
@CrossOrigin(origins = {"http://127.0.0.1:5500", "http://localhost:5500"})
public class EncuentroController {

    @Autowired private EncuentroRepository encuentroRepository;
    @Autowired private UsuarioRepository usuarioRepository;

    /**
     * GET /api/encuentros
     * Devuelve todos los encuentros.
     */
    @GetMapping
    public List<Encuentro> getAll() {
        return encuentroRepository.findAll();
    }

    /**
     * POST /api/encuentros
     * Crea un encuentro vinculándolo automáticamente al usuario autenticado.
     */
    @PostMapping
    public Encuentro create(@RequestBody Encuentro encuentro, Authentication authentication) {
        Usuario creador = usuarioRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        
        encuentro.setUsuario(creador);
        return encuentroRepository.save(encuentro);
    }

    /**
     * POST /api/encuentros/{id}/asistencia
     * Permite a un usuario unirse a un encuentro.
     */
    @PostMapping("/{id}/asistencia")
    public ResponseEntity<String> apuntarse(@PathVariable Long id, Authentication authentication) {
        Encuentro encuentro = encuentroRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Encuentro no encontrado"));
        Usuario usuario = usuarioRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!encuentro.getAsistentes().contains(usuario)) {
            encuentro.getAsistentes().add(usuario);
            encuentroRepository.save(encuentro);
            return ResponseEntity.ok("Unido con éxito");
        }
        return ResponseEntity.badRequest().body("Ya estás en la lista");
    }

    /**
     * DELETE /api/encuentros/{id}/asistencia
     * Permite a un usuario cancelar su asistencia (salir de la lista).
     */
    @DeleteMapping("/{id}/asistencia")
    public ResponseEntity<String> cancelarAsistencia(@PathVariable Long id, Authentication authentication) {
        Encuentro encuentro = encuentroRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Encuentro no encontrado"));
        Usuario usuario = usuarioRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (encuentro.getAsistentes().contains(usuario)) {
            encuentro.getAsistentes().remove(usuario);
            encuentroRepository.save(encuentro);
            return ResponseEntity.ok("Asistencia cancelada");
        }
        return ResponseEntity.badRequest().body("No estabas apuntado");
    }

    /**
     * DELETE /api/encuentros/{id}
     * El Administrador puede borrar cualquiera. 
     * El Usuario solo puede borrar el suyo propio.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id, Authentication authentication) {
        Encuentro encuentro = encuentroRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Encuentro no encontrado"));
        
        Usuario usuarioActual = usuarioRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (isAdmin || encuentro.getUsuario().getId().equals(usuarioActual.getId())) {
            // IMPORTANTE: Limpiar asistentes antes de borrar para evitar errores de FK en DB
            encuentro.getAsistentes().clear();
            encuentroRepository.save(encuentro); // Sincronizamos la limpieza
            
            encuentroRepository.delete(encuentro);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("No tienes permiso para borrar este encuentro.");
        }
    }
}