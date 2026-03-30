package org.example.ingredientsrp.Controller;

import org.example.ingredientsrp.entity.Ingredient;
import org.example.ingredientsrp.entity.StockValue;
import org.example.ingredientsrp.exception.NotFoundException;
import org.example.ingredientsrp.exception.BadRequestException;
import org.example.ingredientsrp.repository.IngredientRepository;
import org.example.ingredientsrp.repository.StockRepository;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/ingredients")
public class IngredientController {

    private final IngredientRepository ingredientRepository;
    private final StockRepository stockRepository;

    public IngredientController(IngredientRepository ingredientRepository,
                                StockRepository stockRepository) {
        this.ingredientRepository = ingredientRepository;
        this.stockRepository = stockRepository;
    }

    // GET /ingredients
    @GetMapping
    public List<Ingredient> getAll() throws SQLException {
        return ingredientRepository.findAll(0, 10);
    }

    // GET /ingredients/{id}
    @GetMapping("/{id}")
    public Ingredient getById(@PathVariable int id) throws SQLException {
        Ingredient i = ingredientRepository.findById(id);
        if (i == null) throw new NotFoundException("Ingredient.id=" + id + " is not found");
        return i;
    }

    // GET /ingredients/{id}/stock?at={temporal}&unit={unit}
    @GetMapping("/{id}/stock")
    public StockValue getStock(@PathVariable int id,
                               @RequestParam(required = false) String at,
                               @RequestParam(required = false) String unit) throws SQLException {

        if (at == null || unit == null) {
            throw new BadRequestException("Either mandatory query parameter `at` or `unit` is not provided.");
        }

        Ingredient i = ingredientRepository.findById(id);
        if (i == null) throw new NotFoundException("Ingredient.id=" + id + " is not found");

        StockValue stock = stockRepository.getStockAt(Instant.parse(at), id);

        return stock;
    }
}