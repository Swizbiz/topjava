package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalTime;
import java.util.List;

public class MealDAOFromMemory implements MealDAO {
    @Override
    public List<MealTo> getAll() {
        return MealsUtil.getFilteredWithExcess(Service.getAllMeal(), LocalTime.MIN, LocalTime.MAX, 2000);
    }
}
