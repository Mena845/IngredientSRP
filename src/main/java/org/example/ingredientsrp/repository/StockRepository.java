package org.example.ingredientsrp.repository;

import org.example.ingredientsrp.entity.StockValue;
import org.example.ingredientsrp.entity.UniteEnum;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.time.Instant;

@Repository
public class StockRepository {

    private final DataSource dataSource;

    public StockRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public StockValue getStockAt(Instant t, Integer ingredientId) throws SQLException {

        String sql = """
            SELECT unit,
                   SUM(
                       CASE 
                           WHEN type = 'IN' THEN quantity
                           WHEN type = 'OUT' THEN -quantity
                       END
                   ) AS total
            FROM stock_movement
            WHERE id_ingredient = ? AND creation_datetime <= ?
            GROUP BY unit
        """;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, ingredientId);
            ps.setTimestamp(2, Timestamp.from(t));

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                StockValue s = new StockValue();
                s.setQuantity(rs.getDouble("total"));

                // 🔥 CORRECTION ICI
                String unitStr = rs.getString("unit");
                s.setUnit(UniteEnum.valueOf(unitStr.toUpperCase()));

                return s;
            }

            return new StockValue(0.0, UniteEnum.PCS);
        }
    }
}