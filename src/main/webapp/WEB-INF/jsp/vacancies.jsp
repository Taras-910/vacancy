<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<html>
<jsp:include page="fragments/headTag.jsp"/>
<body>
<script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/top.common.js" defer></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/top.vacancies.js" defer></script>
<%--<script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/top.vacancyHelper.js" defer></script>--%>
<jsp:include page="fragments/bodyHeader.jsp"/>
<div class="jumbotron pt-4">
    <div class="container-fluid row-cols-lg-10" >
        <h3 class="text-center text-secondary">Вакансии</h3>
        <div class="card border-dark">
            <div class="card-body pb-0">
                <form id="filter">
                    <div class="row">
                        <div class="offset-1"></div>
                        <div class="col-4 text-left">
                            <label for="language"><h7 class="text-center btn-outline-info"></h7></label>
                            <input class="form-control" type="text"
                                   placeholder="Java, PHP, Ruby, Python, JavaScript, Kotlin... "
                                   name="language" id="language">
                        </div>
                        <div class="offset-1 col-1">
                        </div>
                        <div class="col-4 text-left">
                            <label for="workplace"><h7 class="text-center btn-outline-info"></h7></label>
                            <input class="form-control" type="text"
                                   placeholder="За_рубежем, Киев, Днепр, Харьков, Санкт-Петербург... "
                                   name="workplace" id="workplace">
                        </div>
                        <div class="offset-1 col-1">
                        </div>
                    </div>
                    <br/>
                    <div class="row">
                        <a class="offset-3 col-3">
                        </a>
                        <button class="col-2 btn btn-outline-info text-right" onclick="clearFilter()">
                            <span class="fa fa-remove"></span>
                            Сбросить
                        </button>
                        <a class="col-1">
                        </a>
                        <button class="col-2 btn btn-outline-info text-right" onclick="updateFilteredTable()">
                            <span class="fa fa-filter"></span>
                            Фильтровать
                        </button>
                    </div>
                </form>
            </div>
        </div>
        <br>
        <div class="row card-footer">
            <sec:authorize access="hasRole('ADMIN')">
                <button class="col-2 btn btn-primary" onclick="addVacancy()">
                    <span class="fa fa-plus text-left"></span>
                    Добавить
                </button>
            </sec:authorize>
            <a class="offset-4 col-3">
            </a>
            <button class="col-2 btn btn-outline-primary bs-popover-right" onclick="refreshDB()">
                <span class="fa fa-refresh text-left pull-right"></span>
                Обновить DB
            </button>
        </div>
        <table class="table table-striped table-bordered" id="datatable">
            <div class="row">
                <thead>
                <tr>
                    <th hidden>id</th>
                    <th hidden>link</th>
                    <th class="col-auto">Вакансия</th>
                    <th class="col-auto">Компания</th>

                    <th class="col-auto">Город</th>
                    <th class="col-auto">От $</th>
                    <th class="col-auto">До $</th>
                    <th class="col" style="text-align: center;">Требования</th>

                    <th class="col-auto">Дата</th>
                    <th hidden>siteName</th>
                    <th hidden>toVote</th>
                    <th hidden>work place</th>

                    <th hidden>language</th>
                    <th ></th>
                    <th ></th>
                    <th ></th>
                </tr>
                </thead>
            </div>
        </table>
        <br>
    </div>
