package com.training.restaurant.services;

import com.training.restaurant.models.Orders;
import com.training.restaurant.repositories.OrdersRepository;
import com.training.restaurant.services.interfaces.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderServiceImpl implements IOrderService {

    private final OrdersRepository ordersRepository;

    @Autowired
    public OrderServiceImpl(OrdersRepository ordersRepository) {
        this.ordersRepository = ordersRepository;
    }

    @Override
    public Orders createOrder(Orders orders) {
        try{
            return ordersRepository.save(orders);
        }catch (DataIntegrityViolationException e){
            throw new RuntimeException("Ocurrio un error al intentar guardar la orden");
        }
    }

    @Override
    public List<Orders> findAllOrders() {
        return ordersRepository.findAll();
    }
}
