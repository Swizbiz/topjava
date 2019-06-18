package ru.javawebinar.topjava.repository.inmemory;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
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
        if (meal != null) {
            if (meal.isNew()) {
                meal.setId(counter.incrementAndGet());
                meal.setUserId(userId);
                repository.computeIfAbsent(userId, HashMap::new).put(meal.getId(), meal);
                return meal;
            } else if (repository.containsKey(userId) && repository.get(userId).containsKey(meal.getId())) {
                Meal oldMeal = repository.get(userId).get(meal.getId());
                if (isCorrectUserId(oldMeal, userId)) {
                    meal.setUserId(userId);
                    return repository.get(userId).put(meal.getId(), meal);
                }
            }
            return null;
        } else
            return null;
    }

    @Override
    public boolean delete(int id, int userId) {
        if (repository.containsKey(userId) && repository.get(userId).containsKey(id)) {
            Meal meal = repository.get(userId).get(id);
            if (meal != null && isCorrectUserId(meal, userId))
                return repository.get(userId).remove(id) != null;
            return false;
        } else
            return false;
    }

    @Override
    public Meal get(int id, int userId) {
        if (repository.containsKey(userId) && repository.get(userId).containsKey(id)) {
            Meal meal = repository.get(userId).get(id);
            if (meal != null && isCorrectUserId(meal, userId))
                return meal;
        }
        return null;
    }

    @Override
    public List<Meal> getAll(int userId) {
        List<Meal> list = Collections.emptyList();
        if (repository.containsKey(userId)) {
            list = repository.get(userId).values()
                    .stream()
                    .filter(meal -> isCorrectUserId(meal, userId))
                    .sorted(Comparator.comparing(Meal::getDate).reversed())
                    .collect(Collectors.toList());
        }
        return list;
    }

    @Override
    public List<Meal> getAllBetweenDays(int userId, LocalDate startDate, LocalDate endDate) {
        List<Meal> list = Collections.emptyList();
        if (repository.containsKey(userId)) {
            list = repository.get(userId).values()
                    .stream()
                    .filter(meal -> DateTimeUtil.isBetween(meal.getDate(), startDate, endDate))
                    .filter(meal -> isCorrectUserId(meal, userId))
                    .sorted(Comparator.comparing(Meal::getDate).reversed())
                    .collect(Collectors.toList());
        }
        return list;
    }

    private static boolean isCorrectUserId(Meal meal, int userId) {
        return meal.getUserId() == userId;
    }
}

