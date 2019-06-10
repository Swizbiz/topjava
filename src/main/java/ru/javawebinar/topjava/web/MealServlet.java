package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.javawebinar.topjava.dao.MealDao;
import ru.javawebinar.topjava.dao.MealDaoFromMemory;
import ru.javawebinar.topjava.model.Meal;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MealServlet extends HttpServlet {
    private static final String INSERT_OR_EDIT = "/editMeal.jsp";
    private static final String MEALS = "/meals.jsp";
    private MealDao mealDao = new MealDaoFromMemory();
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
    private static final Logger LOG = LoggerFactory.getLogger(MealServlet.class);

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        request.setAttribute("formatter", formatter);

        String description = request.getParameter("description");
        int calories = Integer.parseInt(request.getParameter("calories"));
        String date = request.getParameter("date");
        LocalDateTime parsedDate = LocalDateTime.parse(date, formatter);

        Meal meal = new Meal(parsedDate, description, calories);
        String mealId = request.getParameter("mealId");
        if (mealId == null || mealId.isEmpty()) {
            mealDao.add(meal);
        } else {
            meal.setId(Integer.parseInt(mealId));
            mealDao.update(meal);
        }

        LOG.debug("forward to meals.jsp");
        request.setAttribute("meals", mealDao.findAll());
        request.getRequestDispatcher(MEALS).forward(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        request.setAttribute("formatter", formatter);

        String forward = "";
        String action = request.getParameter("action");

        if (action.equalsIgnoreCase("delete")) {
            int mealId = Integer.parseInt(request.getParameter("mealId"));
            mealDao.delete(mealId);
            forward = MEALS;
            request.setAttribute("meals", mealDao.findAll());
        } else if (action.equalsIgnoreCase("edit")) {
            forward = INSERT_OR_EDIT;
            int mealId = Integer.parseInt(request.getParameter("mealId"));
            Meal meal = mealDao.getById(mealId);
            request.setAttribute("meal", meal);
        } else if (action.equalsIgnoreCase("listMeals")) {
            forward = MEALS;
            request.setAttribute("meals", mealDao.findAll());
        } else if (action.equalsIgnoreCase("insert")) {
            forward = INSERT_OR_EDIT;
        }

        LOG.debug(forward.equals(MEALS) ? "forward to meals.jsp" : "forward to editMeal.jsp");
        request.getRequestDispatcher(forward).forward(request, response);
    }
}
