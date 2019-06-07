<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Meals</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<table>
    <thead>
    <tr>
        <th>Date and time</th>
        <th>Description</th>
        <th>Calories</th>
    </tr>
    </thead>
    <c:forEach var="meal" items="${meals}">
        <tr style="${meal.excess ? "color:#ff0000" : "color:#00ff00"}">
            <td><c:out value="${meal.dateTime.format(formatter)}"/></td>
            <td><c:out value="${meal.description}"/></td>
            <td><c:out value="${meal.calories}"/></td>
        </tr>
    </c:forEach>
</table>
</body>
</html>
