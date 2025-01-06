package com.training.restaurant.dto;

import com.training.restaurant.models.Dishes;

public record DishesDto(
        Long id,
        String name,
        String type,
        Double price
) {
    public DishesDto(Dishes dishes) {
        this(
                dishes.getId(),
                dishes.getName(),
                dishes.getType().typeToString(),
                dishes.getPrice());
    }
}
