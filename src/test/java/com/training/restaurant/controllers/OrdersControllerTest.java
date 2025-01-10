package com.training.restaurant.controllers;

import com.training.restaurant.dto.dishes.CreateDishDto;
import com.training.restaurant.dto.orders.CreateOrderDto;
import com.training.restaurant.dto.orders.OrderDishDto;
import com.training.restaurant.dto.orders.OrdersDto;
import com.training.restaurant.models.Customer;
import com.training.restaurant.models.Dishes;
import com.training.restaurant.models.Orders;
import com.training.restaurant.services.dishes.DishServiceImpl;
import com.training.restaurant.services.orders.OrderServiceImpl;
import com.training.restaurant.services.orders.OrdersDishesServiceImpl;
import com.training.restaurant.utils.DishesConverter;
import com.training.restaurant.utils.OrdersConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


class OrdersControllerTest {

    private WebTestClient webTestClient;
    private OrdersDishesServiceImpl ordersDishesService;
    private OrderServiceImpl orderService;
    private DishServiceImpl dishService;
    private CreateOrderDto createOrderDto;
    private OrdersDto ordersDto;
    private List<OrderDishDto> orderDishDtoList;
    private CreateDishDto createDishDto;
    private OrderDishDto orderDishDto;
    List<Orders> ordersList;
    private Dishes dish;
    private Orders orders;

    @BeforeEach
    void setUp() {
        orderService = mock(OrderServiceImpl.class);
        ordersDishesService = mock(OrdersDishesServiceImpl.class);
        dishService = mock(DishServiceImpl.class);
        webTestClient = WebTestClient.bindToController(new OrdersController(ordersDishesService, orderService)).build();

        createDishDto = new CreateDishDto("Pizza",25.0);
        dish = DishesConverter.toCreateDish(createDishDto);

        orderDishDto = new OrderDishDto("Pizza", 2);
        createOrderDto = new CreateOrderDto(List.of(orderDishDto));
        orderDishDtoList = List.of(orderDishDto);
        Customer customer = new Customer();
        customer.setName("Ana Torres");
        orders = OrdersConverter.toOrders(customer);
        orders.setId(1L);

        orders.setOrderDishes(ordersDishesService.associateDishes(createOrderDto, orders));
        ordersDto = OrdersConverter.toOrdersDto(orders);


        Orders order1 = new Orders(50.0);
        Orders order2 = new Orders(21.0);
        Orders order3 = new Orders(34.0);
        ordersList = List.of(order1, order2, order3);
    }

    @Test
    @DisplayName("Should be able to create an order")
    void createOrder() {
        when(dishService.findDishByName("Pizza")).thenReturn(dish);
        when(ordersDishesService.createOrder(anyLong(), any(CreateOrderDto.class))).thenReturn(orders);

        webTestClient
                .post()
                .uri("/orders/customer/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(createOrderDto)
                .exchange()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectStatus().isCreated()
                .expectBody(OrdersDto.class)
                .value(orderDto1 -> {
                    assertEquals(ordersDto.id(), orderDto1.id());
                    assertEquals(ordersDto.customer(), orderDto1.customer());
                    assertEquals(ordersDto.total(), orderDto1.total());
                    assertEquals(ordersDto.dishes().size(), orderDto1.dishes().size());
                });
    }

    @Test
    @DisplayName("Should be able to find all orders")
    void findAllOrders() {
        when(orderService.findAllOrders()).thenReturn(ordersList);

        webTestClient
                .get()
                .uri("/orders")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(OrdersDto.class)
                .hasSize(3)
                .value(ordersDtos -> {
                    assertEquals(3, ordersDtos.size());
        });
    }

    @Test
    @DisplayName("Should be able to find an order by id")
    void findOrderById() {
        when(orderService.findOrderById(anyLong())).thenReturn(orders);

        webTestClient
                .get()
                .uri("/orders/{id}", 1L)
                .exchange()
                .expectStatus().isOk()
                .expectBody(OrdersDto.class)
                .value(order -> {
                    assertEquals(1L, order.id());
                    assertEquals("Ana Torres", order.customer());
                });
    }

    @Test
    @DisplayName("Should be able to delete an order")
    void deleteOrder() {

        doNothing().when(orderService).deleteOrder(anyLong());

        webTestClient
                .delete()
                .uri("/orders/{id}", 1L)
                .exchange()
                .expectStatus().isNoContent();
    }
}