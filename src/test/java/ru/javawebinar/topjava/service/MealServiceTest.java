package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.ADMIN_ID;
import static ru.javawebinar.topjava.UserTestData.USER_ID;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {

    static {
        SLF4JBridgeHandler.install();
    }

    @Autowired
    private MealService service;

    @Test
    public void get() {
        Meal meal = service.get(MEAL1.getId(), USER_ID);
        assertMatch(meal, MEAL1);
    }

    @Test(expected = NotFoundException.class)
    public void getNotFound() {
        service.get(MEAL7.getId(), USER_ID);
    }

    @Test
    public void getBetweenDateTimes() {
        List<Meal> all = service.getBetweenDateTimes(
                LocalDateTime.of(2015, 5, 30, 8, 0, 0),
                LocalDateTime.of(2015, 5, 30, 19, 0, 0),
                USER_ID);
        assertMatch(all, MEAL2, MEAL1);
    }

    @Test
    public void getBetweenDates() {
        List<Meal> all = service.getBetweenDates(
                LocalDate.of(2015, 5, 31),
                LocalDate.of(2015, 5, 31),
                USER_ID);
        assertMatch(all, MEAL6, MEAL5, MEAL4);
    }

    @Test
    public void getAll() {
        List<Meal> all = service.getAll(USER_ID);
        assertMatch(all, MEAL6, MEAL5, MEAL4, MEAL3, MEAL2, MEAL1);
    }

    @Test(expected = NotFoundException.class)
    public void deleteStrange() throws Exception {
        service.delete(MEAL5.getId(), ADMIN_ID);
    }

    @Test
    public void delete() {
        service.delete(MEAL4.getId(), USER_ID);
        List<Meal> all = service.getAll(USER_ID);
        assertMatch(all, MEAL6, MEAL5, MEAL3, MEAL2, MEAL1);
    }

    @Test(expected = NotFoundException.class)
    public void updateStrange() throws Exception {
        Meal newMeal = new Meal(MEAL8.getId(), LocalDateTime.now(), "dinner", 1555);
        service.update(newMeal, USER_ID);
    }

    @Test
    public void update() {
        Meal newMeal = new Meal(MEAL8);
        service.update(newMeal, ADMIN_ID);
        List<Meal> all = service.getAll(ADMIN_ID);
        assertMatch(all, newMeal, MEAL7);
    }

    @Test(expected = DuplicateKeyException.class)
    public void createDuplicateDateTime() {
        Meal newMeal = new Meal(null, LocalDateTime.of(2015, 5, 30, 17, 0, 0), "dinner", 1555);
        service.create(newMeal, USER_ID);
    }

    @Test
    public void create() {
        Meal newMeal = new Meal(null, LocalDateTime.of(2015, 5, 31, 16, 0, 0), "dinner", 555);
        service.create(newMeal, USER_ID);
        List<Meal> all = service.getAll(USER_ID);
        assertMatch(all, MEAL6, MEAL5, newMeal, MEAL4, MEAL3, MEAL2, MEAL1);
    }
}