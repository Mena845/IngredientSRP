package org.example.ingredientsrp.dataSource;

import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Component
public class DataSource {
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/mini_dish_db",
                "mini_dish_db_manager",
                "123456"
        );
    }
}