</div>
<sec:authorize access="hasRole('ADMIN')">
    <%--add VacancyTo--%>
    <div class="modal fade" tabindex="-1" id="addRow">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h4 class="modal-title">Добавить</h4>
                    <button type="button" class="close" data-dismiss="modal" onclick="closeNoty()">&times;</button>
                </div>
                <div class="modal-body">
                    <form id="detailsForm">
                        <input type="hidden" id="id" name="id">
                        <div class="form-group not_update_visible">
                            <label for="title" class="col-form-label">Название вакансии</label>
                            <input type="text" class="form-control" id="title" name="title">
                        </div>
                        <div class="form-group not_update_visible">
                            <label for="employerName" class="col-form-label">Работодатель</label>
                            <input type="text" class="form-control" id="employerName" name="employerName">
                        </div>
                        <div class="form-group not_update_visible">
                            <label for="address" class="col-form-label">Адрес</label>
                            <input type="text" class="form-control" id="address" name="address">
                        </div>
                        <div class="form-group">
                            <label for="salaryMin" class="col-form-label">з/п от (usd cent)</label>
                            <input type="number" class="form-control" id="salaryMin" name="salaryMin">
                        </div>
                        <div class="form-group">
                            <label for="salaryMax" class="col-form-label">з/п до (usd cent)</label>
                            <input type="number" class="form-control" id="salaryMax" name="salaryMax">
                        </div>
                        <div class="form-group">
                            <label for="url" class="col-form-label">Ссылка</label>
                            <input type="text" class="form-control" id="url" name="url"
                                   placeholder="https://www.example.com">
                        </div>
                        <div class="form-group">
                            <label for="skills" class="col-form-label">Требуемые знания, навыки, ...</label>
                            <input type="text" class="form-control" id="skills" name="skills">
                        </div>
                        <div class="form-group">
                            <input type="hidden" class="form-control" id="releaseDate" name="releaseDate">
                        </div>
                        <div class="form-group not_update_visible">
                            <input type="hidden" class="form-control" id="siteName" name="siteName">
                        </div>
                        <div class="form-group">
                            <label for="language" class="col-form-label">Язык программирования</label>
                            <input type="text" class="form-control" id="languageCode" name="language">
                        </div>
                        <div class="form-group">
                            <input type="hidden" class="form-control" id="workplace1" name="workplace">
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
    <%--update RowVacancy--%>
    <div class="modal fade" tabindex="-1" id="updateRow">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h4 class="modal-title">Редактировать</h4>
                    <button type="button" class="close" data-dismiss="modal" onclick="closeNoty()">&times;</button>
                </div>
                <div class="modal-body">
                    <form id="detailsUpdateForm">
                        <input type="hidden" id="idUpdate" name="id">
                        <div class="form-group not_update_visible">
                            <label for="title" class="col-form-label">Вакансия</label>
                            <input type="text" class="form-control" id="titleUpdate" name="title">
                        </div>
                        <div class="form-group not_update_visible">
                            <input type="hidden" class="form-control" id="employerNameUpdate" name="employerName">
                        </div>
                        <div class="form-group not_update_visible">
                            <input type="hidden" class="form-control" id="addressUpdate" name="address">
                        </div>
                        <div class="form-group">
                            <label for="salaryMin" class="col-form-label">з/п от (usd cent)</label>
                            <input type="number" class="form-control" id="salaryMinUpdate" name="salaryMin">
                        </div>
                        <div class="form-group">
                            <label for="salaryMax" class="col-form-label">з/п до (usd cent)</label>
                            <input type="number" class="form-control" id="salaryMaxUpdate" name="salaryMax">
                        </div>
                        <div class="form-group">
                            <label for="url" class="col-form-label"></label>
                            <input type="text" class="form-control" id="urlUpdate" name="url"
                                   placeholder="https://www.example.com">
                        </div>
                        <div class="form-group">
                            <label for="skills" class="col-form-label">Требования</label>
                            <input type="text" class="form-control" id="skillsUpdate" name="skills">
                        </div>
                        <div class="form-group">
                            <input type="hidden" class="form-control" id="releaseDateUpdate" name="releaseDate">
                        </div>
                        <div class="form-group">
                            <input type="hidden" class="form-control" id="siteNameUpdate" name="siteName">
                        </div>
                        <div class="form-group">
                            <input type="hidden" class="form-control" id="languageCodeUpdate" name="language">
                        </div>
                        <div class="form-group">
                            <input type="hidden" class="form-control" id="workplaceUpdate" name="workplace">
                        </div>
                    </form>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-dismiss="modal" onclick="closeNoty()">
                        <span class="fa fa-close"></span>
                        Отменить
                    </button>
                    <button type="button" class="btn btn-primary" onclick="updateVacancyTo()">
                        <span class="fa fa-check"></span>
                        Сохранить
                    </button>
                </div>
            </div>
        </div>
    </div>
    <%--delete RowVacancy--%>
    <div class="modal fade" tabindex="-1" id="deleteRow">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header box" style="text-align: center;">
                    <h6><em>Подтвердите ваше действие !</em></h6>
                </div>
                <div class="modal-body">
                    <form id="detailsDeleteForm">
                        <label for="idDelete" ><em>'Are you sure?'</em></label>
                        <input type="hidden" id="idDelete" name="id">
                    </form>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-dismiss="modal" onclick="closeNoty()">
                        <span class="fa fa-close"></span>
                        Отменить
                    </button>
                    <button type="button" class="btn btn-primary" onclick="deleteVacancyTo()">
                        <span class="fa fa-check"></span>
                        Подтвердить
                    </button>
                </div>
            </div>
        </div>
    </div>
</sec:authorize>
<%--refresh--%>
<div class="modal fade" tabindex="-1" id="refreshRow">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">Обновить DB по параметрам:</h5>
                <button type="button" class="close" data-dismiss="modal" onclick="closeNoty()">&times;</button>
            </div>
            <div class="modal-body">
                <form id="detailsRefreshForm">
                    <div class="form-group">
                        <label type="hidden" for="recordedDate" class="col-form-label"></label>
                        <input type="hidden" class="form-control" id="recordedDate" name="recordedDate">
                    </div>
                    <div class="form-group">
                        <label for="languageTask" class="col-form-label">
                            <em>Java, PHP, Ruby, Python, JavaScript, Kotlin...</em>
                        </label>
                        <input type="text" class="form-control" id="languageTask" name="language">
                    </div>
                    <div class="form-group">
                        <label for="workplaceTask" class="col-form-label">
                            <em>Киев, Днепр, Харьков, За_рубежем, Санкт-Петербург...</em>
                        </label>
                        <input type="text" class="form-control" id="workplaceTask" name="workplace">
                    </div>
                    <div class="form-group">
                        <label type="hidden" for="userId" class="col-form-label"></label>
                        <input type="hidden" class="form-control" id="userId" name="userId">
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
                    Обновить
                </button>
            </div>
        </div>
    </div>
</div>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>
