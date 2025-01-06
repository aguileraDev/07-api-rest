package com.training.restaurant.services;

import com.training.restaurant.dto.CreateOrderDto;
import com.training.restaurant.models.Customer;
import com.training.restaurant.models.Orders;
import com.training.restaurant.services.interfaces.IOrderDishesService;
import com.training.restaurant.utils.OrdersConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrdersDishesServiceImpl implements IOrderDishesService {

    private final CustomerServiceImpl customerService;
    private final OrderServiceImpl orderService;
    private final DishServiceImpl dishService;

    @Autowired
    public OrdersDishesServiceImpl(CustomerServiceImpl customerService, OrderServiceImpl orderService, DishServiceImpl dishService) {
        this.orderService = orderService;
        this.dishService = dishService;
        this.customerService = customerService;
    }

    @Override
    public Orders createOrder(Long customerId, CreateOrderDto createOrderDto) {
        Customer customer = customerService.findCustomerById(customerId);
        Orders orders = OrdersConverter.toOrders(customer);
        orders.setDishes(dishService.findAllDishesByOrders(createOrderDto.dishes()));
        return orderService.createOrder(orders);

    }
}
