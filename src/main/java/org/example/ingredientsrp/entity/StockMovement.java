package org.example.ingredientsrp.entity;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

public class StockMovement {

    private Integer id;
    private Instant createdAt;
    @Getter @Setter
    private String unit;
    @Getter @Setter
    private double value;
    @Getter @Setter
    private String type;


    private Ingredient ingredient;


    public StockMovement() {}

    public StockMovement(Integer id, Instant createdAt, String unit, double value, String type) {
        this.id = id;
        this.createdAt = createdAt;
        this.unit = unit;
        this.value = value;
        this.type = type;
    }


    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public Ingredient getIngredient() { return ingredient; }
    public void setIngredient(Ingredient ingredient) { this.ingredient = ingredient; }
}