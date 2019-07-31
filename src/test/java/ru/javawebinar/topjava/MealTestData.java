package ru.javawebinar.topjava;

import org.springframework.test.web.servlet.ResultMatcher;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.Month;
import java.util.Collection;
import java.util.List;

import static java.time.LocalDateTime.of;
import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.TestUtil.*;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

public class MealTestData {
    public static final int MEAL1_ID = START_SEQ + 2;
    public static final int ADMIN_MEAL_ID = START_SEQ + 8;

    public static final Meal MEAL1 = new Meal(MEAL1_ID, of(2015, Month.MAY, 30, 10, 0), "Завтрак", 500);
    public static final Meal MEAL2 = new Meal(MEAL1_ID + 1, of(2015, Month.MAY, 30, 13, 0), "Обед", 1000);
    public static final Meal MEAL3 = new Meal(MEAL1_ID + 2, of(2015, Month.MAY, 30, 20, 0), "Ужин", 500);
    public static final Meal MEAL4 = new Meal(MEAL1_ID + 3, of(2015, Month.MAY, 31, 10, 0), "Завтрак", 500);
    public static final Meal MEAL5 = new Meal(MEAL1_ID + 4, of(2015, Month.MAY, 31, 13, 0), "Обед", 1000);
    public static final Meal MEAL6 = new Meal(MEAL1_ID + 5, of(2015, Month.MAY, 31, 20, 0), "Ужин", 510);
    public static final Meal ADMIN_MEAL1 = new Meal(ADMIN_MEAL_ID, of(2015, Month.JUNE, 1, 14, 0), "Админ ланч", 510);
    public static final Meal ADMIN_MEAL2 = new Meal(ADMIN_MEAL_ID + 1, of(2015, Month.JUNE, 1, 21, 0), "Админ ужин", 1500);

    public static final List<Meal> MEALS = List.of(MEAL6, MEAL5, MEAL4, MEAL3, MEAL2, MEAL1);

    public static final MealTo MEAL1_TO = new MealTo(MEAL1.getId(), MEAL1.getDateTime(), MEAL1.getDescription(), MEAL1.getCalories(), false);
    public static final MealTo MEAL2_TO = new MealTo(MEAL2.getId(), MEAL2.getDateTime(), MEAL2.getDescription(), MEAL2.getCalories(), false);
    public static final MealTo MEAL3_TO = new MealTo(MEAL3.getId(), MEAL3.getDateTime(), MEAL3.getDescription(), MEAL3.getCalories(), false);
    public static final MealTo MEAL4_TO = new MealTo(MEAL4.getId(), MEAL4.getDateTime(), MEAL4.getDescription(), MEAL4.getCalories(), true);
    public static final MealTo MEAL5_TO = new MealTo(MEAL5.getId(), MEAL5.getDateTime(), MEAL5.getDescription(), MEAL5.getCalories(), true);
    public static final MealTo MEAL6_TO = new MealTo(MEAL6.getId(), MEAL6.getDateTime(), MEAL6.getDescription(), MEAL6.getCalories(), true);
    public static final List<MealTo> MEALS_TO = List.of(MEAL6_TO, MEAL5_TO, MEAL4_TO, MEAL3_TO, MEAL2_TO, MEAL1_TO);

    public static Meal getCreated() {
        return new Meal(null, of(2015, Month.JUNE, 1, 18, 0), "Созданный ужин", 300);
    }

    public static Meal getUpdated() {
        return new Meal(MEAL1_ID, MEAL1.getDateTime(), "Обновленный завтрак", 200);
    }

    public static void assertMatch(Meal actual, Meal expected) {
        assertThat(actual).isEqualToIgnoringGivenFields(expected, "user");
    }

    public static void assertMatch(Iterable<Meal> actual, Meal... expected) {
        assertMatch(actual, List.of(expected));
    }

    public static void assertMatch(Iterable<Meal> actual, Iterable<Meal> expected) {
        assertThat(actual).usingElementComparatorIgnoringFields("user").isEqualTo(expected);
    }

    public static ResultMatcher contentJson(Meal... expected) {
        return result -> assertMatch(readListFromJsonMvcResult(result, Meal.class), List.of(expected));
    }

    public static void assertMatchMealTo(Iterable<MealTo> actual, Iterable<MealTo> expected) {
        assertThat(actual).isEqualTo(expected);
    }

    public static ResultMatcher contentJson(Collection<MealTo> expected) {
        return result -> assertMatchMealTo(readListFromJsonMvcResult(result, MealTo.class), expected);
    }
}
