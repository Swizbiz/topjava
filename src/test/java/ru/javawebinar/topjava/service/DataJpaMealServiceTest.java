package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.model.Meal;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.UserTestData.*;


@ActiveProfiles(Profiles.DATAJPA)
public class DataJpaMealServiceTest extends MealServiceTest {
    @Test
    public void getWithUser() {
        List<Meal> mealList = service.getWithUser(ADMIN_ID);
        mealList.forEach(m -> assertThat(m.getUser()).isEqualTo(ADMIN));
    }
}
