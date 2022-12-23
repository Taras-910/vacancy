<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<html>
<jsp:include page="fragments/headTag.jsp"/>
<body>
<nav class="navbar navbar-dark bg-dark py-0">
    <div class="container">
        <div class="navbar-brand"><h3><img src="resources/images/icon-vacancy.png">    4000+ <spring:message code="body_header.title"/> 🔥</h3></div>
        <form class="form-inline my-2" id="login_form" action="spring_security_check" method="post">
            <input class="form-control mr-1" type="text" placeholder="Email" name="username">
            <input class="form-control mr-1" type="password" placeholder="Password" name="password">
            <button class="btn btn-success" type="submit">
                <span class="fa fa-sign-in"></span>
            </button>
        </form>
    </div>
</nav>
<div class="container jumbotron py-0">
        <c:if test="${param.error}">
            <div class="error">${sessionScope["SPRING_SECURITY_LAST_EXCEPTION"].message}</div>
        </c:if>
        <c:if test="${not empty param.message}">
            <div class="message">
                <spring:message code="${param.message}"/>
            </div>
        </c:if>
        <br/>
        <sec:authorize access="isAnonymous()">
            <p>
                <a class="btn btn-lg btn-info" href="profile/register"><spring:message code="login.registration"/></a>
                <button type="submit" class="btn btn-lg btn-outline-primary" onclick="login('user@yandex.ru', 'password')">
                    <spring:message code="login.as_user"/>
                </button>
                <button type="submit" class="btn btn-lg btn-outline-primary" onclick="login('admin@gmail.com', 'admin')">
                    <spring:message code="login.as_admin"/>
                </button>
            </p>
        </sec:authorize>
        <div class="lead py-3"><spring:message code="login.stack"/>
            <a href="http://projects.spring.io/spring-security/">Spring Security</a>,
            <a href="https://docs.spring.io/spring/docs/current/spring-framework-reference/html/mvc.html">Spring MVC</a>,
            <a href="http://projects.spring.io/spring-data-jpa/">Spring Data JPA</a>,
            <a href="http://spring.io/blog/2014/05/07/preview-spring-security-test-method-security">Spring Security-Test</a>,
            <a href="https://docs.spring.io/spring-framework/docs/current/reference/html/integration.html#scheduling-task-scheduler">Spring TaskScheduler</a>,<br>
            <a href="https://jsoup.org">Jsoup HTML Parser</a>,
            <a href="http://hibernate.org/orm/">Hibernate ORM</a>,
            <a href="http://hibernate.org/validator/">Hibernate Validator</a>,
            <a href="http://www.slf4j.org/">SLF4J</a>,
            <a href="https://github.com/FasterXML/jackson">Json Jackson</a>,
            <a href="http://ru.wikipedia.org/wiki/JSP">JSP</a>,
            <a href="http://en.wikipedia.org/wiki/JavaServer_Pages_Standard_Tag_Library">JSTL</a>,
            <a href="http://tomcat.apache.org/">Apache Tomcat</a>,
            <a href="http://www.webjars.org/">WebJars</a>,<br>
            <a href="http://datatables.net/">DataTables plugin</a>,
            <a href="http://ehcache.org">EHCACHE</a>,
            <a href="http://www.postgresql.org/">PostgreSQL</a>,
            <a href="https://hsqldb.org">HSQLDB</a>,
            <a href="https://www.h2database.com">H2DB</a>,
            <a href="http://junit.org/">JUnit</a>,
            <a href="http://hamcrest.org/JavaHamcrest/">Hamcrest</a>
            <a href="http://jquery.com/">jQuery</a>,
            <a href="http://ned.im/noty/">jQuery notification</a>,
            <a href="http://getbootstrap.com/">Bootstrap</a>
        </div>
    </div>
<div class="container lead py-3">
    <a href="https://github.com/JavaOPs/topjava"><spring:message code="login.project"/></a> <spring:message code="login.body"/>
    <a href="https://djinni.co/jobs/keyword-java/">Djinni</a>,
    <a href="https://www.itjobs.ca/en/search-jobs/">ItJobs</a>,
    <%--<a href="https://www.itjobswatch.co.uk/search">ItJobsWatch</a>,--%>
    <a href="https://www.jobbank.gc.ca/jobsearch/jobsearch">JobBank</a>,
    <a href="https://www.jobs.bg/front_job_search.php">JobsBG</a>,
    <a href="https://jobsmarket.io">JobsMarket</a>,
    <a href="https://jobs.dou.ua/first-job/">JobsDou</a>,
    <a href="https://jobsmarket.com.ua/search?position=Java">JobsMarket</a>,
    <a href="https://www.linkedin.com/jobs/search?position=1&pageNum=0">Linkedin</a>,
    <a href="https://nofluffjobs.com/pl/warszawa/backend?lang=uk">NoFluffJobs</a>,
    <%--<a href="https://rabota.ua">Rabota</a>,--%>
    <%--<a href="https://ua.indeed.com">UAIndeed</a>,--%>
    <a href="https://ua.jooble.org/SearchResult">UAJooble</a>,
    <a href="https://www.work.ua/ru/jobs-kyiv-java/">Work</a>,
    <a href="https://www.zaplata.bg/ru/software/firma-organizacia/">Zaplata</a>
</div>
<br>
<div class="container jumbotron py-0">
    <div class="col">
        <a class="btn btn-lg btn-success my-4" href="swagger-ui.html" target="_blank">Swagger REST Api <spring:message code="login.doc"/></a>
    </div>
</div>
<jsp:include page="fragments/footer.jsp"/>
<script type="text/javascript">
    <c:if test="${not empty param.username}">
    setCredentials("${param.username}", "");
    </c:if>
    function login(username, password) {
        setCredentials(username, password);
        $("#login_form").submit();
    }
    function setCredentials(username, password) {
        $('input[name="username"]').val(username);
        $('input[name="password"]').val(password);
    }
</script>
</body>
</html>
