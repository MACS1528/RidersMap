package com.ridersmap.mapa_riders_backend.repository;

import com.ridersmap.mapa_riders_backend.model.Ruta;
import com.ridersmap.mapa_riders_backend.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RutaRepository extends JpaRepository<Ruta, Long> {
    
    /**
     * Busca todas las rutas creadas por un usuario específico.
     * Spring Data JPA genera la consulta automáticamente.
     */
    List<Ruta> findByCreador(Usuario creador);
}