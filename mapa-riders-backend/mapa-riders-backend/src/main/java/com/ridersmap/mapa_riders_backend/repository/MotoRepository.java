package com.ridersmap.mapa_riders_backend.repository;

import com.ridersmap.mapa_riders_backend.model.Moto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MotoRepository extends JpaRepository<Moto, Long> {
    // Listo
}