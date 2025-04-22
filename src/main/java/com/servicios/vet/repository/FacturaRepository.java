package com.servicios.vet.repository;

import com.servicios.vet.model.Factura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FacturaRepository extends JpaRepository<Factura, String> {
    // Métodos personalizados pueden ser agregados aquí si es necesario
} 