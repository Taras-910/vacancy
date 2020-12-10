<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://topjava.javawebinar.ru/functions" %>
<html>
<jsp:include page="fragments/headTag.jsp"/>
<body>
<script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/top.common.js" defer></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/top.vacancies.js" defer></script>
<jsp:include page="fragments/bodyHeader.jsp"/>

<div class="jumbotron pt-4">
    <div class="container">
        <h3 class="text-center">Вакансии</h3>


        <div class="card border-dark">
            <div class="card-body pb-0">
                <form id="filter">
                    <div class="row">
                        <div class="col-5">
                            <label for="language">Язык программирования:</label>
                            <input class="form-control" type="text" value="Java" name="language" id="language">
                        </div>
                        <div class="offset-1 col-1">
                        </div>
                        <div class="col-5">
                            <label for="residence">Место расположения вакансии (город или страна):</label>
                            <input class="form-control" type="text" value="за_рубежем" name="residence" id="residence">
                        </div>
                    </div>
                </form>
            </div>
            <div class="card-footer text-right">
                <button class="btn btn-danger" onclick="clearFilter()">
                    <span class="fa fa-remove"></span>
                    Сброс фильтра
                </button>
                <button class="btn btn-primary" onclick="updateFilteredTable()">
                    <span class="fa fa-filter"></span>
                    Поиск
                </button>
            </div>
        </div>
        <br>

        <button class="btn btn-primary" onclick="add()">
            <span class="fa fa-plus"></span>
            Добавить
        </button>

        <table class="table table-striped" id="datatable">
            <thead>
            <tr>
                <th hidden>id</th>
                <th>Вакансия</th>
                <th>Компания</th>
                <th>Адрес</th>
                <th>От $</th>
                <th>До $</th>
                <th>Требования</th>
                <th>Дата</th>
                <th></th>
                <th></th>
                <th></th>
            </tr>
            </thead>
            <c:forEach items="${vacancies}" var="vacancy">
                <jsp:useBean id="vacancy" type="ua.training.top.to.VacancyTo"/>
                <tr vacancy-toVote="${vacancy.toVote}">
                    <td hidden>${vacancy.id}</td>
                    <td><a href="${vacancy.link}">${vacancy.title}</a></td>
                    <td><c:out value="${vacancy.employerName}"/></td>
                    <td><c:out value="${vacancy.address}"/></td>
                    <td><c:out value="${vacancy.salaryMin}"/></td>
                    <td><c:out value="${vacancy.salaryMax}"/></td>
                    <td><c:out value="${vacancy.skills}"/></td>
                    <td><c:out value="${fn:formatDateTime(vacancy.localDate)}"/></td>
                    <td><input type="checkbox" <c:if test="${vacancy.toVote}">checked</c:if> onclick="vote($(this), ${vacancy.id})"/></td>
                    <td><a><span class="fa fa-pencil"></span></a></td>
                    <td><a onclick="deleteRow(${vacancy.id})"><span class="fa fa-remove"></span></a></td>
                </tr>
            </c:forEach>
        </table>
        <br>
    </div>
</div>

<div class="modal fade" tabindex="-1" id="editRow">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title">Добавить</h4>
                <button type="button" class="close" data-dismiss="modal" onclick="closeNoty()">&times;</button>
            </div>
            <div class="modal-body">
                <form id="detailsForm">
                    <input type="hidden" id="id" name="id">
                    <div class="form-group">
                        <label for="title" class="col-form-label">Название вакансии</label>
                        <input type="text" class="form-control" id="title" name="title"
                               placeholder="${Название}">
                    </div>
                    <%--<div class="form-group">
                        <label for="salaryMin" class="col-form-label">з/п мин (usв cent)</label>
                        <input type="number" class="form-control" id="salaryMin" name="salaryMin"
                               value="1">
                    </div>
                    <div class="form-group">
                        <label for="salaryMax" class="col-form-label">з/п макс (usв cent)</label>
                        <input type="number" class="form-control" id="salaryMax" name="salaryMax"
                               placeholder="1">
                    </div>
                    <div class="form-group">
                        <label for="link" class="col-form-label"></label>
                        <input type="text" class="form-control" id="link" name="link"
                               placeholder="https://www.example.com">
                    </div>
                    <div class="form-group">
                        <label for="skills" class="col-form-label">Требования</label>
                        <input type="text" class="form-control" id="skills" name="skills"
                               placeholder="Требуемые знания, навыки, ...">
                    </div>
                    <div class="form-group">
                        <label for="localDate" class="col-form-label">Дата публикации</label>
                        <input type="datetime-local" class="form-control" id="localDate" name="localDate"
                               placeholder="Дата">
                    </div>
                    <div class="form-group">
                        <label for="lang" class="col-form-label">Язык программирования</label>
                        <input type="text" class="form-control" id="lang" name="lang"
                               placeholder="java">
                    </div>
                    <div class="form-group">
                        <label for="workplace" class="col-form-label">Город или страна</label>
                        <input type="text" class="form-control" id="workplace" name="workplace"
                               placeholder="за рубежем">
                    </div>
                    <div class="form-group">
                        <label for="employerName" class="col-form-label">Работодатель</label>
                        <input type="text" class="form-control" id="employerName" name="employerName"
                               placeholder="Название">
                    </div>--%>

                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-dismiss="modal" onclick="closeNoty()">
                    <span class="fa fa-close"></span>
                    Отменить
                </button>
                <button type="button" class="btn btn-primary" onclick="save()">
                    <span class="fa fa-check"></span>
                    Сохранить
                </button>
            </div>
        </div>
    </div>
</div>

<jsp:include page="fragments/footer.jsp"/>
</body>
</html>
