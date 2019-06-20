package ru.javawebinar.topjava.repository.inmemory;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepositoryImpl implements MealRepository {
    private Map<Integer, Map<Integer, Meal>> repository = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.MEALS.forEach(meal -> save(meal, 1));
        save(new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "dinner1", 1000), 2);
        save(new Meal(LocalDateTime.now().minusHours(2).truncatedTo(ChronoUnit.MINUTES), "dinner2", 1000), 2);
        save(new Meal(LocalDateTime.now().minusHours(5).truncatedTo(ChronoUnit.MINUTES), "dinner3", 1), 2);
        save(new Meal(LocalDateTime.now().minusDays(1).truncatedTo(ChronoUnit.MINUTES), "dinner3", 5500), 2);
    }

    @Override
    public Meal save(Meal meal, int userId) {
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            meal.setUserId(userId);
            repository.computeIfAbsent(userId, HashMap::new).put(meal.getId(), meal);
            return meal;
        } else {
            Map<Integer, Meal> meals = repository.get(userId);
            if (meals != null) {
                meal.setUserId(userId);
                return meals.put(meal.getId(), meal);
            }
        }
        return null;
    }

    @Override
    public boolean delete(int id, int userId) {
        Map<Integer, Meal> meals = repository.get(userId);
        return meals != null && meals.remove(id) != null;
    }

    @Override
    public Meal get(int id, int userId) {
        Map<Integer, Meal> meals = repository.get(userId);
        return meals != null ? meals.get(id) : null;
    }

    @Override
    public List<Meal> getAll(int userId) {
        return getAllBetweenDays(userId, meal -> true);
    }

    @Override
    public List<Meal> getAllBetweenDays(int userId, Predicate<Meal> filter) {
        Map<Integer, Meal> meals = repository.get(userId);
        return meals == null ? Collections.emptyList() : meals.values()
                .stream()
                .filter(filter)
                .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                .collect(Collectors.toList());

    }

}

