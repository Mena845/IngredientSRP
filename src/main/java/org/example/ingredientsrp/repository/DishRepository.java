package org.example.ingredientsrp.repository;

import org.example.ingredientsrp.entity.*;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class DishRepository {

    private final DataSource dataSource;

    public DishRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Dish findById(int id) throws SQLException {
        String sql = """
            SELECT d.id AS d_id, d.name AS d_name, d.dish_type,
                   i.id AS i_id, i.name AS i_name, i.price, i.category, i.required_quantity
            FROM dish d
            LEFT JOIN dish_ingredient di ON d.id = di.dish_id
            LEFT JOIN ingredient i ON i.id = di.ingredient_id
            WHERE d.id = ?
        """;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            Dish dish = null;
            List<Ingredient> ingredients = new ArrayList<>();

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    if (dish == null) {
                        dish = new Dish(
                                rs.getInt("d_id"),
                                rs.getString("d_name"),
                                DishTypeEnum.valueOf(rs.getString("dish_type"))
                        );
                    }
                    if (rs.getObject("i_id") != null) {
                        ingredients.add(new Ingredient(
                                rs.getInt("i_id"),
                                rs.getString("i_name"),
                                rs.getDouble("price"),
                                CategoryEnum.valueOf(rs.getString("category")),
                                rs.getObject("required_quantity", Double.class),
                                dish
                        ));
                    }
                }
            }

            if (dish != null) {
                dish.setIngredients(ingredients);
            }

            return dish;
        }
    }

    public List<Dish> findAll() throws SQLException {
        List<Dish> dishes = new ArrayList<>();
        String sql = "SELECT id FROM dish ORDER BY id";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                Dish dish = findById(id);
                if (dish != null) dishes.add(dish);
            }
        }
        return dishes;
    }

    public void updateIngredients(int dishId, List<Ingredient> ingredients) throws SQLException {
        try (Connection conn = dataSource.getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement psDelete = conn.prepareStatement(
                    "DELETE FROM dish_ingredient WHERE dish_id = ?")) {
                psDelete.setInt(1, dishId);
                psDelete.executeUpdate();
            }

            try (PreparedStatement psInsert = conn.prepareStatement(
                    "INSERT INTO dish_ingredient(dish_id, ingredient_id) VALUES (?, ?)")) {
                for (Ingredient i : ingredients) {
                    psInsert.setInt(1, dishId);
                    psInsert.setInt(2, i.getId());
                    psInsert.executeUpdate();
                }
            }

            conn.commit();
        } catch (SQLException e) {
            throw e;
        }
    }

    // Nouvelle méthode pour récupérer les ingrédients filtrés
    public List<Ingredient> findIngredientsByDishIdFiltered(int dishId, String ingredientName, Double ingredientPriceAround) throws SQLException {
        StringBuilder sql = new StringBuilder("""
            SELECT i.id, i.name, i.price, i.category, i.required_quantity
            FROM ingredient i
            INNER JOIN dish_ingredient di ON i.id = di.ingredient_id
            WHERE di.dish_id = ?
        """);

        if (ingredientName != null && !ingredientName.isEmpty()) {
            sql.append(" AND i.name ILIKE ?");
        }
        if (ingredientPriceAround != null) {
            sql.append(" AND i.price BETWEEN ? AND ?");
        }

        List<Ingredient> ingredients = new ArrayList<>();

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            int paramIndex = 1;
            ps.setInt(paramIndex++, dishId);

            if (ingredientName != null && !ingredientName.isEmpty()) {
                ps.setString(paramIndex++, "%" + ingredientName + "%");
            }
            if (ingredientPriceAround != null) {
                ps.setDouble(paramIndex++, ingredientPriceAround - 50);
                ps.setDouble(paramIndex++, ingredientPriceAround + 50);
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ingredients.add(new Ingredient(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getDouble("price"),
                            CategoryEnum.valueOf(rs.getString("category")),
                            rs.getObject("required_quantity", Double.class),
                            null // On ne set pas le Dish ici
                    ));
                }
            }
        }

        return ingredients;
    }
}