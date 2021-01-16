<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<nav class="navbar navbar-dark bg-dark py-0">
    <div class="container">
        <a href="vacancies" class="navbar-brand"><h3><img src="resources/images/icon-vacancy.png">  Вакансии</h3></a>
        <sec:authorize access="isAuthenticated()">
            <form class="form-inline my-2">
                <sec:authorize access="hasRole('ADMIN')">
                    <a class="btn btn-info mr-1" href="users">Пользователи</a>
                </sec:authorize>
                <a class="btn btn-info mr-1" href="profile">профиль ${user.name}</a>
                <a class="btn btn-primary my-1" href="logout">
                    <span class="fa fa-sign-out"></span>
                </a>
            </form>
        </sec:authorize>
        <sec:authorize access="isAnonymous()">
            <form class="form-inline my-2" id="login_form" action="spring_security_check" method="post">
             <sec:authorize access="isAuthenticated()">
                <input class="form-control mr-1" type="text" placeholder="Email" name="username">
                <input class="form-control mr-1" type="password" placeholder="Password" name="password">
                <button class="btn btn-success" type="submit">
                    <span class="fa fa-sign-in"></span>
                </button>
             </sec:authorize>
            </form>
        </sec:authorize>
    </div>
</nav>
