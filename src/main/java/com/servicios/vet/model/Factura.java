package com.servicios.vet.model;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

/**
 * Esta clase representa una factura de servicios veterinarios.
 * Es como un documento que registra los servicios brindados a un cliente
 * y calcula cuánto debe pagar en total.
 */
@Entity
@Table(name = "FACTURAS")
public class Factura {
    // Un código único para identificar cada factura
    @Id
    @Column(name = "FACTURA_ID")
    private String id;
    
    // Lista de servicios veterinarios incluidos en esta factura
    @ManyToMany
    @JoinTable(
        name = "FACTURA_SERVICIOS",
        joinColumns = @JoinColumn(name = "FACTURA_ID"),
        inverseJoinColumns = @JoinColumn(name = "SERVICIO_ID")
    )
    private List<Servicio> servicios;
    
    // El monto total a pagar, calculado sumando el costo de todos los servicios
    @Column(name = "TOTAL", nullable = false)
    private double total;
    
    // Indica si la factura ya fue pagada (true) o aún está pendiente (false)
    @Column(name = "PAGADA", nullable = false)
    private boolean pagada;

    /**
     * Constructor vacío que crea una factura en estado "no pagada" por defecto
     */
    public Factura() {
        this.pagada = false;
    }

    /**
     * Constructor que crea una factura con ID y servicios específicos.
     * El total se calcula automáticamente y la factura se marca como no pagada.
     */
    public Factura(String id, List<Servicio> servicios) {
        this.id = id;
        this.servicios = servicios;
        this.pagada = false;
        this.calcularTotal();
    }

    /**
     * Método interno que suma el costo de todos los servicios para obtener el total
     */
    private void calcularTotal() {
        this.total = servicios.stream().mapToDouble(Servicio::getCosto).sum();
    }

    /**
     * Obtiene el identificador único de la factura
     */
    public String getId() {
        return id;
    }

    /**
     * Establece o cambia el identificador de la factura
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Obtiene la lista de servicios incluidos en la factura
     */
    public List<Servicio> getServicios() {
        return servicios;
    }

    /**
     * Establece o reemplaza la lista de servicios y recalcula el total
     */
    public void setServicios(List<Servicio> servicios) {
        this.servicios = servicios;
        this.calcularTotal();
    }

    /**
     * Obtiene el monto total a pagar por todos los servicios
     */
    public double getTotal() {
        return total;
    }

    /**
     * Verifica si la factura ya fue pagada
     */
    public boolean isPagada() {
        return pagada;
    }

    /**
     * Marca la factura como pagada o pendiente
     */
    public void setPagada(boolean pagada) {
        this.pagada = pagada;
    }
} 