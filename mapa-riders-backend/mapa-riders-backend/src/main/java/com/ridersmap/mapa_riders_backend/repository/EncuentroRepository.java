package com.ridersmap.mapa_riders_backend.repository;

import com.ridersmap.mapa_riders_backend.model.Encuentro;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EncuentroRepository extends JpaRepository<Encuentro, Long> {
}