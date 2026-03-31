package org.example.ingredientsrp.repository;

import org.example.ingredientsrp.entity.CategoryEnum;
import org.example.ingredientsrp.entity.Ingredient;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class IngredientRepository {

    private final DataSource dataSource;

    // Injection automatique du DataSource configuré par Spring Boot
    public IngredientRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    // Récupérer tous les ingrédients avec pagination
    public List<Ingredient> findAll(int page, int size) throws SQLException {
        String sql = """
                SELECT id, name, price, category
                FROM ingredient
                ORDER BY id
                LIMIT ? OFFSET ?
                """;

        List<Ingredient> result = new ArrayList<>();

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, size);
            ps.setInt(2, page * size);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    result.add(new Ingredient(
                            rs.getInt("id"),
                            rs.getString("name"),
                            CategoryEnum.valueOf(rs.getString("category")),
                            rs.getDouble("price")
                    ));
                }
            }
        }

        return result;
    }

    // Récupérer un ingrédient par son identifiant
    public Ingredient findById(int id) throws SQLException {
        String sql = "SELECT id, name, price, category FROM ingredient WHERE id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Ingredient(
                            rs.getInt("id"),
                            rs.getString("name"),
                            CategoryEnum.valueOf(rs.getString("category")),
                            rs.getDouble("price")
                    );
                }
            }
        }

        return null; // Aucun ingrédient trouvé
    }
}