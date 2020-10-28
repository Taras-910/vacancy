<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<html>
<head>
    <title>Dishes</title>
    <link rel="stylesheet" href="../../css/style.css">
</head>
<body>
<section>
    <h3><a href="/vacancy">Home</a></h3>
    <hr/>
    <h2>Menus</h2>
    <hr/>
    <a href="vacancy/create">Add Menu</a>
    <br><br>
    <table border="1" cellpadding="8" cellspacing="0">
        <thead>
        <tr>
            <th>Employer name</th>
            <th hidden>Id</th>
            <th>
                <th1>VacancyTitle / </th1>
                <th1>Price</th1>
            </th>
            <th>like</th>
            <th></th>
            <th></th>
        </tr>
        </thead>
        <c:forEach items="${vacancies}" var="vacancy">
            <tr>
                <jsp:useBean id="menu" type="ua.training.top.to.VacancyTo"/>
                <td>${vacancy.name}</td>
                <td hidden>${vacancy.restaurant.id}</td>
                <td>
                    <c:forEach items="${menu.restaurant.dishes}" var="dish">
                        <tr1>
                            <jsp:useBean id="dish" type="ua.training.top.to.VacancyTo"/>
                            <td1>${vacancy.name}</td1>
                            /
                            <td1>${vacancy.price}</td1>
                            <td1><a href="vacancy?action=update&id=${dish.id}">Update</a></td1>
                            <td1><a href="vacancy?action=delete&id=${dish.id}">Delete</a></td1>
                        </tr1>
                        <br>
                    </c:forEach>
                    <a  href="/rest/admin/dishes/add">
                        Add Dish
                    </a>
                </td>
                <td>${vacancy.toVote}</td>
                <td><a href="vacancy?action=update&id=${menu.restaurant.id}">Update</a></td>
                <td><a href="vacancy?action=delete&id=${menu.restaurant.id}">Delete</a></td>
            </tr>
        </c:forEach>
    </table>
</section>
</body>
</html>
