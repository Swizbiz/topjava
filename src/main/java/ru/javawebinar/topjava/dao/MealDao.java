package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;

import java.util.Collection;

public interface MealDao {
        Collection<Meal> findAll();
        void add(Meal meal);
        void delete(int id);
        void update(Meal meal);
        Meal getById(int id);
}
