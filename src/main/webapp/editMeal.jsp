<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Edit meal</title>
</head>
<body>
<form method="POST" action='meals?action=listMeals' name="formAddMeal">
    Meal ID : <input type="text" readonly="readonly" name="mealId"
                     value="<c:out value="${meal.id}" />"/> <br/>
    Description : <input type="text" name="description"
                         value="<c:out value="${meal.description}" />"/> <br/>
    Calories : <input type="text" name="calories"
                      value="<c:out value="${meal.calories}"/>"/> <br/>
    Date : <input type="text" name="date"
                  value="${meal.dateTime.format(formatter)}"/> <br/>
    <input type="submit" value="Submit"/>
</form>
</body>
</html>
