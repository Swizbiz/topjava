<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Edit meal</title>
</head>
<body>
<form method="POST" action='meals' name="formAddMeal">
    <input hidden="hidden" type="text" readonly="readonly" name="mealId"
           value="<c:out value="${meal.id}" />"/>
    Description : <input type="text" name="description"
                         value="<c:out value="${meal.description}" />"/> <br/>
    Calories : <input type="text" name="calories"
                      value="<c:out value="${meal.calories}"/>"/> <br/>
    Date and time: <input type="datetime-local" name="date"
                          value="${meal.dateTime}"/> <br/>

    <input type="submit" value="Submit"/>
    <button formaction="meals?action=listMeals"> Back</button>
</form>
</body>
</html>
