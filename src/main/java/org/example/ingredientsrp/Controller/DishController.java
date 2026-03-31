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

    // GET /dishes
    @GetMapping
    public List<Dish> getAll() throws SQLException {
        return dishRepository.findAll();
    }

    // GET /dishes/{id}
    @GetMapping("/{id}")
    public Dish getById(@PathVariable int id) throws SQLException {
        Dish dish = dishRepository.findById(id);
        if (dish == null) {
            throw new NotFoundException("Dish.id=" + id + " not found");
        }
        return dish;
    }

    // PUT /dishes/{id}/ingredients
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
}