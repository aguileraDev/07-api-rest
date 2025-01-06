package com.training.restaurant.services.interfaces;

import com.training.restaurant.dto.CreateOrderDto;
import com.training.restaurant.models.Orders;

public interface IOrderDishesService {
    Orders createOrder(Long customerId, CreateOrderDto createOrderDto);
}
