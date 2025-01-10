package com.training.restaurant.services.menus;

import com.training.restaurant.dto.dishes.CreateDishDto;
import com.training.restaurant.dto.menus.CreateMenuDto;
import com.training.restaurant.dto.menus.MenuDto;
import com.training.restaurant.dto.menus.UpdateMenuDto;
import com.training.restaurant.models.Menu;
import com.training.restaurant.repositories.MenuRepository;
import com.training.restaurant.utils.MenuConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MenuServiceImplTest {

    @Mock
    private MenuRepository menuRepository;

    @InjectMocks
    private MenuServiceImpl menuService;

    private CreateMenuDto createMenuDto;
    private UpdateMenuDto updateMenuDto;
    private List<CreateDishDto> createDishDtoList;
    private List<Menu> menuList = new ArrayList<>();
    private Menu menu;

    @BeforeEach
    void setUp() {
        createDishDtoList = List.of(
                new CreateDishDto("Pizza doble queso", 99.0),
                new CreateDishDto("Pizza especial", 82.0),
                new CreateDishDto("Pizza napolitana", 70.0)
        );
        List<CreateDishDto> createMenuDtoList2 = List.of(
                new CreateDishDto("Lesagne", 82.0),
                new CreateDishDto("Carbonara", 99.0)
        );
        createMenuDto = new CreateMenuDto("Pizza locura", "friday", createDishDtoList);
        CreateMenuDto createMenuDto2 = new CreateMenuDto("Sabor italia", "saturday", createMenuDtoList2);
        menu = MenuConverter.toMenu(createMenuDto);
        Menu menu2 = MenuConverter.toMenu(createMenuDto2);

        menuList.add(menu);
        menuList.add(menu2);

        updateMenuDto = new UpdateMenuDto("Italia sensation", "wednesday");
    }

    @Test
    @DisplayName("Should be able to create a menu")
    void createMenu() {
        when(menuRepository.save(any(Menu.class))).thenReturn(menu);

        Menu menuDb = menuService.createMenu(createMenuDto);
        assertEquals(menu.getName(), menuDb.getName());
        assertEquals(menu.getSchedule(), menuDb.getSchedule());
        assertEquals(menu.getDishes(), menuDb.getDishes());

        verify(menuRepository).save(any(Menu.class));
    }

    @Test
    @DisplayName("Should be able to find all menus")
    void findAllMenus() {
        when(menuRepository.findAll()).thenReturn(menuList);

        List<MenuDto> menuDtoList = menuService.findAllMenus();

        assertNotNull(menuDtoList);
        assertEquals(menuList.size(), menuDtoList.size());
        assertEquals(menuList.get(0).getName(), menuDtoList.get(0).name());
        assertEquals(menuList.get(0).getSchedule(), menuDtoList.get(0).schedule());
        assertEquals(menuList.get(1).getName(), menuDtoList.get(1).name());
        assertEquals(menuList.get(1).getSchedule(), menuDtoList.get(1).schedule());

        verify(menuRepository).findAll();
    }

    @Test
    @DisplayName("Should be able to find a menu by id")
    void findMenuById() {
        when(menuRepository.findById(anyLong())).thenReturn(Optional.of(menu));

        Menu menuDb = menuService.findMenuById(1L);

        assertNotNull(menuDb);
        assertEquals(menu.getName(), menuDb.getName());
        assertEquals(menu.getSchedule(), menuDb.getSchedule());
        assertEquals(menu.getDishes(), menuDb.getDishes());

        verify(menuRepository).findById(anyLong());
    }

    @Test
    @DisplayName("Should be able to remove a menu")
    void removeMenu() {
        when(menuRepository.findById(anyLong())).thenReturn(Optional.of(menu));

        menuService.removeMenu(1L);

        verify(menuRepository).findById(1L);
        verify(menuRepository).delete(menu);
    }

    @Test
    @DisplayName("Should be able to update a menu")
    void updateMenu() {
        when(menuRepository.findById(anyLong())).thenReturn(Optional.of(menu));
        when(menuRepository.save(any(Menu.class))).thenReturn(menu);

        Menu menuUpdated = menuService.updateMenu(1L, updateMenuDto);

        assertNotNull(menuUpdated);
        assertEquals(updateMenuDto.name(), menuUpdated.getName());
        assertEquals(updateMenuDto.schedule(), menuUpdated.getSchedule());
        verify(menuRepository).findById(anyLong());
        verify(menuRepository).save(any(Menu.class));

    }

    @Test
    @DisplayName("Should update menu fields when both name and schedule are provided")
    void updateMenuFields_ShouldUpdateAllFields() {
        menuService.updateMenuFields(menu, updateMenuDto);

        assertEquals(updateMenuDto.name(), menu.getName());
        assertEquals(updateMenuDto.schedule(), menu.getSchedule());
    }

    @Test
    @DisplayName("Should update menu name when a new name is provided")
    void updateMenuNameIfPresent_ShouldUpdateName() {
        String newName = "Hamburguesas dto";

        menuService.updateMenuNameIfPresent(menu, newName);

        assertEquals(newName, menu.getName());
    }

    @Test
    @DisplayName("Should not update menu name when name is null")
    void updateMenuNameIfPresent_ShouldNotUpdateNameWhenNull() {
        menuService.updateMenuNameIfPresent(menu, null);

        assertEquals("Pizza locura", menu.getName());
    }

    @Test
    @DisplayName("Should update menu schedule when a new schedule is provided")
    void updateMenuScheduleIfPresent_ShouldUpdateSchedule() {
        String newSchedule = "thursday";

        menuService.updateMenuScheduleIfPresent(menu, newSchedule);

        assertEquals(newSchedule, menu.getSchedule());
    }

    @Test
    @DisplayName("Should not update menu schedule when schedule is null")
    void updateMenuScheduleIfPresent_ShouldNotUpdateScheduleWhenNull() {
        menuService.updateMenuScheduleIfPresent(menu, null);

        assertEquals("friday", menu.getSchedule());
    }


}