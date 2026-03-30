package org.example.ingredientsrp.entity;


public class StockValue {

    private double quantity;
    private UniteEnum unit;

    public StockValue(double quantity, UniteEnum unit) {
        this.quantity = quantity;
        this.unit = unit;
    }

    public StockValue() {

    }

    public double getQuantity() {
        return quantity;
    }

    public UniteEnum getUnit() {
        return unit;
    }
    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }
    public void setUnit(UniteEnum unit) {
        this.unit = unit;
    }

    @Override
    public String toString() {
        return quantity + " " + unit;
    }
}
