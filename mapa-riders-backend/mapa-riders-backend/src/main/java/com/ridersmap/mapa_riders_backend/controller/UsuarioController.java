package com.ridersmap.mapa_riders_backend.controller;

import com.ridersmap.mapa_riders_backend.model.Usuario;
import com.ridersmap.mapa_riders_backend.repository.UsuarioRepository;
import com.ridersmap.mapa_riders_backend.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
// Permite que el frontend (VS Code) acceda a estos endpoints
@CrossOrigin(origins = {"http://localhost:5500", "http://127.0.0.1:5500", "http://localhost:5501", "http://127.0.0.1:5501"})
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private UsuarioService usuarioService;

    // 1. Obtener todos los usuarios
    @GetMapping 
    public List<Usuario> getAllUsuarios() {
        return usuarioRepository.findAll();
    }

    // 2. Registrar un nuevo usuario
    @PostMapping("/register") 
    public ResponseEntity<Usuario> registerUser(@RequestBody Usuario nuevoUsuario) {
        Usuario usuarioGuardado = usuarioService.registrarUsuario(nuevoUsuario);
        return new ResponseEntity<>(usuarioGuardado, HttpStatus.CREATED);
    }

    // 3. Añadir ruta a favoritos
    @PostMapping("/{usuarioId}/favoritas/{rutaId}")
    public ResponseEntity<Usuario> agregarRutaAFavoritos(@PathVariable Long usuarioId, @PathVariable Long rutaId) {
        Usuario usuarioActualizado = usuarioService.agregarRutaAFavoritos(usuarioId, rutaId);
        return new ResponseEntity<>(usuarioActualizado, HttpStatus.OK);
    }
    
    // 4. Obtener un solo usuario por su email (Útil para el Login del frontend)
    @GetMapping("/perfil/{email}")
    public ResponseEntity<Usuario> getUsuarioByEmail(@PathVariable String email) {
        return usuarioRepository.findByEmail(email)
                .map(usuario -> new ResponseEntity<>(usuario, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // 5. Eliminar un usuario (Para el panel de Administrador)
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarUsuario(@PathVariable Long id) {
        try {
            // Llamamos al servicio para que realice la limpieza y el borrado
            usuarioService.eliminarUsuario(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 204 No Content: éxito total
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Error al eliminar el usuario", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
