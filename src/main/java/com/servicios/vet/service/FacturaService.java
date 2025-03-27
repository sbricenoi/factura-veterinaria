package com.servicios.vet.service;

import com.servicios.vet.model.Factura;
import com.servicios.vet.model.Servicio;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    // Almacén en memoria para las facturas (como un archivador digital)
    // Usa un mapa donde la "llave" es el ID de la factura y el "valor" es la factura misma
    private Map<String, Factura> facturas = new HashMap<>();
    
    // Almacén en memoria para los servicios
    // Funciona igual que el mapa de facturas
    private Map<String, Servicio> servicios = new HashMap<>();

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
        servicios.put(servicio.getId(), servicio);
        return servicio;
    }

    /**
     * Busca y retorna un servicio específico por su ID.
     * 
     * @param id El identificador único del servicio
     * @return El servicio encontrado
     * @throws IllegalArgumentException Si no existe un servicio con ese ID
     */
    public Servicio obtenerServicio(String id) {
        Servicio servicio = servicios.get(id);
        if (servicio == null) {
            throw new IllegalArgumentException("No existe servicio con ID: " + id);
        }
        return servicio;
    }

    /**
     * Obtiene la lista de todos los servicios registrados en el sistema.
     * 
     * @return Lista de todos los servicios
     */
    public List<Servicio> listarServicios() {
        return new ArrayList<>(servicios.values());
    }

    // ===== MÉTODOS PARA GESTIONAR FACTURAS =====
    
    /**
     * Crea una nueva factura a partir de una lista de IDs de servicios.
     * 
     * Para cada ID en la lista, busca el servicio correspondiente y lo incluye
     * en la factura. Genera un ID único para la factura y la guarda en el sistema.
     * 
     * @param serviciosIds Lista de IDs de los servicios a incluir
     * @return La factura creada
     * @throws IllegalArgumentException Si la lista está vacía o algún ID no existe
     */
    public Factura crearFactura(List<String> serviciosIds) {
        // Verificamos que haya al menos un servicio
        if (serviciosIds == null || serviciosIds.isEmpty()) {
            throw new IllegalArgumentException("Debe incluir al menos un servicio en la factura");
        }

        // Convertimos los IDs en objetos Servicio
        List<Servicio> serviciosFactura = new ArrayList<>();
        for (String id : serviciosIds) {
            Servicio servicio = obtenerServicio(id);
            serviciosFactura.add(servicio);
        }

        // Generamos un ID único para la factura
        String facturaId = UUID.randomUUID().toString();
        
        // Creamos la factura con los servicios
        Factura nuevaFactura = new Factura(facturaId, serviciosFactura);
        
        // Guardamos la factura en el mapa
        facturas.put(facturaId, nuevaFactura);
        
        return nuevaFactura;
    }

    /**
     * Busca y retorna una factura específica por su ID.
     * 
     * @param id El identificador único de la factura
     * @return La factura encontrada
     * @throws IllegalArgumentException Si no existe una factura con ese ID
     */
    public Factura obtenerFactura(String id) {
        Factura factura = facturas.get(id);
        if (factura == null) {
            throw new IllegalArgumentException("No existe factura con ID: " + id);
        }
        return factura;
    }

    /**
     * Marca una factura como pagada.
     * 
     * Busca la factura por su ID y cambia su estado a "pagada".
     * 
     * @param id El identificador único de la factura
     * @return La factura actualizada
     * @throws IllegalArgumentException Si no existe una factura con ese ID
     * @throws IllegalStateException Si la factura ya estaba pagada
     */
    public Factura pagarFactura(String id) {
        Factura factura = obtenerFactura(id);
        
        // Verificamos que la factura no esté ya pagada
        if (factura.isPagada()) {
            throw new IllegalStateException("La factura ya ha sido pagada");
        }
        
        // Marcamos la factura como pagada
        factura.setPagada(true);
        return factura;
    }

    /**
     * Obtiene la lista de todas las facturas registradas en el sistema.
     * 
     * @return Lista de todas las facturas
     */
    public List<Factura> listarFacturas() {
        return new ArrayList<>(facturas.values());
    }
} 