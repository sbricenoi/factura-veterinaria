package com.servicios.vet.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
public class DatabaseConfig {

    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    @Bean
    public DataSource dataSource() {
        // Configurar propiedades del sistema para el wallet
        String walletPath = extractWalletPath(url);
        System.setProperty("oracle.net.tns_admin", walletPath);

        // Configurar HikariDataSource
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(url);
        config.setUsername(username);
        config.setPassword(password);
        config.setDriverClassName("oracle.jdbc.OracleDriver");
        
        // Configuraciones adicionales de conexión
        config.setMaximumPoolSize(10);
        config.setMinimumIdle(5);
        config.setConnectionTimeout(30000);
        config.setIdleTimeout(600000);
        config.setMaxLifetime(1800000);
        config.setPoolName("OracleHikariPool");
        
        // Propiedades adicionales de conexión
        Properties props = new Properties();
        props.setProperty("oracle.jdbc.timezoneAsRegion", "false");
        config.setDataSourceProperties(props);
        
        return new HikariDataSource(config);
    }

    private String extractWalletPath(String url) {
        // Extraer la ruta del wallet de la URL de conexión
        int startIndex = url.indexOf("MY_WALLET_DIRECTORY=") + "MY_WALLET_DIRECTORY=".length();
        int endIndex = url.indexOf(")", startIndex);
        return url.substring(startIndex, endIndex);
    }
} 