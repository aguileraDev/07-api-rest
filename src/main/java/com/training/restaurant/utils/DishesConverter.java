package com.training.restaurant.utils;

import com.training.restaurant.dto.CreateDishDto;

import com.training.restaurant.dto.DishesDto;
import com.training.restaurant.models.DishType;
import com.training.restaurant.models.Dishes;
import com.training.restaurant.models.Menu;

import java.util.List;

public class DishesConverter {

    public static Dishes toCreateDish(CreateDishDto createDishDto){
        return new Dishes(createDishDto.name(), DishType.COMMON.typeToString(), createDishDto.price());
    }

    public static Dishes toDish(DishesDto dishesDto){
        return new Dishes(dishesDto.id(),dishesDto.name(), dishesDto.type(), dishesDto.price());
    }
    public static DishesDto toDishesDto(Dishes dishes){
        return new DishesDto(dishes);
    }

    public static List<Dishes> toDishesList(List<DishesDto> dishesDtos){
        return dishesDtos.stream().map(DishesConverter::toDish).toList();
    }

    public static List<Dishes> toDishesListWithMenu(Menu menu, List<CreateDishDto> dishes){
        List<Dishes> dishesList = dishes.stream().map(DishesConverter::toCreateDish).toList();
        for (Dishes dish: dishesList){
            dish.setMenu(menu);
        }
        return dishesList;
    }

    public static List<DishesDto> toDishesDtoList(List<Dishes> dishes){
        return dishes.stream().map(DishesConverter::toDishesDto).toList();
    }
}
