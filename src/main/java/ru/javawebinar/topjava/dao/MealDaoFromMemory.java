package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class MealDaoFromMemory implements MealDao {
    private static final CopyOnWriteArrayList<Meal> mealList = new CopyOnWriteArrayList<>();
    private static AtomicInteger count = new AtomicInteger();
    static {
        mealList.add(new Meal(LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Завтрак", 500));
        mealList.add(new Meal(LocalDateTime.of(2015, Month.MAY, 30, 13, 0), "Обед", 1000));
        mealList.add(new Meal(LocalDateTime.of(2015, Month.MAY, 30, 20, 0), "Ужин", 500));
        mealList.add(new Meal(LocalDateTime.of(2015, Month.MAY, 31, 10, 0), "Завтрак", 1000));
        mealList.add(new Meal(LocalDateTime.of(2015, Month.MAY, 31, 13, 0), "Обед", 500));
        mealList.add(new Meal(LocalDateTime.of(2015, Month.MAY, 31, 20, 0), "Ужин", 510));
        for (Meal meal : mealList) {
            meal.setId(count.incrementAndGet());
        }
    }

    @Override
    public List<MealTo> findAll() {
        return MealsUtil.getAll(mealList, 2000);
    }

    @Override
    public void add(Meal meal) {
        if (meal.getId() == 0)
            meal.setId(count.incrementAndGet());
        mealList.add(meal);
    }

    @Override
    public void delete(int id) {
        mealList.stream()
                .filter(meal -> meal.getId() == id)
                .findFirst()
                .ifPresent(mealList::remove);
    }

    @Override
    public void update(Meal meal) {
        delete(meal.getId());
        add(meal);
    }

    @Override
    public Meal getById(int id) {
        return mealList.stream()
                .filter(meal -> meal.getId() == id)
                .findFirst()
                .get();
    }

}
