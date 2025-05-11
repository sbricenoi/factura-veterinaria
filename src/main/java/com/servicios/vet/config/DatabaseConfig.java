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

    @Value("${oracle.net.wallet_location:}")
    private String walletPath;

    @Bean
    public DataSource dataSource() {
        // Configurar propiedades del sistema para el wallet de Oracle Cloud
        if (walletPath != null && !walletPath.isEmpty()) {
            System.setProperty("oracle.net.wallet_location", walletPath);
        }

        // Configurar HikariDataSource
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(url);
        config.setUsername(username);
        config.setPassword(password);
        config.setDriverClassName("oracle.jdbc.OracleDriver");
        
        // Configuraciones de conexión segura para Oracle Cloud
        Properties props = new Properties();
        props.setProperty("oracle.jdbc.timezoneAsRegion", "false");
        props.setProperty("oracle.net.ssl_server_dn_match", "true");
        
        // Configuraciones adicionales de HikariCP
        config.setMaximumPoolSize(10);
        config.setMinimumIdle(5);
        config.setConnectionTimeout(30000);
        config.setIdleTimeout(600000);
        config.setMaxLifetime(1800000);
        config.setPoolName("OracleCloudHikariPool");
        config.setConnectionTestQuery("SELECT 1 FROM DUAL");
        
        // Establecer propiedades adicionales
        config.setDataSourceProperties(props);
        
        return new HikariDataSource(config);
    }
} 