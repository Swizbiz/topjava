package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;

import java.util.List;

public interface MealDao {
        List<MealTo> findAll();
        void add(Meal meal);
        void delete(int id);
        void update(Meal meal);
        Meal getById(int id);
}
