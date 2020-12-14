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
                        <div class="offset-1"></div>
                        <div class="col-4 text-left">
                            <label for="language">Язык: java, php, python,...</label>
                            <input class="form-control" type="text" placeholder="java" name="language" id="language">
                        </div>
                        <div class="offset-1 col-1">
                        </div>
                        <div class="col-4 text-left">
                            <label for="workplace">Местонахождение: за_рубежем, Киев,...</label>
                            <input class="form-control" type="text" placeholder="за_рубежем" name="workplace" id="workplace">
                        </div>
                    <div class="offset-1 col-1">
                        </div>
                    </div>
                    <br/>
                    <div class="row">
                        <a class="offset-3 col-3">
                        </a>
                        <button class="col-2 btn btn-danger text-right" onclick="clearFilter()">
                            <span class="fa fa-remove"></span>
                            Сбросить
                        </button>
                        <a class="col-1">
                        </a>
                        <button class="col-2 btn btn-primary text-right" onclick="updateFilteredTable()">
                            <span class="fa fa-filter"></span>
                            Фильтровать
                        </button>
                    </div>
                </form>
            </div>
        </div>
        <br>
        <div class="row card-footer">
            <button class="col-2 btn btn-danger" onclick="add()">
            <span class="fa fa-plus text-left"></span>
                    Добавить
                </button>
            <a class="offset-4 col-3">
            </a>
            <button class="col-2 btn btn-info text-left" onclick="refreshDB()">
                <span class="fa fa-refresh text-right"></span>
                Обновить DB
            </button>
        </div>
        <table class="table table-striped" id="datatable">
            <thead>
            <tr>
                <th hidden>id</th>
                <th hidden>link</th>
                <th>Вакансия</th>
                <th>Компания</th>
                <th>Локализация</th>
                <th>От $</th>
                <th>До $</th>
                <th>Требования</th>
                <th>Датировано</th>
                <th></th>
                <th></th>
                <th></th>
            </tr>
            </thead>
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
                               placeholder="Название">
                    </div>
                    <div class="form-group">
                        <label for="employerName" class="col-form-label">Работодатель</label>
                        <input type="text" class="form-control" id="employerName" name="employerName"
                               placeholder="Название">
                    </div>
                    <div class="form-group">
                        <label for="address" class="col-form-label">Локализация</label>
                        <input type="text" class="form-control" id="address" name="address"
                               placeholder="Киев">
                    </div>
                    <div class="form-group">
                        <label for="salaryMin" class="col-form-label">з/п от (usd cent)</label>
                        <input type="number" class="form-control" id="salaryMin" name="salaryMin"
                               placeholder="1">
                    </div>
                    <div class="form-group">
                        <label for="salaryMax" class="col-form-label">з/п до (usd cent)</label>
                        <input type="number" class="form-control" id="salaryMax" name="salaryMax"
                               placeholder="1">
                    </div>
                    <div class="form-group">
                        <label for="url" class="col-form-label"></label>
                        <input type="text" class="form-control" id="url" name="url"
                               placeholder="https://www.example.com">
                    </div>
                    <div class="form-group">
                        <label for="skills" class="col-form-label">Требования</label>
                        <input type="text" class="form-control" id="skills" name="skills"
                               placeholder="Требуемые знания, навыки, ...">
                    </div>
                    <div class="form-group">
                        <label for="language" class="col-form-label">Язык программирования</label>
                        <input type="text" class="form-control" id="languageCode" name="language"
                               placeholder="java" aria-valuenow="java">
                    </div>
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

<div class="modal fade" tabindex="-1" id="refreshRow">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">DB обновится по параметрам:</h5>
                <button type="button" class="close" data-dismiss="modal" onclick="closeNoty()">&times;</button>
            </div>
            <div class="modal-body">
                <form id="detailsRefreshForm">
                    <div class="form-group">
                        <label for="languageTask" class="col-form-label">язык (java, php, python, ...)</label>
                        <input type="text" class="form-control" id="languageTask" name="languageTask"
                               placeholder="Java">
                    </div>
                    <div class="form-group">
                        <label for="workplaceTask" class="col-form-label">местонахождение (за_рубежем, Киев, Санкт-Петербург...)</label>
                        <input type="text" class="form-control" id="workplaceTask" name="workplaceTask"
                               value="за_рубежем">
                    </div>
                </form>
            </div>
            <h7 class="modal-title"></h7>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-dismiss="modal" onclick="closeNoty()">
                    <span class="fa fa-close"></span>
                    Отменить
                </button>
                <button type="button" class="btn btn-primary" onclick="sendRefresh()">
                    <span class="fa fa-check"></span>
                    Отправить
                </button>
            </div>
        </div>
    </div>
</div>

<jsp:include page="fragments/footer.jsp"/>
</body>
</html>
