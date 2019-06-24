package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

public class MealTestData {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static final Meal MEAL1 = new Meal(START_SEQ + 2, LocalDateTime.parse("2019-06-22 08:00:00", DATE_TIME_FORMATTER), "dinner1", 1000);
    public static final Meal MEAL2 = new Meal(START_SEQ + 3, LocalDateTime.parse("2019-06-22 13:00:00", DATE_TIME_FORMATTER), "dinner2", 510);
    public static final Meal MEAL3 = new Meal(START_SEQ + 4, LocalDateTime.parse("2019-06-22 17:00:00", DATE_TIME_FORMATTER), "dinner3", 500);
    public static final Meal MEAL4 = new Meal(START_SEQ + 5, LocalDateTime.parse("2019-06-23 08:00:00", DATE_TIME_FORMATTER), "breakfast1", 1000);
    public static final Meal MEAL5 = new Meal(START_SEQ + 6, LocalDateTime.parse("2019-06-23 13:00:00", DATE_TIME_FORMATTER), "breakfast2", 500);
    public static final Meal MEAL6 = new Meal(START_SEQ + 7, LocalDateTime.parse("2019-06-23 17:00:00", DATE_TIME_FORMATTER), "breakfast3", 500);

    public static void assertMatch(Meal actual, Meal expected) {
        assertThat(actual).isEqualToIgnoringGivenFields(expected, "user_id");
    }

    public static void assertMatch(Iterable<Meal> actual, Meal... expected) {
        assertMatch(actual, Arrays.asList(expected));
    }

    public static void assertMatch(Iterable<Meal> actual, Iterable<Meal> expected) {
        assertThat(actual).usingElementComparatorIgnoringFields("user_id").isEqualTo(expected);
    }}
