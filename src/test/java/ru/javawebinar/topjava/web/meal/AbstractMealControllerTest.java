package ru.javawebinar.topjava.web.meal;

import org.springframework.beans.factory.annotation.Autowired;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.web.AbstractControllerTest;

abstract public class AbstractMealControllerTest extends AbstractControllerTest {

    @Autowired
    protected MealService service;
}
