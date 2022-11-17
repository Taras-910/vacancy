<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="vacancy" tagdir="/WEB-INF/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<jsp:include page="fragments/headTag.jsp"/>
<body>
<jsp:include page="fragments/bodyHeader.jsp"/>
<div class="jumbotron pt-4">
    <div class="container">
        <div class="row">
            <div class="col-5 offset-3">
                <c:choose>
                    <c:when test="${register}">
                        <h3><p><spring:message code="profile.register"/></p></h3>
                    </c:when>
                    <c:otherwise>
                        <h3><p><spring:message code="profile.profile"/> ${user.name}</p></h3>
                    </c:otherwise>
                </c:choose>
                <form:form class="form-group" modelAttribute="user" method="post" action="${register ? 'profile/register' : 'profile'}"
                           charset="utf-8" accept-charset="UTF-8">
                    <vacancy:inputField labelCode="name" name="name"/>
                    <vacancy:inputField labelCode="email" name="email"/>
                    <vacancy:inputField labelCode="password" name="password" inputType="password"/>
                    <div class="text-right">
                        <a class="btn btn-secondary" href="#" onclick="window.history.back()">
                            <span class="fa fa-close"></span>
                            <spring:message code="common.back"/>
                        </a>
                        <button type="submit" class="btn btn-primary">
                            <span class="fa fa-check"></span>
                            <spring:message code="common.save"/>
                        </button>
                    </div>
                </form:form>
            </div>
        </div>
    </div>
</div>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>
