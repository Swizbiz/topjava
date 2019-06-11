package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class MealDaoFromMemory implements MealDao {
    private static final Map<Integer, Meal> mealMap = new ConcurrentHashMap<>();
    private static AtomicInteger count = new AtomicInteger();

    {
        add(new Meal(count.incrementAndGet(), LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Завтрак", 500));
        add(new Meal(count.incrementAndGet(), LocalDateTime.of(2015, Month.MAY, 30, 13, 0), "Обед", 1000));
        add(new Meal(count.incrementAndGet(), LocalDateTime.of(2015, Month.MAY, 30, 20, 0), "Ужин", 500));
        add(new Meal(count.incrementAndGet(), LocalDateTime.of(2015, Month.MAY, 31, 10, 0), "Завтрак", 1000));
        add(new Meal(count.incrementAndGet(), LocalDateTime.of(2015, Month.MAY, 31, 13, 0), "Обед", 500));
        add(new Meal(count.incrementAndGet(), LocalDateTime.of(2015, Month.MAY, 31, 20, 0), "Ужин", 510));
    }

    @Override
    public Collection<Meal> findAll() {
        return mealMap.values();
    }

    @Override
    public void add(Meal meal) {
        if (meal.getId() == 0)
            mealMap.put(count.incrementAndGet(), new Meal(count.get(), meal.getDateTime(), meal.getDescription(), meal.getCalories()));
        else if (!mealMap.containsValue(meal))
            mealMap.put(meal.getId(), meal);
    }

    @Override
    public void delete(int id) {
        mealMap.remove(id);
    }

    @Override
    public void update(Meal meal) {
        delete(meal.getId());
        add(meal);
    }

    @Override
    public Meal getById(int id) {
        return mealMap.get(id);
    }

}
