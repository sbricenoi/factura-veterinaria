package com.servicios.vet.dto;

/**
 * Esta clase es un "Data Transfer Object" (DTO) para transportar datos de servicios.
 * Funciona como un mensajero entre el frontend y el backend, llevando la información
 * necesaria para crear o modificar un servicio.
 * 
 * Es muy similar a la clase Servicio del modelo, pero está específicamente diseñada
 * para la comunicación con el cliente (navegador).
 */
public class ServicioDTO {
    // El identificador único del servicio (puede ser null cuando se crea uno nuevo)
    private String id;
    
    // El nombre descriptivo del servicio
    private String nombre;
    
    // El costo o precio del servicio
    private double costo;

    /**
     * Constructor vacío necesario para que Spring pueda convertir 
     * automáticamente el JSON recibido
     */
    public ServicioDTO() {
    }

    /**
     * Constructor para crear un DTO con todos los datos del servicio
     */
    public ServicioDTO(String id, String nombre, double costo) {
        this.id = id;
        this.nombre = nombre;
        this.costo = costo;
    }

    /**
     * Obtiene el identificador del servicio
     */
    public String getId() {
        return id;
    }

    /**
     * Establece el identificador del servicio
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Obtiene el nombre del servicio
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre del servicio
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Obtiene el costo del servicio
     */
    public double getCosto() {
        return costo;
    }

    /**
     * Establece el costo del servicio
     */
    public void setCosto(double costo) {
        this.costo = costo;
    }
} 