package com.servicios.vet.service;

import com.servicios.vet.model.Servicio;
import com.servicios.vet.repository.ServicioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ServicioService {

    private final ServicioRepository servicioRepository;

    @Autowired
    public ServicioService(ServicioRepository servicioRepository) {
        this.servicioRepository = servicioRepository;
    }

    @Transactional
    public Servicio agregarServicio(Servicio servicio) {
        return servicioRepository.save(servicio);
    }

    @Transactional(readOnly = true)
    public List<Servicio> listarServicios() {
        return servicioRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Servicio obtenerServicio(String id) {
        return servicioRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Servicio no encontrado con ID: " + id));
    }
} 