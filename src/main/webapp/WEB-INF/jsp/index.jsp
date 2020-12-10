<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<html>
<jsp:include page="fragments/headTag.jsp"/>
<body>
<jsp:include page="fragments/bodyHeader.jsp"/>
<div class="jumbotron">
    <div class="container">
        <form method="post" action="users" class="form-inline">
            <label>Логин</label>
            <select name="userId" class="form-control mx-3">
                <option value="100000" selected>User</option>
                <option value="100001">Admin</option>
            </select>
            <button type="submit" class="btn btn-primary">Выбрать</button>
        </form>
    </div>
</div>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>

<%--<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Java Enterprise (Top)</title>
</head>
<body>
<h3>Проект Java Enterprise (Top)</h3>
<td><a href="users">Users</a></td>
<hr>
<a href="users?action=refresh">Обновить</a>
<hr/>
<form method="post" action="users">
    <b>Select user&nbsp;</b>
    <select name="userId">
        <option value="100000">User</option>
        <option value="100001">Admin</option>
    </select>
    <button type="submit">Select</button>
</form>
</body>
</html>--%>
