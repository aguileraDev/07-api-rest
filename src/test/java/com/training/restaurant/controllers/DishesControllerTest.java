package com.training.restaurant.controllers;

import com.training.restaurant.dto.dishes.CreateDishDto;
import com.training.restaurant.dto.dishes.DishesDto;
import com.training.restaurant.dto.dishes.UpdateDishDto;
import com.training.restaurant.models.DishType;
import com.training.restaurant.models.Dishes;
import com.training.restaurant.services.dishes.DishServiceImpl;
import com.training.restaurant.services.menus.MenuDishServiceImpl;
import com.training.restaurant.utils.DishesConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class DishesControllerTest {

    private DishServiceImpl dishService;
    private MenuDishServiceImpl menuDishService;
    private WebTestClient webTestClient;
    private Dishes dish;
    private CreateDishDto createDishDto;
    private CreateDishDto createDishDto2;
    private DishesDto dishesDto;
    private UpdateDishDto updateDishDto;
    private List<CreateDishDto> createDishDtosList = new ArrayList<>();
    private List<Dishes> dishes = new ArrayList<>();

    @BeforeEach
    void setUp() {
        dishService = mock(DishServiceImpl.class);
        menuDishService = mock(MenuDishServiceImpl.class);
        webTestClient = WebTestClient.bindToController(new DishesController(dishService,menuDishService)).build();

        createDishDto = new CreateDishDto("Pizza",25.0);
        createDishDto2 = new CreateDishDto("Hamburguesa",25.0);
        dish = DishesConverter.toCreateDish(createDishDto);
        dishesDto = DishesConverter.toDishesDto(dish);
        updateDishDto = new UpdateDishDto("Pizza doble queso", DishType.COMMON.typeToString(), 30.0);

        createDishDtosList = List.of(createDishDto, createDishDto2);
        dishes = DishesConverter.toDishesList(createDishDtosList);

    }

    @Test
    @DisplayName("Should create a new dish")
    void createDish() {
        when(menuDishService.createDishForExistingMenu(anyLong(), any(CreateDishDto.class))).thenReturn(DishesConverter.toCreateDish(createDishDto));

        webTestClient
                .post()
                .uri("/dishes/menu/{menuId}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(createDishDto)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(DishesDto.class)
                .value(dish -> {
                    assertEquals(dishesDto.name(), dish.name());
                    assertEquals(dishesDto.type(), dish.type());
                    assertEquals(dishesDto.price(), dish.price());
                });
    }

    @Test
    @DisplayName("Should return all dishes")
    void findAllDishes() {
        when(dishService.findAllDishes()).thenReturn(dishes);

        webTestClient
                .get()
                .uri("/dishes")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(DishesDto.class)
                .hasSize(2)
                .value(dishes -> {
                    assertEquals(2, dishes.size());
                    assertEquals(dishesDto.name(), dishes.get(0).name());
                    assertEquals(dishesDto.type(), dishes.get(0).type());
                    assertEquals(dishesDto.price(), dishes.get(0).price());
                });
    }

    @Test
    @DisplayName("Should return a dish by id")
    void findDishById() {
        when(dishService.findDishById(anyLong())).thenReturn(dish);

        webTestClient
                .get()
                .uri("/dishes/{id}", 1L)
                .exchange()
                .expectStatus().isOk()
                .expectBody(DishesDto.class)
                .value(dish -> {
                    assertEquals(dishesDto.id(), dish.id());
                    assertEquals(dishesDto.name(), dish.name());
                    assertEquals(dishesDto.type(), dish.type());
                    assertEquals(dishesDto.price(), dish.price());
                });
    }

    @Test
    @DisplayName("Should update a dish")
    void updateDish() {
        when(dishService.updateDish(anyLong(), any(UpdateDishDto.class))).thenReturn(dish);

        webTestClient
                .put()
                .uri("/dishes/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(updateDishDto)
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    @DisplayName("Should delete a dish")
    void deleteDish() {
        doNothing().when(dishService).removeDish(anyLong());

        webTestClient
                .delete()
                .uri("/dishes/{id}", 1L)
                .exchange()
                .expectStatus().isNoContent();
    }
}