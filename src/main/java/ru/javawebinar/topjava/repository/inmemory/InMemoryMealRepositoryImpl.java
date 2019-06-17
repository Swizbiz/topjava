package ru.javawebinar.topjava.repository.inmemory;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.AbstractBaseEntity;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepositoryImpl implements MealRepository {
    private Map<Integer, Meal> repository = new ConcurrentHashMap<>();
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
            repository.put(meal.getId(), meal);
            return meal;
        }
        // treat case: update, but absent in storage
        if (repository.get(meal.getId()).getUserId() == userId) {
            meal.setUserId(userId);
            return repository.computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
        } else
            return null;
    }

    @Override
    public boolean delete(int id, int userId) {
        for (Map.Entry<Integer, Meal> entry : repository.entrySet()) {
            if (entry.getKey() == id && isCorrectUserId(entry.getValue(), userId)) {
                repository.remove(id);
                return true;
            }
        }
        return false;
    }

    @Override
    public Meal get(int id, int userId) {
        Meal meal = repository.get(id);
        return isCorrectUserId(meal, userId) ? meal : null;
    }

    @Override
    public Collection<Meal> getAll(int userId) {
        return repository.values()
                .stream()
                .filter(meal -> isCorrectUserId(meal, userId))
                .sorted(Comparator.comparing(Meal::getDate).reversed().thenComparing(AbstractBaseEntity::getId))
                .collect(Collectors.toList());
    }

    private static boolean isCorrectUserId(Meal meal, int userId) {
        return meal.getUserId() == userId;
    }
}

