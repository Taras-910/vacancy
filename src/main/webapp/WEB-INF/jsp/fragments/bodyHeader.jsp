<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<nav class="navbar navbar-dark bg-dark py-0">
    <div class="container">
      <a href="vacancies" class="navbar-brand"><h3><img src="resources/images/icon-vacancy.png">  Вакансии</h3></a>
      <form class="form-inline my-2">
            <a class="btn btn-info mr-1" href="users">Пользователи</a>
            <a class="btn btn-primary" href="logout">
                <span class="fa fa-sign-out"></span>
            </a>
        </form>
    </div>
</nav>
