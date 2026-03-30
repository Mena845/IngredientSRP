package org.example.ingredientsrp.Controller;


import org.example.entity.Dish;
import org.example.repository.DishRepository;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.ArrayList;
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
        // Exemple simplifié : récupérer tous les plats
        List<Dish> list = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            Dish d = dishRepository.findById(i);
            if (d != null) list.add(d);
        }
        return list;
    }
}