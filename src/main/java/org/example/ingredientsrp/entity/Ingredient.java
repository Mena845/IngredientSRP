package org.example.ingredientsrp.entity;
import lombok.Getter;
import lombok.Setter;
import org.example.ingredientsrp.entity.Dish;


public class Ingredient {

    private Integer id;
    @Setter
    private String name;
    private String category;
    @Getter
    private double price;
    private Dish dish;
    @Getter
    @Setter
    private Double requiredQuantity;

    public Ingredient() {}

    public Ingredient(Integer id, String name, String category, double price) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
    }

    public Ingredient(int id, String name, double price, CategoryEnum category, Double requiredQuantity, Dish dish) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.category = String.valueOf(category);
        this.requiredQuantity = requiredQuantity;
        this.dish = dish;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setPrice(double price) {
        this.price = price;
    }

}