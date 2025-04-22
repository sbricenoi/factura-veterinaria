package com.servicios.vet.repository;

import com.servicios.vet.model.Servicio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServicioRepository extends JpaRepository<Servicio, String> {
    // Métodos personalizados pueden ser agregados aquí si es necesario
} 