<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://topjava.javawebinar.ru/functions" %>

<html>
<head>
    <title>Vacancies</title>
    <link rel="stylesheet" href="resources/css/style.css">
</head>
<body>
<section>
    <h3><a href="/vacancy">Home</a></h3>
    <hr/>
    <h2>Вакансии</h2>
    <hr/>
    <%--<a href="vacancy/create">Add</a>--%>
    <br><br>
    <table border="1" cellpadding="8" cellspacing="0">
        <thead>
        <tr>
            <th hidden>VacancyId</th>
            <th>Вакансия</th>
            <th>Требования</th>
            <th>Компания</th>
            <th>Адрес</th>
            <th>Зарплата</th>
            <th>Дата публикации</th>
            <th></th>
        </tr>
        </thead>
        <jsp:useBean id="vacancies" scope="request" type="java.util.List"/>
        <c:forEach items="${vacancies}" var="vacancy">
            <jsp:useBean id="vacancy" type="ua.training.top.to.VacancyTo"/>
            <tr data-toVote="${vacancy.toVote}">
                <td hidden>${vacancy.vacancyId}</td>
                <td><a href="${vacancy.link}">${vacancy.title}</a></td>
                <td>${vacancy.skills}</td>
                <td>${vacancy.employerName}</td>
                <td>${vacancy.address}</td>
                <td>${vacancy.salary}</td>
                <td>${fn:formatDateTime(vacancy.localDate)}</td>
                <td><input type="checkbox" <c:if test="${vacancy.toVote}">checked</c:if>/></td>
            </tr>
        </c:forEach>
    </table>
</section>
</body>
</html>
