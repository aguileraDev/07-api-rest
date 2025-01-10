package com.training.restaurant.services.menus;

import com.training.restaurant.dto.dishes.CreateDishDto;
import com.training.restaurant.dto.menus.CreateMenuDto;
import com.training.restaurant.models.Dishes;
import com.training.restaurant.models.Menu;
import com.training.restaurant.services.dishes.DishServiceImpl;
import com.training.restaurant.utils.DishesConverter;
import com.training.restaurant.utils.MenuConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
@ExtendWith(MockitoExtension.class)
class MenuDishServiceImplTest {

    @Mock
    private MenuServiceImpl menuService;

    @Mock
    private DishServiceImpl dishService;

    @InjectMocks
    private MenuDishServiceImpl menuDishService;

    private CreateDishDto createDishDto;
    private CreateMenuDto createMenuDto;
    private List<CreateDishDto> createDishDtoList;
    private Menu menu;
    private Dishes dish;
    private List<Dishes> dishes;

    @BeforeEach
    void setUp() {
        createDishDto = new CreateDishDto("Pizza doble queso", 99.0);
        createDishDtoList = List.of(
                new CreateDishDto("Pizza doble queso", 99.0),
                new CreateDishDto("Pizza especial", 82.0),
                new CreateDishDto("Pizza napolitana", 70.0)
        );
        createMenuDto = new CreateMenuDto("Pizza locura", "friday", createDishDtoList);
        menu = MenuConverter.toMenu(createMenuDto);
        dish = DishesConverter.toCreateDish(createDishDto);
        dishes = DishesConverter.toDishesListWithMenu(menu, createDishDtoList);
    }

    @Test
    @DisplayName("Should create a dish for an existing menu")
    void createDishForExistingMenu() {
        when(menuService.findMenuById(anyLong())).thenReturn(menu);
        when(dishService.createDish(any(Dishes.class))).thenReturn(dish);

        Dishes createdDish = menuDishService.createDishForExistingMenu(1L, createDishDto);

        assertEquals(dish.getName(), createdDish.getName());
        assertEquals(dish.getPrice(), createdDish.getPrice());
    }

    @Test
    @DisplayName("Should create a menu with dishes")
    void createMenuWithDishes() {
        when(menuService.createMenu(any(CreateMenuDto.class))).thenReturn(menu);
        when(dishService.createAllDishes(any(List.class))).thenReturn(dishes);

        Menu createdMenu = menuDishService.createMenuWithDishes(createMenuDto);

        assertEquals(menu.getName(), createdMenu.getName());
        assertEquals(menu.getDishes().size(), createdMenu.getDishes().size());
    }

    @Test
    @DisplayName("Should create dishes from menu")
    void createDishesFromMenu() {
        when(dishService.createAllDishes(any(List.class))).thenReturn(dishes);

        List<Dishes> createdDishes = menuDishService.createDishesFromMenu(menu, createDishDtoList);

        assertEquals(3, createdDishes.size());
        assertEquals(dish.getName(), createdDishes.get(0).getName());
    }
}