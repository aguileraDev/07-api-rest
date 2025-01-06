package com.training.restaurant.dto;

import com.training.restaurant.models.Orders;

import java.time.ZonedDateTime;
import java.util.List;

public record OrdersDto(
        String customer,
        List<DishesDto> dishes,
        ZonedDateTime createdAt
) {
    public OrdersDto(Orders orders) {
        this(
                orders.getCustomer().getName(),
                orders.getDishes().stream().map(DishesDto::new).toList(),
                orders.getCreatedAt()
        );
    }
}
