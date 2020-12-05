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
    <form method="post" action="/vacancy/vacancies">
        <table border="1" cellpadding="8" cellspacing="0">
            <thead>
            <tr>
                <th hidden>VacancyId</th>
                <th>Вакансия</th>
                <th>Компания</th>
                <th>Адрес</th>
                <th>usd cent from</th>
                <th>usd cent to</th>
                <th>Требования</th>
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
                    <td>${vacancy.employerName}</td>
                    <td>${vacancy.address}</td>
                    <td>${vacancy.salaryMin}</td>
                    <td>${vacancy.salaryMax}</td>
                    <td>${vacancy.skills}</td>
                    <td>${fn:formatDateTime(vacancy.localDate)}</td>

                    <td>
                    <%--td><input <type="checkbox" checked="checked" value="<c:out value='${vacancy.vacancyId}' />"/></td>--%>

                    <input type="checkbox" name="toVote" value="${vacancy.vacancyId}" id="${vacancy.vacancyId}">
                    </td>
                </tr>
            </c:forEach>
        </table>
        <input type="submit" value="Submit">
    </form>

    </form>
</section>
</body>
</html>
<%--<form action="modifyAcl" method="post">
<label><input type='checkbox' name="fullControl" value='FullControl' checked='checked'/>Full Control</label>
...all your other checkboxes...

<input type="submit" value="Modify ACL" />
</form>
--%>
