package com.servicios.vet.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuración para habilitar CORS (Cross-Origin Resource Sharing).
 * 
 * CORS es un mecanismo de seguridad que permite o restringe las solicitudes
 * que provienen de diferentes orígenes (dominios) que no sean el mismo
 * donde se está ejecutando la aplicación.
 * 
 * Esta configuración es necesaria para que el frontend (HTML/JavaScript)
 * pueda comunicarse con el backend (Spring Boot) incluso si están en
 * diferentes dominios o puertos.
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * Configura las reglas CORS para permitir las solicitudes entre dominios.
     * 
     * En este caso, se configuran permisos amplios para desarrollo:
     * - Permite solicitudes desde cualquier origen (*)
     * - Permite los métodos HTTP más comunes (GET, POST, PUT, DELETE, OPTIONS)
     * - Permite cualquier cabecera HTTP
     * 
     * Nota: En un entorno de producción, sería recomendable restringir
     * los orígenes permitidos por razones de seguridad.
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")                // Aplica a todas las rutas
                .allowedOrigins("*")              // Permite cualquier origen
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Métodos permitidos
                .allowedHeaders("*");             // Permite cualquier cabecera
    }
} 