package com.training.restaurant.services.interfaces;

import com.training.restaurant.models.Orders;

import java.util.List;

public interface IOrderService {
    Orders createOrder(Orders orders);
    List<Orders> findAllOrders();
    Orders findOrderById(Long id);
    void deleteOrder(Long id);

}
