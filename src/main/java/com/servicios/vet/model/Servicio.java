package com.servicios.vet.model;

/**
 * Esta clase representa un servicio veterinario que puede ofrecerse a los clientes.
 * Es como una ficha que guarda la información de cada tipo de servicio disponible.
 */
public class Servicio {
    // Un código único para identificar cada servicio (como una cédula de identidad)
    private String id;
    
    // El nombre que describe el servicio (por ejemplo: "Vacunación", "Consulta", etc.)
    private String nombre;
    
    // Cuánto cuesta este servicio (en la moneda local)
    private double costo;

    /**
     * Constructor vacío necesario para que Spring pueda crear objetos
     * cuando recibe datos del frontend
     */
    public Servicio() {
    }

    /**
     * Constructor que permite crear un servicio con todos sus datos de una vez
     */
    public Servicio(String id, String nombre, double costo) {
        this.id = id;
        this.nombre = nombre;
        this.costo = costo;
    }

    // Los siguientes métodos son "getters" y "setters" que permiten
    // leer o modificar cada propiedad del servicio
    
    /**
     * Obtiene el identificador único del servicio
     */
    public String getId() {
        return id;
    }

    /**
     * Establece o cambia el identificador del servicio
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Obtiene el nombre descriptivo del servicio
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece o cambia el nombre del servicio
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Obtiene el costo/precio del servicio
     */
    public double getCosto() {
        return costo;
    }

    /**
     * Establece o cambia el costo del servicio
     */
    public void setCosto(double costo) {
        this.costo = costo;
    }
} 