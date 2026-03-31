package org.example.ingredientsrp.Controller;

import org.example.ingredientsrp.entity.Dish;
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
        return dishRepository.findById(id);
    }
}