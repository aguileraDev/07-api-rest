package com.training.restaurant.controllers;

import com.training.restaurant.dto.dishes.CreateDishDto;
import com.training.restaurant.dto.menus.CreateMenuDto;
import com.training.restaurant.dto.menus.MenuDto;
import com.training.restaurant.dto.menus.UpdateMenuDto;
import com.training.restaurant.models.Menu;
import com.training.restaurant.services.menus.MenuDishServiceImpl;
import com.training.restaurant.services.menus.MenuServiceImpl;
import com.training.restaurant.utils.MenuConverter;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class MenuControllerTest {
    private MenuDishServiceImpl menuDishService;
    private MenuServiceImpl menuService;

    private WebTestClient webTestClient;
    private CreateMenuDto createMenuDto;
    private Menu menu;
    private MenuDto menuDto;
    private UpdateMenuDto updateMenuDto;
    private List<CreateDishDto> createDishDtoList;

    @BeforeEach
    void setUp() {
        menuService = mock(MenuServiceImpl.class);
        menuDishService = mock(MenuDishServiceImpl.class);
        webTestClient = WebTestClient.bindToController(new MenuController(menuService, menuDishService)).build();

        createDishDtoList = List.of(
                new CreateDishDto("Pizza doble queso", 99.0),
                new CreateDishDto("Pizza especial", 82.0),
                new CreateDishDto("Pizza napolitana", 70.0)
        );
        createMenuDto = new CreateMenuDto("Pizza locura", "friday", createDishDtoList);
        menu = MenuConverter.toMenu(createMenuDto);
        menuDto = MenuConverter.toMenuDto(menu);
        updateMenuDto = new UpdateMenuDto("Sabor italia", "saturday");
    }

    @Test
    @DisplayName("Should be able to create a menu")
    void createMenu() {
        when(menuDishService.createMenuWithDishes(any(CreateMenuDto.class))).thenReturn(menu);

        webTestClient
                .post()
                .uri("/menu")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(createMenuDto)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(MenuDto.class)
                .value(menuDto1 -> {
                    assertEquals(menuDto.name(), menuDto1.name());
                    assertEquals(menuDto.schedule(), menuDto1.schedule());
                    assertEquals(menuDto.dishes().size(), menuDto1.dishes().size());
                });

        verify(menuDishService).createMenuWithDishes(createMenuDto);
    }

    @Test
    @DisplayName("Should be able to get all menus")
    void findAllMenus() {
        when(menuService.findAllMenus()).thenReturn(getMenus().stream().map(MenuConverter::toMenuDto).toList());

        webTestClient
                .get()
                .uri("/menu")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(MenuDto.class)
                .hasSize(2)
                .value(menuDtos -> {
                    assertEquals(2, menuDtos.size());
                    assertEquals(menuDto.name(), menuDtos.get(0).name());
                    assertEquals(menuDto.schedule(), menuDtos.get(0).schedule());
                    assertEquals(menuDto.dishes().size(), menuDtos.get(0).dishes().size());
                });

        verify(menuService).findAllMenus();
    }

    @Test
    @DisplayName("Should be able to find a menu by id")
    void findMenuById() {
        when(menuService.findMenuById(any(Long.class))).thenReturn(menu);

        webTestClient
                .get()
                .uri("/menu/{id}", 1L)
                .exchange()
                .expectStatus().isOk()
                .expectBody(MenuDto.class)
                .value(menuDto1 -> {
                    assertEquals(menuDto.name(), menuDto1.name());
                    assertEquals(menuDto.schedule(), menuDto1.schedule());
                    assertEquals(menuDto.dishes().size(), menuDto1.dishes().size());
                });

        verify(menuService).findMenuById(anyLong());
    }

    @Test
    @DisplayName("Should be able to update a menu")
    void updateMenu() {
        doNothing().when(menuService).updateMenu(anyLong(), any(UpdateMenuDto.class));

        webTestClient
                .put()
                .uri("/menu/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(updateMenuDto)
                .exchange()
                .expectStatus().isNoContent();

        verify(menuService).updateMenu(anyLong(), any(UpdateMenuDto.class));
    }

    @Test
    @DisplayName("Should be able to delete a menu")
    void deleteMenu() {
        doNothing().when(menuService).removeMenu(anyLong());

        webTestClient
                .delete()
                .uri("/menu/{id}", 1L)
                .exchange()
                .expectStatus().isNoContent();

        verify(menuService).removeMenu(anyLong());
    }

    private List<Menu> getMenus(){
        CreateMenuDto createMenuDto1 = new CreateMenuDto("Pizza locura", "friday", List.of(
                new CreateDishDto("Pizza doble queso", 99.0),
                new CreateDishDto("Pizza especial", 82.0),
                new CreateDishDto("Pizza napolitana", 70.0)
        ));
        CreateMenuDto createMenuDto2 = new CreateMenuDto("Verdadera italiana", "saturday", List.of(
                new CreateDishDto("Pasta con doble queso", 99.0),
                new CreateDishDto("Pizza especial", 82.0),
                new CreateDishDto("Carbonara", 70.0)
        ));
        return List.of(
                MenuConverter.toMenu(createMenuDto1),
                MenuConverter.toMenu(createMenuDto2)
        );
    }
}