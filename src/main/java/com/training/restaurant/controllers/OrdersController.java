package com.training.restaurant.controllers;

import com.training.restaurant.dto.CreateOrderDto;
import com.training.restaurant.dto.OrdersDto;
import com.training.restaurant.models.Orders;
import com.training.restaurant.services.OrderServiceImpl;
import com.training.restaurant.services.OrdersDishesServiceImpl;
import com.training.restaurant.utils.OrdersConverter;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/orders")
public class OrdersController {

    private final OrdersDishesServiceImpl ordersDishesService;
    private final OrderServiceImpl orderService;
    private final static Logger logger = LoggerFactory.getLogger(OrdersController.class);

    @Autowired
    public OrdersController(OrdersDishesServiceImpl ordersDishesService, OrderServiceImpl orderService) {
        this.ordersDishesService = ordersDishesService;
        this.orderService = orderService;
    }

    @PostMapping("customer/{id}")
    public ResponseEntity<OrdersDto> createOrder(@PathVariable Long id, @RequestBody @Valid CreateOrderDto createOrderDto){
        logger.info("taking new order for customer" + id);
        Orders orders = ordersDishesService.createOrder(id, createOrderDto);
        URI uri = UriComponentsBuilder.fromUriString("/orders/{id}").buildAndExpand(orders.getId()).toUri();
        return ResponseEntity.created(uri).body(OrdersConverter.toOrdersDto(orders));

    }
}
