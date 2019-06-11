package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.javawebinar.topjava.dao.MealDao;
import ru.javawebinar.topjava.dao.MealDaoFromMemory;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.MealsUtil;

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
    private static final String REDIRECT_MEALS = "/topjava/meals";
    private static final Logger LOG = LoggerFactory.getLogger(MealServlet.class);
    private MealDao mealDao;
    private static int caloriesPerDay;
    private DateTimeFormatter formatter;

    @Override
    public void init() throws ServletException {
        super.init();
        mealDao = new MealDaoFromMemory();
        caloriesPerDay = 2000;
        formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String action = "";
        action = request.getParameter("action");
        if (action == null || action.isEmpty()) {
            String description = request.getParameter("description");
            int calories = Integer.parseInt(request.getParameter("calories"));
            String date = request.getParameter("date");
            LocalDateTime parsedDate = LocalDateTime.parse(date);
            String mealId = request.getParameter("mealId");
            if (mealId == null || mealId.isEmpty()) {
                mealDao.add(new Meal(0, parsedDate, description, calories));
            } else {
                mealDao.update(new Meal(Integer.parseInt(mealId), parsedDate, description, calories));
            }
        }
        LOG.debug("after edit meal redirect to meals.jsp");
        response.sendRedirect(REDIRECT_MEALS);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        request.setAttribute("formatter", formatter);

        String forward = "";
        String action = request.getParameter("action");

        if (action == null || action.isEmpty() || action.equalsIgnoreCase("listMeals")) {
            forward = MEALS;
            request.setAttribute("meals", MealsUtil.getAll(mealDao.findAll(), caloriesPerDay));
        } else if (action.equalsIgnoreCase("delete")) {
            int mealId = Integer.parseInt(request.getParameter("mealId"));
            mealDao.delete(mealId);
            LOG.debug("after delete redirect to meals");
            response.sendRedirect(REDIRECT_MEALS);
        } else if (action.equalsIgnoreCase("edit")) {
            forward = INSERT_OR_EDIT;
            int mealId = Integer.parseInt(request.getParameter("mealId"));
            Meal meal = mealDao.getById(mealId);
            request.setAttribute("meal", meal);
        } else if (action.equalsIgnoreCase("insert")) {
            forward = INSERT_OR_EDIT;
        }

        if (action == null || action.isEmpty() || !action.equalsIgnoreCase("delete")) {
            LOG.debug(forward.equals(MEALS) ? "forward to meals.jsp" : "forward to editMeal.jsp");
            request.getRequestDispatcher(forward).forward(request, response);
        }
    }
}
