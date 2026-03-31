package org.example.ingredientsrp.dataSource;

import org.springframework.boot.jdbc.autoconfigure.DataSourceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataSource {

    @Bean
    public javax.sql.DataSource appDataSource(DataSourceProperties properties) {
        return properties.initializeDataSourceBuilder().build();
    }
}