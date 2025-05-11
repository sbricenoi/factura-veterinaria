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
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.hateoas.Link;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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
     * Endpoint para registrar un nuevo servicio con soporte HATEOAS.
     */
    @PostMapping("/servicio")
    public ResponseEntity<EntityModel<Servicio>> agregarServicio(@RequestBody ServicioDTO servicioDTO) {
        try {
            Servicio servicio = new Servicio(
                    servicioDTO.getId(),
                    servicioDTO.getNombre(),
                    servicioDTO.getCosto()
            );
            
            Servicio resultado = facturaService.agregarServicio(servicio);
            
            // Crear un EntityModel con links HATEOAS
            EntityModel<Servicio> servicioModel = EntityModel.of(resultado,
                WebMvcLinkBuilder.linkTo(methodOn(FacturaController.class).obtenerServicio(resultado.getId())).withSelfRel(),
                WebMvcLinkBuilder.linkTo(methodOn(FacturaController.class).listarServicios()).withRel("servicios")
            );
            
            return new ResponseEntity<>(servicioModel, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity("Error al procesar la solicitud: " + e.getMessage(), 
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Endpoint para obtener la lista de todos los servicios con soporte HATEOAS.
     */
    @GetMapping("/servicio")
    public ResponseEntity<List<EntityModel<Servicio>>> listarServicios() {
        List<Servicio> servicios = facturaService.listarServicios();
        
        List<EntityModel<Servicio>> serviciosModel = servicios.stream()
            .map(servicio -> EntityModel.of(servicio, 
                WebMvcLinkBuilder.linkTo(methodOn(FacturaController.class).obtenerServicio(servicio.getId())).withSelfRel(),
                WebMvcLinkBuilder.linkTo(methodOn(FacturaController.class).listarServicios()).withRel("servicios")
            ))
            .collect(Collectors.toList());
        
        return new ResponseEntity<>(serviciosModel, HttpStatus.OK);
    }

    /**
     * Endpoint para obtener un servicio específico por su ID con soporte HATEOAS.
     */
    @GetMapping("/servicio/{id}")
    public ResponseEntity<EntityModel<Servicio>> obtenerServicio(@PathVariable String id) {
        try {
            Servicio servicio = facturaService.obtenerServicio(id);
            
            // Crear un EntityModel con links HATEOAS
            EntityModel<Servicio> servicioModel = EntityModel.of(servicio,
                WebMvcLinkBuilder.linkTo(methodOn(FacturaController.class).obtenerServicio(id)).withSelfRel(),
                WebMvcLinkBuilder.linkTo(methodOn(FacturaController.class).listarServicios()).withRel("servicios")
            );
            
            return new ResponseEntity<>(servicioModel, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity("Error al procesar la solicitud: " + e.getMessage(), 
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // ===== ENDPOINTS PARA FACTURAS =====

    /**
     * Endpoint para crear una nueva factura con soporte HATEOAS.
     */
    @PostMapping("/factura")
    public ResponseEntity<EntityModel<Factura>> crearFactura(@RequestBody FacturaRequestDTO facturaRequestDTO) {
        try {
            Factura factura = facturaService.crearFactura(facturaRequestDTO.getServiciosIds());
            
            // Crear un EntityModel con links HATEOAS
            EntityModel<Factura> facturaModel = EntityModel.of(factura,
                WebMvcLinkBuilder.linkTo(methodOn(FacturaController.class).obtenerFactura(factura.getId())).withSelfRel(),
                WebMvcLinkBuilder.linkTo(methodOn(FacturaController.class).pagarFactura(factura.getId())).withRel("pagar"),
                WebMvcLinkBuilder.linkTo(methodOn(FacturaController.class).listarFacturas()).withRel("facturas")
            );
            
            return new ResponseEntity<>(facturaModel, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity("Error al procesar la solicitud: " + e.getMessage(), 
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Endpoint para obtener una factura específica por su ID con soporte HATEOAS.
     */
    @GetMapping("/factura/{id}")
    public ResponseEntity<EntityModel<Factura>> obtenerFactura(@PathVariable String id) {
        try {
            Factura factura = facturaService.obtenerFactura(id);
            
            // Crear un EntityModel con links HATEOAS
            EntityModel<Factura> facturaModel = EntityModel.of(factura,
                WebMvcLinkBuilder.linkTo(methodOn(FacturaController.class).obtenerFactura(id)).withSelfRel(),
                WebMvcLinkBuilder.linkTo(methodOn(FacturaController.class).pagarFactura(id)).withRel("pagar"),
                WebMvcLinkBuilder.linkTo(methodOn(FacturaController.class).listarFacturas()).withRel("facturas")
            );
            
            return new ResponseEntity<>(facturaModel, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity("Error al procesar la solicitud: " + e.getMessage(), 
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Endpoint para marcar una factura como pagada con soporte HATEOAS.
     */
    @PutMapping("/factura/{id}/pagar")
    public ResponseEntity<EntityModel<Factura>> pagarFactura(@PathVariable String id) {
        try {
            Factura factura = facturaService.pagarFactura(id);
            
            // Crear un EntityModel con links HATEOAS
            EntityModel<Factura> facturaModel = EntityModel.of(factura,
                WebMvcLinkBuilder.linkTo(methodOn(FacturaController.class).obtenerFactura(id)).withSelfRel(),
                WebMvcLinkBuilder.linkTo(methodOn(FacturaController.class).listarFacturas()).withRel("facturas")
            );
            
            return new ResponseEntity<>(facturaModel, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (IllegalStateException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity("Error al procesar la solicitud: " + e.getMessage(), 
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Endpoint para obtener la lista de todas las facturas con soporte HATEOAS.
     */
    @GetMapping("/factura")
    public ResponseEntity<List<EntityModel<Factura>>> listarFacturas() {
        List<Factura> facturas = facturaService.listarFacturas();
        
        List<EntityModel<Factura>> facturasModel = facturas.stream()
            .map(factura -> EntityModel.of(factura, 
                WebMvcLinkBuilder.linkTo(methodOn(FacturaController.class).obtenerFactura(factura.getId())).withSelfRel(),
                WebMvcLinkBuilder.linkTo(methodOn(FacturaController.class).pagarFactura(factura.getId())).withRel("pagar"),
                WebMvcLinkBuilder.linkTo(methodOn(FacturaController.class).listarFacturas()).withRel("facturas")
            ))
            .collect(Collectors.toList());
        
        return new ResponseEntity<>(facturasModel, HttpStatus.OK);
    }

    /**
     * Endpoint para eliminar una factura con soporte HATEOAS.
     */
    @DeleteMapping("/factura/{id}")
    public ResponseEntity<?> eliminarFactura(@PathVariable String id) {
        try {
            facturaService.eliminarFactura(id);
            
            // Crear un link HATEOAS para listar facturas
            //Link listarFacturasLink = linkTo(methodOn(FacturaController.class).listarFacturas()).withRel("facturas");
            
            // Devolver una respuesta sin contenido pero con un mensaje personalizado
            return ResponseEntity.ok()
                .header("X-Mensaje", "Factura eliminada exitosamente")
                .body(Collections.singletonMap("mensaje", "Factura eliminada exitosamente"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Collections.singletonMap("error", e.getMessage()));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Collections.singletonMap("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Collections.singletonMap("error", "Error al procesar la solicitud: " + e.getMessage()));
        }
    }
} 