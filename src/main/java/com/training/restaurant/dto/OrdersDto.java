package com.training.restaurant.dto;

import com.training.restaurant.models.OrderDish;
import com.training.restaurant.models.Orders;

import java.time.ZonedDateTime;
import java.util.List;

public record OrdersDto(
        Long id,
        String customer,
        List<DishWithQuantityDto> dishes,
        Integer quantity,
        Double total,
        ZonedDateTime createdAt
) {
    public OrdersDto(Orders orders) {
        this(
                orders.getId(),
                orders.getCustomer().getName(),
                orders.getOrderDishes().stream().map(DishWithQuantityDto::new).toList(),
                orders.getOrderDishes().stream().mapToInt(OrderDish::getQuantity).sum(),
                orders.getOrderDishes().stream().mapToDouble(o -> o.getDish().getPrice() * o.getQuantity()).sum(),
                orders.getCreatedAt()
        );
    }
}

