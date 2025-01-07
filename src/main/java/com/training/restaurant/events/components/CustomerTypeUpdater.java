package com.training.restaurant.events.components;

import com.training.restaurant.models.Customer;
import com.training.restaurant.models.CustomerType;
import com.training.restaurant.events.NewOrderEvent;
import com.training.restaurant.models.Dishes;
import com.training.restaurant.models.Orders;
import com.training.restaurant.repositories.CustomerRepository;
import com.training.restaurant.services.OrderServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;


@Slf4j
@Component
public class CustomerTypeUpdater {

    private final CustomerRepository customerRepository;
    private final OrderServiceImpl orderService;

    @Autowired
    public CustomerTypeUpdater(CustomerRepository customerRepository, OrderServiceImpl orderService) {
        this.customerRepository = customerRepository;
        this.orderService = orderService;
    }

    @EventListener
    public void handleNewOrderEvent(NewOrderEvent event) {
        Orders order = event.getOrder();
        Customer customer = order.getCustomer();
        Long countOrders = orderService.findCountOrdersByCustomer(customer);
        log.info("Customer " + customer.getName() + " has " + countOrders + " orders.");

        if (!(countOrders < 10)) {
            customer.setType(CustomerType.FREQUENT);
            order.getOrderDishes().forEach(orderDish -> {
                Dishes dish = orderDish.getDish();
                double discountedPrice = dish.getPrice() * 0.9762;
                dish.setPrice(discountedPrice);
            });
        }
        customerRepository.save(customer);
    }
}