package com.training.restaurant.services.dishes;

import com.training.restaurant.dto.dishes.CreateDishDto;
import com.training.restaurant.dto.dishes.UpdateDishDto;
import com.training.restaurant.models.DishType;
import com.training.restaurant.models.Dishes;
import com.training.restaurant.repositories.DishesRepository;
import com.training.restaurant.utils.DishesConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DishServiceImplTest {

    @Mock
    private DishesRepository dishesRepository;

    @InjectMocks
    private DishServiceImpl dishService;

    private CreateDishDto createDishDto;
    private CreateDishDto createDishDto2;
    private UpdateDishDto updateDishDto;
    private Dishes dish;
    private List<Dishes> dishes = new LinkedList<>();

    @BeforeEach
    void setUp() {
        createDishDto = new CreateDishDto("Pizza", 25.0);
        createDishDto2 = new CreateDishDto("Hamburguesa",25.0);
        updateDishDto = new UpdateDishDto("Pizza doble queso", DishType.COMMON.typeToString(), 30.0);
        dish = DishesConverter.toCreateDish(createDishDto);
        dishes = DishesConverter.toDishesList(List.of(createDishDto, createDishDto2));
    }

    @Test
    @DisplayName("Should create a new dish")
    void createDish() {
        when(dishesRepository.save(any(Dishes.class))).thenReturn(dish);
        Dishes result = dishService.createDish(dish);
        assertNotNull(result);
        assertEquals(dish.getName(), result.getName());
        assertEquals(dish.getPrice(), result.getPrice());

        verify(dishesRepository).save(any(Dishes.class));
    }

    @Test
    @DisplayName("Should find all dishes")
    void findAllDishes() {
        when(dishesRepository.findAll()).thenReturn(dishes);

        List<Dishes> dishesDb = dishService.findAllDishes();
        assertNotNull(dishesDb);
        assertEquals(dishes.size(), dishesDb.size());
        assertEquals(dishes.get(0).getName(), dishesDb.get(0).getName());
        assertEquals(dishes.get(0).getPrice(), dishesDb.get(0).getPrice());

        verify(dishesRepository).findAll();
    }

    @Test
    @DisplayName("Should create all dishes")
    void createAllDishes() {
        when(dishesRepository.saveAll(any())).thenReturn(dishes);

        List<Dishes> dishesDb = dishService.createAllDishes(dishes);
        assertNotNull(dishesDb);
        assertEquals(dishes.size(), dishesDb.size());
        assertEquals(dishes.get(0).getName(), dishesDb.get(0).getName());
        assertEquals(dishes.get(0).getPrice(), dishesDb.get(0).getPrice());

        verify(dishesRepository).saveAll(any());
    }

    @Test
    @DisplayName("Should find a dish by id")
    void findDishById() {
        when(dishesRepository.findById(anyLong())).thenReturn(Optional.of(dish));

        Dishes dishDb = dishService.findDishById(1L);
        assertNotNull(dishDb);
        assertEquals(dish.getName(), dishDb.getName());
        assertEquals(dish.getType().typeToString(), dishDb.getType().typeToString());
        assertEquals(dish.getPrice(), dishDb.getPrice());

        verify(dishesRepository).findById(anyLong());
    }

    @Test
    @DisplayName("Should update a dish")
    void updateDish() {
        when(dishesRepository.findById(anyLong())).thenReturn(Optional.of(dish));
        when(dishesRepository.save(any(Dishes.class))).thenReturn(dish);

        Dishes result = dishService.updateDish(1L, updateDishDto);
        assertNotNull(result);
        assertEquals(dish.getName(), result.getName());
        assertEquals(dish.getPrice(), result.getPrice());

        verify(dishesRepository).findById(anyLong());
        verify(dishesRepository).save(any(Dishes.class));
    }

    @Test
    @DisplayName("Should remove a dish")
    void removeDish() {
        when(dishesRepository.findById(anyLong())).thenReturn(Optional.of(dish));
        doNothing().when(dishesRepository).delete(any(Dishes.class));

        dishService.removeDish(1L);
        verify(dishesRepository).delete(any(Dishes.class));
    }

    @Test
    @DisplayName("Should find a dish by name")
    void findDishByName() {
        when(dishesRepository.findByName(anyString())).thenReturn(Optional.of(dish));

        Dishes result = dishService.findDishByName("Pizza");
        assertNotNull(result);
        assertEquals(dish.getName(), result.getName());
        assertEquals(dish.getPrice(), result.getPrice());
        verify(dishesRepository).findByName(anyString());
    }
}