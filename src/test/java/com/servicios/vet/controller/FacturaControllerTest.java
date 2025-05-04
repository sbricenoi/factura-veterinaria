package com.servicios.vet.controller;

import com.servicios.vet.dto.FacturaRequestDTO;
import com.servicios.vet.dto.ServicioDTO;
import com.servicios.vet.model.Factura;
import com.servicios.vet.model.Servicio;
import com.servicios.vet.service.FacturaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.hateoas.EntityModel;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FacturaControllerTest {

    @Mock
    private FacturaService facturaService;

    @InjectMocks
    private FacturaController facturaController;

    private Servicio servicioTest;
    private Factura facturaTest;

    @BeforeEach
    public void setUp() {
        // Configurar datos de prueba
        servicioTest = new Servicio(
            UUID.randomUUID().toString(), 
            "Consulta Veterinaria", 
            50000.0
        );

        facturaTest = new Factura(
            UUID.randomUUID().toString(), 
            Arrays.asList(servicioTest)
        );
    }

    @Test
    public void testAgregarServicio_Exitoso() {
        // Preparar
        ServicioDTO servicioDTO = new ServicioDTO(
            servicioTest.getId(), 
            servicioTest.getNombre(), 
            servicioTest.getCosto()
        );
        
        when(facturaService.agregarServicio(any(Servicio.class))).thenReturn(servicioTest);

        // Ejecutar
        ResponseEntity<EntityModel<Servicio>> respuesta = facturaController.agregarServicio(servicioDTO);

        // Verificar
        assertEquals(HttpStatus.CREATED, respuesta.getStatusCode());
        assertNotNull(respuesta.getBody());
        assertEquals(servicioTest.getId(), respuesta.getBody().getContent().getId());
        
        verify(facturaService).agregarServicio(any(Servicio.class));
    }

    @Test
    public void testAgregarServicio_DatosInvalidos() {
        // Preparar
        ServicioDTO servicioDTO = new ServicioDTO(null, "", -1000.0);
        
        when(facturaService.agregarServicio(any(Servicio.class)))
            .thenThrow(new IllegalArgumentException("Datos inválidos"));

        // Ejecutar y Verificar
        ResponseEntity<?> respuesta = facturaController.agregarServicio(servicioDTO);
        
        assertEquals(HttpStatus.BAD_REQUEST, respuesta.getStatusCode());
    }

    @Test
    public void testListarServicios_Exitoso() {
        // Preparar
        List<Servicio> servicios = Arrays.asList(servicioTest);
        
        when(facturaService.listarServicios()).thenReturn(servicios);

        // Ejecutar
        ResponseEntity<List<EntityModel<Servicio>>> respuesta = facturaController.listarServicios();

        // Verificar
        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertNotNull(respuesta.getBody());
        assertFalse(respuesta.getBody().isEmpty());
        
        verify(facturaService).listarServicios();
    }

    @Test
    public void testObtenerServicio_Exitoso() {
        // Preparar
        when(facturaService.obtenerServicio(servicioTest.getId())).thenReturn(servicioTest);

        // Ejecutar
        ResponseEntity<EntityModel<Servicio>> respuesta = facturaController.obtenerServicio(servicioTest.getId());

        // Verificar
        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertNotNull(respuesta.getBody());
        assertEquals(servicioTest.getId(), respuesta.getBody().getContent().getId());
        
        verify(facturaService).obtenerServicio(servicioTest.getId());
    }

    @Test
    public void testObtenerServicio_NoEncontrado() {
        // Preparar
        String idNoExistente = UUID.randomUUID().toString();
        
        when(facturaService.obtenerServicio(idNoExistente))
            .thenThrow(new IllegalArgumentException("No existe servicio con ID: " + idNoExistente));

        // Ejecutar
        ResponseEntity<EntityModel<Servicio>> respuesta = facturaController.obtenerServicio(idNoExistente);

        // Verificar
        assertEquals(HttpStatus.NOT_FOUND, respuesta.getStatusCode());
    }

    @Test
    public void testCrearFactura_Exitoso() {
        // Preparar
        FacturaRequestDTO facturaRequestDTO = new FacturaRequestDTO();
        facturaRequestDTO.setServiciosIds(Arrays.asList(servicioTest.getId()));
        
        when(facturaService.crearFactura(any())).thenReturn(facturaTest);

        // Ejecutar
        ResponseEntity<EntityModel<Factura>> respuesta = facturaController.crearFactura(facturaRequestDTO);

        // Verificar
        assertEquals(HttpStatus.CREATED, respuesta.getStatusCode());
        assertNotNull(respuesta.getBody());
        assertEquals(facturaTest.getId(), respuesta.getBody().getContent().getId());
        
        verify(facturaService).crearFactura(any());
    }

    @Test
    public void testObtenerFactura_Exitoso() {
        // Preparar
        when(facturaService.obtenerFactura(facturaTest.getId())).thenReturn(facturaTest);

        // Ejecutar
        ResponseEntity<EntityModel<Factura>> respuesta = facturaController.obtenerFactura(facturaTest.getId());

        // Verificar
        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertNotNull(respuesta.getBody());
        assertEquals(facturaTest.getId(), respuesta.getBody().getContent().getId());
        
        verify(facturaService).obtenerFactura(facturaTest.getId());
    }

    @Test
    public void testPagarFactura_Exitoso() {
        // Preparar
        facturaTest.setPagada(false);
        
        when(facturaService.pagarFactura(facturaTest.getId())).thenReturn(facturaTest);

        // Ejecutar
        ResponseEntity<EntityModel<Factura>> respuesta = facturaController.pagarFactura(facturaTest.getId());

        // Verificar
        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertNotNull(respuesta.getBody());
        
        verify(facturaService).pagarFactura(facturaTest.getId());
    }

    @Test
    public void testListarFacturas_Exitoso() {
        // Preparar
        List<Factura> facturas = Arrays.asList(facturaTest);
        
        when(facturaService.listarFacturas()).thenReturn(facturas);

        // Ejecutar
        ResponseEntity<List<EntityModel<Factura>>> respuesta = facturaController.listarFacturas();

        // Verificar
        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertNotNull(respuesta.getBody());
        assertFalse(respuesta.getBody().isEmpty());
        
        verify(facturaService).listarFacturas();
    }
} 