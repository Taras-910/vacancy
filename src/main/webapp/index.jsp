<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Java Enterprise (Top)</title>
</head>
<body>
<h3>Проект Java Enterprise (Top)</h3>
<td><a href="users">Users</a></td>
<hr>
<form method="post" action="users">
    <b>Select user&nbsp;</b>
    <select name="userId">
        <option value="100000">User</option>
        <option value="100001">Admin</option>
    </select>
    <button type="submit">Select</button>
</form>
</body>
</html>
