package com.training.restaurant.services.orders;

import com.training.restaurant.dto.orders.CreateOrderDto;
import com.training.restaurant.dto.orders.OrderDishDto;
import com.training.restaurant.models.Customer;
import com.training.restaurant.models.DishType;
import com.training.restaurant.models.Dishes;
import com.training.restaurant.models.OrderDish;
import com.training.restaurant.models.Orders;
import com.training.restaurant.services.customer.CustomerServiceImpl;
import com.training.restaurant.services.dishes.DishServiceImpl;
import com.training.restaurant.utils.OrdersConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrdersDishesServiceImplTest {

    @Mock
    private CustomerServiceImpl customerService;

    @Mock
    private OrderServiceImpl orderService;

    @Mock
    private DishServiceImpl dishService;

    @InjectMocks
    private OrdersDishesServiceImpl ordersDishesService;

    private CreateOrderDto createOrderDto;
    private Orders orders;
    private Customer customer = new Customer();
    private Dishes dish;

    @BeforeEach
    void setUp() {
        dish = new Dishes("Pizza", DishType.POPULAR.typeToString(), 25.0);
        OrderDishDto orderDishDto = new OrderDishDto("Pizza", 2);
        createOrderDto = new CreateOrderDto(List.of(orderDishDto));
        customer.setName("Ana Torres");
        customer.setId(1L);
        orders = OrdersConverter.toOrders(customer);
        orders.setId(1L);
        orders.setOrderDishes(ordersDishesService.associateDishes(createOrderDto, orders));
    }

    @Test
    @DisplayName("Should create an order")
    void createOrder() {
        when(customerService.findCustomerById(anyLong())).thenReturn(customer);
        when(orderService.createOrder(any(Orders.class))).thenReturn(orders);

        Orders result = ordersDishesService.createOrder(1L, createOrderDto);

        assertNotNull(result);
        verify(customerService).findCustomerById(1L);
        verify(orderService).createOrder(any(Orders.class));

    }

    @Test
    @DisplayName("Should associate dishes")
    void associateDishes() {
        when(dishService.findDishByName("Pizza")).thenReturn(dish);

        List<OrderDish> result = ordersDishesService.associateDishes(createOrderDto, orders);

        assertEquals(1, result.size());
        assertEquals("Pizza", result.get(0).getDish().getName());
        verify(dishService, times(2)).findDishByName("Pizza");
    }
}