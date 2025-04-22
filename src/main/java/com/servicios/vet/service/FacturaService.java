package com.servicios.vet.service;

import com.servicios.vet.model.Factura;
import com.servicios.vet.model.Servicio;
import com.servicios.vet.repository.FacturaRepository;
import com.servicios.vet.repository.ServicioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Esta clase contiene toda la lógica de negocio para gestionar servicios y facturas.
 * 
 * Funciona como el "cerebro" de la aplicación, procesando todas las operaciones
 * relacionadas con la creación, consulta y modificación de servicios y facturas.
 * 
 * Al estar marcada con @Service, Spring la reconoce como un componente que puede
 * ser inyectado en otras clases (como el controlador).
 */
@Service
public class FacturaService {
    private final FacturaRepository facturaRepository;
    private final ServicioRepository servicioRepository;

    @Autowired
    public FacturaService(FacturaRepository facturaRepository, ServicioRepository servicioRepository) {
        this.facturaRepository = facturaRepository;
        this.servicioRepository = servicioRepository;
    }

    // ===== MÉTODOS PARA GESTIONAR SERVICIOS =====
    
    /**
     * Registra un nuevo servicio en el sistema.
     * 
     * Si el servicio no tiene ID, le genera uno automáticamente.
     * Valida que el servicio tenga datos correctos antes de guardarlo.
     * 
     * @param servicio El servicio a registrar
     * @return El servicio guardado (con ID generado si era nuevo)
     * @throws IllegalArgumentException Si el servicio tiene datos inválidos
     */
    public Servicio agregarServicio(Servicio servicio) {
        // Si no tiene ID o está vacío, generamos uno aleatorio
        if (servicio.getId() == null || servicio.getId().isEmpty()) {
            servicio.setId(UUID.randomUUID().toString());
        }
        
        // Verificamos que el costo sea positivo
        if (servicio.getCosto() <= 0) {
            throw new IllegalArgumentException("El costo del servicio debe ser mayor que cero");
        }
        
        // Verificamos que tenga un nombre válido
        if (servicio.getNombre() == null || servicio.getNombre().isEmpty()) {
            throw new IllegalArgumentException("El nombre del servicio no puede estar vacío");
        }
        
        // Guardamos el servicio en el mapa usando su ID como llave
        return servicioRepository.save(servicio);
    }

    /**
     * Busca y retorna un servicio específico por su ID.
     * 
     * @param id El identificador único del servicio
     * @return El servicio encontrado
     * @throws IllegalArgumentException Si no existe un servicio con ese ID
     */
    public Servicio obtenerServicio(String id) {
        return servicioRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("No existe servicio con ID: " + id));
    }

    /**
     * Obtiene la lista de todos los servicios registrados en el sistema.
     * 
     * @return Lista de todos los servicios
     */
    public List<Servicio> listarServicios() {
        return servicioRepository.findAll();
    }

    // ===== MÉTODOS PARA GESTIONAR FACTURAS =====
    
    @Transactional
    public Factura crearFactura(List<String> serviciosIds) {
        // Verificamos que haya al menos un servicio
        if (serviciosIds == null || serviciosIds.isEmpty()) {
            throw new IllegalArgumentException("Debe incluir al menos un servicio en la factura");
        }

        // Convertimos los IDs en objetos Servicio
        List<Servicio> serviciosFactura = new ArrayList<>();
        for (String id : serviciosIds) {
            Servicio servicio = servicioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No existe servicio con ID: " + id));
            serviciosFactura.add(servicio);
        }

        // Generamos un ID único para la factura
        String facturaId = UUID.randomUUID().toString();
        
        // Creamos la factura con los servicios
        Factura nuevaFactura = new Factura(facturaId, serviciosFactura);
        
        // Guardamos la factura en la base de datos
        return facturaRepository.save(nuevaFactura);
    }

    @Transactional(readOnly = true)
    public Factura obtenerFactura(String id) {
        return facturaRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("No existe factura con ID: " + id));
    }

    @Transactional
    public Factura pagarFactura(String id) {
        Factura factura = obtenerFactura(id);
        
        // Verificamos que la factura no esté ya pagada
        if (factura.isPagada()) {
            throw new IllegalStateException("La factura ya ha sido pagada");
        }
        
        // Marcamos la factura como pagada
        factura.setPagada(true);
        return facturaRepository.save(factura);
    }

    @Transactional(readOnly = true)
    public List<Factura> listarFacturas() {
        return facturaRepository.findAll();
    }
} 