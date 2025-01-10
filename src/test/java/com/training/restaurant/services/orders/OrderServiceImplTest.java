package com.training.restaurant.services.orders;

import com.training.restaurant.events.NewOrderEvent;
import com.training.restaurant.models.Customer;
import com.training.restaurant.models.Dishes;
import com.training.restaurant.models.Orders;
import com.training.restaurant.repositories.OrdersRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock
    private OrdersRepository ordersRepository;
    @Mock
    private ApplicationEventPublisher eventPublisher;
    @InjectMocks
    private OrderServiceImpl orderService;

    private Customer customer;
    private Orders orders;
    private List<Orders> ordersList = new LinkedList<>();

    @BeforeEach
    void setUp() {
        customer = new Customer();
        orders = new Orders(100.0);
        Orders order1 = new Orders(50.0);
        Orders order2 = new Orders(21.0);
        Orders order3 = new Orders(34.0);
        ordersList = List.of(order1, order2, order3);
    }

    @Test
    @DisplayName("Should be create Order")
    void createOrder() {
        when(ordersRepository.save(any(Orders.class))).thenReturn(orders);
        Orders result = orderService.createOrder(orders);

        verify(ordersRepository, times(1)).save(orders);
        verify(eventPublisher, times(1)).publishEvent(any(NewOrderEvent.class));

        assert result.getTotal() == 100.0;
    }

    @Test
    @DisplayName("Should be able to find all orders")
    public void findAllOrders() {

        when(ordersRepository.findAll()).thenReturn(ordersList);
        List<Orders> result = orderService.findAllOrders();
        verify(ordersRepository, times(1)).findAll();
        assertEquals(3, result.size());
    }

    @Test
    @DisplayName("Should be able to find order by id")
    public void findOrderById() {

        when(ordersRepository.findById(anyLong())).thenReturn(Optional.of(orders));
        Orders result = orderService.findOrderById(1L);
        verify(ordersRepository, times(1)).findById(1L);
        assertEquals(100.0, result.getTotal());
    }

    @Test
    @DisplayName("Should be able to delete order")
    public void deleteOrder() {
        when(ordersRepository.findById(anyLong())).thenReturn(Optional.of(orders));
        doNothing().when(ordersRepository).delete(orders);
        orderService.deleteOrder(1L);
        verify(ordersRepository, times(1)).delete(orders);
    }

    @Test
    @DisplayName("Should be able to find count orders by customer")
    public void findCountOrdersByCustomer() {
        when(ordersRepository.countByCustomer(any(Customer.class))).thenReturn(5L);
        Long result = orderService.findCountOrdersByCustomer(customer);
        verify(ordersRepository, times(1)).countByCustomer(customer);
        assertEquals(5L, result);
    }

    @Test
    @DisplayName("Should be able to count dish purchases")
    public void countDishPurchases() {
        Dishes dish = new Dishes();
        dish.setId(0L);
        when(ordersRepository.countDishPurchases(0L)).thenReturn(1L);

        Long result = orderService.countDishPurchases(dish);

        verify(ordersRepository, times(1)).countDishPurchases(0L);
        assertEquals(1L, result);
    }



}
