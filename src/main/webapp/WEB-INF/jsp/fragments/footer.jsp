<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--https://getbootstrap.com/docs/4.0/examples/sticky-footer/--%>
<footer class="footer">
    <div class="container">
        <span class="text-muted"><spring:message code="app.footer"/>
            <a href="https://demo.jmix.io/petclinic/#login" target=_blank> PetClinic Application Spring 5/JPA Enterprise</a></span>
    </div>
    <script type="text/javascript">
        const i18n = [];
        <c:forEach var="key" items='<%=new String[]{"common.deleted","common.saved","common.enabled","common.disabled","common.errorStatus", "common.errorType", "common.confirm","common.updated","common.select","common.deselected"}%>'>
        i18n["${key}"] = "<spring:message code="${key}"/>";
        </c:forEach>
    </script>
</footer>
