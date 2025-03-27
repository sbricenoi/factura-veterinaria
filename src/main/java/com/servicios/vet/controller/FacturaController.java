package com.servicios.vet.controller;

import com.servicios.vet.dto.FacturaRequestDTO;
import com.servicios.vet.dto.ServicioDTO;
import com.servicios.vet.model.Factura;
import com.servicios.vet.model.Servicio;
import com.servicios.vet.service.FacturaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST que expone los endpoints de la API para interactuar con servicios y facturas.
 * 
 * Este controlador funciona como el "recepcionista" de la aplicación:
 * - Recibe las peticiones HTTP desde el frontend
 * - Valida y procesa los datos recibidos
 * - Llama al servicio correspondiente para realizar la operación
 * - Devuelve una respuesta HTTP adecuada
 * 
 * La anotación @RestController indica que es un controlador REST que devuelve JSON.
 * La anotación @RequestMapping("/api") establece la ruta base para todos los endpoints.
 */
@RestController
@RequestMapping("/api")
public class FacturaController {

    // Servicio que contiene la lógica de negocio
    // Spring lo inyecta automáticamente gracias a @Autowired
    private final FacturaService facturaService;

    /**
     * Constructor que recibe el servicio a través de inyección de dependencias
     */
    @Autowired
    public FacturaController(FacturaService facturaService) {
        this.facturaService = facturaService;
    }

    // ===== ENDPOINTS PARA SERVICIOS =====

    /**
     * Endpoint para registrar un nuevo servicio.
     * 
     * URL: POST /api/servicio
     * 
     * Recibe los datos del servicio en formato JSON, los convierte en un objeto Servicio
     * y lo pasa al servicio para registrarlo en el sistema.
     * 
     * @param servicioDTO Datos del servicio recibidos del cliente
     * @return Respuesta HTTP con el servicio creado o un mensaje de error
     */
    @PostMapping("/servicio")
    public ResponseEntity<?> agregarServicio(@RequestBody ServicioDTO servicioDTO) {
        try {
            // Convertimos el DTO a un objeto Servicio
            Servicio servicio = new Servicio(
                    servicioDTO.getId(),
                    servicioDTO.getNombre(),
                    servicioDTO.getCosto()
            );
            
            // Llamamos al servicio para registrarlo
            Servicio resultado = facturaService.agregarServicio(servicio);
            
            // Devolvemos respuesta exitosa (201 Created) con el servicio creado
            return new ResponseEntity<>(resultado, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            // Error en los datos enviados (400 Bad Request)
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            // Error interno del servidor (500 Internal Server Error)
            return new ResponseEntity<>("Error al procesar la solicitud: " + e.getMessage(), 
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Endpoint para obtener la lista de todos los servicios.
     * 
     * URL: GET /api/servicio
     * 
     * @return Lista de todos los servicios registrados
     */
    @GetMapping("/servicio")
    public ResponseEntity<List<Servicio>> listarServicios() {
        List<Servicio> servicios = facturaService.listarServicios();
        return new ResponseEntity<>(servicios, HttpStatus.OK);
    }

    /**
     * Endpoint para obtener un servicio específico por su ID.
     * 
     * URL: GET /api/servicio/{id}
     * 
     * @param id Identificador único del servicio
     * @return Respuesta HTTP con el servicio encontrado o un mensaje de error
     */
    @GetMapping("/servicio/{id}")
    public ResponseEntity<?> obtenerServicio(@PathVariable String id) {
        try {
            Servicio servicio = facturaService.obtenerServicio(id);
            return new ResponseEntity<>(servicio, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            // No se encontró el servicio (404 Not Found)
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            // Error interno del servidor (500 Internal Server Error)
            return new ResponseEntity<>("Error al procesar la solicitud: " + e.getMessage(), 
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // ===== ENDPOINTS PARA FACTURAS =====

    /**
     * Endpoint para crear una nueva factura.
     * 
     * URL: POST /api/factura
     * 
     * Recibe una lista de IDs de servicios y crea una factura incluyendo esos servicios.
     * 
     * @param facturaRequestDTO Datos para crear la factura (lista de IDs de servicios)
     * @return Respuesta HTTP con la factura creada o un mensaje de error
     */
    @PostMapping("/factura")
    public ResponseEntity<?> crearFactura(@RequestBody FacturaRequestDTO facturaRequestDTO) {
        try {
            Factura factura = facturaService.crearFactura(facturaRequestDTO.getServiciosIds());
            return new ResponseEntity<>(factura, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            // Error en los datos enviados (400 Bad Request)
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            // Error interno del servidor (500 Internal Server Error)
            return new ResponseEntity<>("Error al procesar la solicitud: " + e.getMessage(), 
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Endpoint para obtener una factura específica por su ID.
     * 
     * URL: GET /api/factura/{id}
     * 
     * @param id Identificador único de la factura
     * @return Respuesta HTTP con la factura encontrada o un mensaje de error
     */
    @GetMapping("/factura/{id}")
    public ResponseEntity<?> obtenerFactura(@PathVariable String id) {
        try {
            Factura factura = facturaService.obtenerFactura(id);
            return new ResponseEntity<>(factura, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            // No se encontró la factura (404 Not Found)
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            // Error interno del servidor (500 Internal Server Error)
            return new ResponseEntity<>("Error al procesar la solicitud: " + e.getMessage(), 
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Endpoint para marcar una factura como pagada.
     * 
     * URL: PUT /api/factura/{id}/pagar
     * 
     * @param id Identificador único de la factura
     * @return Respuesta HTTP con la factura actualizada o un mensaje de error
     */
    @PutMapping("/factura/{id}/pagar")
    public ResponseEntity<?> pagarFactura(@PathVariable String id) {
        try {
            Factura factura = facturaService.pagarFactura(id);
            return new ResponseEntity<>(factura, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            // No se encontró la factura (404 Not Found)
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (IllegalStateException e) {
            // La factura ya estaba pagada (400 Bad Request)
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            // Error interno del servidor (500 Internal Server Error)
            return new ResponseEntity<>("Error al procesar la solicitud: " + e.getMessage(), 
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Endpoint para obtener la lista de todas las facturas.
     * 
     * URL: GET /api/factura
     * 
     * @return Lista de todas las facturas registradas
     */
    @GetMapping("/factura")
    public ResponseEntity<List<Factura>> listarFacturas() {
        List<Factura> facturas = facturaService.listarFacturas();
        return new ResponseEntity<>(facturas, HttpStatus.OK);
    }
} 