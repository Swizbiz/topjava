package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.web.meal.MealRestController;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

public class MealServlet extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(MealServlet.class);

    private MealRestController controller;
    private ConfigurableApplicationContext appCtx;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        appCtx = new ClassPathXmlApplicationContext("spring/spring-app.xml");
        controller = appCtx.getBean(MealRestController.class);
    }

    @Override
    public void destroy() {
        super.destroy();
        appCtx.close();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String id = request.getParameter("id");

        Meal meal = new Meal(id.isEmpty() ? null : Integer.valueOf(id),
                LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"),
                Integer.parseInt(request.getParameter("calories")));

        if (meal.isNew()) {
            log.info("Create {}", meal);
            controller.create(meal);
        } else {
            log.info("Update {}", meal);
            controller.update(meal, getId(request));
        }
        response.sendRedirect("meals");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        switch (action == null ? "all" : action) {
            case "delete":
                int id = getId(request);
                log.info("Delete {}", id);
                controller.delete(id);
                response.sendRedirect("meals");
                break;
            case "create":
            case "update":
                final Meal meal = "create".equals(action) ?
                        new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000) :
                        controller.get(getId(request));
                request.setAttribute("meal", meal);
                request.getRequestDispatcher("/mealForm.jsp").forward(request, response);
                break;
            case "reset":
                response.sendRedirect("meals");
                break;
            case "all":
            default:
                LocalDate localStartDate = null;
                String startDate = request.getParameter("startDate");
                if (startDate != null && !startDate.isEmpty()) {
                    localStartDate = LocalDate.parse(startDate);
//                    request.setAttribute("startDate", localStartDate);
                }

                LocalDate localEndDate = null;
                String endDate = request.getParameter("endDate");
                if (endDate != null && !endDate.isEmpty()) {
                    localEndDate = LocalDate.parse(endDate);
//                    request.setAttribute("endDate", localEndDate);
                }

                LocalTime localStartTime = null;
                String startTime = request.getParameter("startTime");
                if (startTime != null && !startTime.isEmpty()) {
                    localStartTime = LocalTime.parse(startTime);
//                    request.setAttribute("startTime", localStartTime);
                }

                LocalTime localEndTime = null;
                String endTime = request.getParameter("endTime");
                if (endTime != null && !endTime.isEmpty()) {
                    localEndTime = LocalTime.parse(endTime);
//                    request.setAttribute("endTime", localEndTime);
                }

                if (localStartDate == null && localEndDate == null && localStartTime == null && localEndTime == null)
                    request.setAttribute("meals", controller.getAll());
                else
                    request.setAttribute("meals", controller.getAll(localStartDate, localEndDate, localStartTime, localEndTime));
                request.getRequestDispatcher("/meals.jsp").forward(request, response);
        }
    }

    private int getId(HttpServletRequest request) {
        String paramId = Objects.requireNonNull(request.getParameter("id"));
        return Integer.parseInt(paramId);
    }

    private <T> T getParameter(HttpServletRequest request, String parameterName, Class<T> clazz) {
        T ret = null;
        String parameter = request.getParameter(parameterName);
        if (parameter != null && !parameter.isEmpty()) {
            try {
                Method parse = clazz.getDeclaredMethod("parse", CharSequence.class);
                parse.setAccessible(true);
                ret = (T) parse.invoke(parameter);
                request.setAttribute(parameterName, ret);
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return ret;
    }
}
