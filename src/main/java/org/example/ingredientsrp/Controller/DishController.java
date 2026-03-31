package org.example.ingredientsrp.Controller;

import org.example.ingredientsrp.entity.Dish;
import org.example.ingredientsrp.entity.Ingredient;
import org.example.ingredientsrp.exception.NotFoundException;
import org.example.ingredientsrp.repository.DishRepository;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/dishes")
public class DishController {

    private final DishRepository dishRepository;

    public DishController(DishRepository dishRepository) {
        this.dishRepository = dishRepository;
    }

    @GetMapping
    public List<Dish> getAll() throws SQLException {
        return dishRepository.findAll();
    }

    @GetMapping("/{id}")
    public Dish getById(@PathVariable int id) throws SQLException {
        Dish dish = dishRepository.findById(id);
        if (dish == null) {
            throw new NotFoundException("Dish.id=" + id + " not found");
        }
        return dish;
    }

    @PutMapping("/{id}/ingredients")
    public String updateDishIngredients(@PathVariable int id,
                                        @RequestBody List<Ingredient> ingredients) throws SQLException {
        Dish dish = dishRepository.findById(id);
        if (dish == null) {
            throw new NotFoundException("Dish.id=" + id + " not found");
        }
        dishRepository.updateIngredients(id, ingredients);
        return "Ingredients updated successfully";
    }

    // Nouveau endpoint GET /dishes/{id}/ingredients?ingredientName={i}&ingredientPriceAround={p}
    @GetMapping("/{id}/ingredients")
    public List<Ingredient> getIngredientsFiltered(
            @PathVariable int id,
            @RequestParam(required = false) String ingredientName,
            @RequestParam(required = false) Double ingredientPriceAround
    ) throws SQLException {
        Dish dish = dishRepository.findById(id);
        if (dish == null) {
            throw new NotFoundException("Dish.id=" + id + " not found");
        }

        return dishRepository.findIngredientsByDishIdFiltered(id, ingredientName, ingredientPriceAround);
    }
}