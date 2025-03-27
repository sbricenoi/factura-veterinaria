package com.servicios.vet.dto;

import java.util.List;

/**
 * Esta clase es un "Data Transfer Object" (DTO) específico para solicitudes de creación de facturas.
 * 
 * Su función es transportar desde el frontend (navegador) al backend (servidor) 
 * la lista de IDs de los servicios que el usuario seleccionó para incluir en una nueva factura.
 * 
 * Es una clase simple que actúa como un "sobre" para enviar exactamente lo que el 
 * backend necesita para crear una factura.
 */
public class FacturaRequestDTO {
    // Lista de identificadores de los servicios que se incluirán en la factura
    private List<String> serviciosIds;

    /**
     * Constructor vacío necesario para que Spring pueda convertir
     * automáticamente el JSON recibido
     */
    public FacturaRequestDTO() {
    }

    /**
     * Constructor que permite crear la solicitud con la lista de IDs
     */
    public FacturaRequestDTO(List<String> serviciosIds) {
        this.serviciosIds = serviciosIds;
    }

    /**
     * Obtiene la lista de IDs de servicios a incluir en la factura
     */
    public List<String> getServiciosIds() {
        return serviciosIds;
    }

    /**
     * Establece la lista de IDs de servicios
     */
    public void setServiciosIds(List<String> serviciosIds) {
        this.serviciosIds = serviciosIds;
    }
} 