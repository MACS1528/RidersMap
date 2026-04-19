package com.ridersmap.mapa_riders_backend.repository;

import com.ridersmap.mapa_riders_backend.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional; // Importación necesaria

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // Permite buscar un usuario por su email.
    // Lo usaremos en el login para encontrar al usuario.
    Optional<Usuario> findByEmail(String email); 
}
