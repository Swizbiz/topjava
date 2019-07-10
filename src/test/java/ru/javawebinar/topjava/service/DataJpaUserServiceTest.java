package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.UserTestData;
import ru.javawebinar.topjava.model.User;

import static org.assertj.core.api.Assertions.assertThat;


@ActiveProfiles(Profiles.DATAJPA)
public class DataJpaUserServiceTest extends UserServiceTest {
    @Test
    public void getWithMeal() {
        User user = service.getWithMeal(UserTestData.USER_ID);
        assertThat(user.getMeals()).containsAll(MealTestData.MEALS);
    }
}
