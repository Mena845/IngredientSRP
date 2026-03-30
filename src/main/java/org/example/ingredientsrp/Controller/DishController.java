package org.example.ingredientsrp.Controller;

import org.example.ingredientsrp.entity.Dish;
import org.example.ingredientsrp.entity.Ingredient;
import org.example.ingredientsrp.exception.NotFoundException;
import org.example.ingredientsrp.exception.BadRequestException;
import org.example.ingredientsrp.repository.DishRepository;
import org.example.ingredientsrp.repository.IngredientRepository;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/dishes")
public class DishController {

    private final DishRepository dishRepository;
    private final IngredientRepository ingredientRepository;

    public DishController(DishRepository dishRepository,
                          IngredientRepository ingredientRepository) {
        this.dishRepository = dishRepository;
        this.ingredientRepository = ingredientRepository;
    }

    // GET /dishes
    @GetMapping
    public List<Dish> getAll() throws SQLException {
        return dishRepository.findAll();
    }

    // PUT /dishes/{id}/ingredients
    @PutMapping("/{id}/ingredients")
    public Dish updateDishIngredients(@PathVariable int id,
                                      @RequestBody List<Ingredient> ingredients) throws SQLException {

        if (ingredients == null || ingredients.isEmpty()) {
            throw new BadRequestException("Request body containing ingredients list is mandatory");
        }

        Dish dish = dishRepository.findById(id);
        if (dish == null) throw new NotFoundException("Dish.id=" + id + " is not found");

        // Filtrer les ingrédients existants seulement
        List<Ingredient> validIngredients = ingredients.stream()
                .map(i -> {
                    try {
                        return ingredientRepository.findById(i.getId());
                    } catch (SQLException e) {
                        return null;
                    }
                })
                .filter(i -> i != null)
                .collect(Collectors.toList());

        // Mettre à jour la base (associer/détacher les ingrédients)
        dishRepository.updateIngredients(dish.getId(), validIngredients);

        // Retourner le plat avec ingrédients mis à jour
        dish.setIngredients(validIngredients);
        return dish;
    }
}