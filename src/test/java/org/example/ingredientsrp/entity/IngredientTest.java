package org.example.ingredientsrp.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class IngredientTest {

    @Test
    void constructorShouldInitializeAllFields() {
        Ingredient ingredient = new Ingredient(1, "Salt", CategoryEnum.OTHER, 12.5);

        assertEquals(1, ingredient.getId());
        assertEquals("Salt", ingredient.getName());
        assertEquals("OTHER", ingredient.getCategory());
        assertEquals(12.5, ingredient.getPrice());
    }
}
