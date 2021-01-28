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
<script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/top.freshen.js" defer></script>
<jsp:include page="fragments/bodyHeader.jsp"/>
<div class="jumbotron pt-4">
    <div class="container-fluid row-cols-lg-10" >
        <p class="text-info"><h3 class="text-center">Вакансии</h3></p>
        <div class="card border-dark">
            <div class="card-body pb-0">
                <div class="container rounded-lg" style="width: 90%">
                    <div class="row justify-content-md-between align-items-center">
                        <form class="col-8 form-row needs-validation" id="filter">
                            <div class="col-md-4 mb-3">
                                <label for="language"><h7 class="btn-outline-info"><em>Java, Php, Ruby...</em></h7></label>
                                <input class="form-control" type="text" name="language" id="language" list="language_name">
                                <datalist id="language_name">
                                    <option value='all' selected>all</option>
                                    <option value='Java'>Java</option>
                                    <option value='Php'>Php</option>
                                    <option value='Ruby'>Ruby</option>
                                    <option value='JavaScript'>JavaScript</option>
                                    <option value='TypeScript'>TypeScript</option>
                                    <option value='Kotlin'>Kotlin</option>
                                    <option value='Python'>Python</option>
                                    <option value='C#'>C#</option>
                                    <option value='C++'>C++</option>
                                </datalist>
                            </div>
                            <div class="col-md-8 mb-3">
                                <label for="workplace"><h7 class="btn-outline-info"><em>Киев, Днепр, За_рубежем...</em></h7></label>
                                <input class="form-control" type="text" name="workplace" id="workplace" list="city_name">
                                <datalist id="city_name">
                                    <option value='all' selected>all</option>
                                    <option value='Киев'>Киев</option>
                                    <option value='Днепр'>Днепр</option>
                                    <option value='Львов'>Львов</option>
                                    <option value='Харьков'>Харьков</option>
                                    <option value='За_рубежем'>За_рубежем</option>
                                    <option value='Санкт-Петербург'>Санкт-Петербург</option>
                                </datalist>
                            </div>
                        </form>
                        <div class="col-2">
                            <button class="btn-sm btn-outline-info" onclick="updateFilteredTable()">
                                <span class="fa fa-filter"></span>
                                Фильтровать
                            </button>
                        </div>
                        <div class="col-md-2 ml-md-auto">
                            <label><h7 class="invisible">Сброс<br></h7></label>
                            <button class="btn-sm btn-outline-danger" onclick="clearFilter()">
                                <span class="fa fa-remove"></span>
                                Сбросить
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="card-body pb-0">
            <div class="row card-footer justify-content-between" style="width: 103%">
                <div class="col">
                <sec:authorize access="hasRole('ADMIN')">
                    <button class="col-md-2 btn btn-primary" onclick="add()">
                        <span class="fa fa-plus text-left"></span>
                        Добавить
                    </button>
                </sec:authorize>
                </div>
                <button class="offset-8 col-md-2 btn btn-outline-info bs-popover-right" onclick="refreshDB()">
                    <span class="fa fa-refresh text-left pull-right"></span>
                    Обновить DB
                </button>
            </div>
        </div>
        <table class="table table-striped table-bordered" id="datatable" style="width: 100%">
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
                    <th class="col-auto text-nowrap">Дата</th>
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
                            <label for="languageCode" class="col-form-label">Язык программирования</label>
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
                <div class="modal-header box">
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
                        Удалить
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
            <div class="modal-header box">
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
                        <label for="languageTask"><h7 class="btn-outline-info"><em>Java, Php, Ruby...</em></h7></label>
                        <input class="form-control" type="text" name="language" id="languageTask" list="language_name_2">
                        <datalist id="language_name_2">
                            <option value='Java'>Java</option>
                            <option value='Php'>Php</option>
                            <option value='Ruby'>Ruby</option>
                            <option value='JavaScript'>JavaScript</option>
                            <option value='TypeScript'>TypeScript</option>
                            <option value='Kotlin'>Kotlin</option>
                            <option value='Python'>Python</option>
                            <option value='C#'>C#</option>
                            <option value='C++'>C++</option>
                        </datalist>
                    </div>
                    <div class="form-group">
                        <label for="workplaceTask"><h7 class="btn-outline-info"><em>Киев, Днепр, За_рубежем...</em></h7></label>
                        <input class="form-control" type="text" name="workplace" id="workplaceTask" list="city_name_2">
                        <datalist id="city_name_2">
                            <option value='Киев'>Киев</option>
                            <option value='Днепр'>Днепр</option>
                            <option value='Львов'>Львов</option>
                            <option value='Харьков'>Харьков</option>
                            <option value='За_рубежем'>За_рубежем</option>
                            <option value='Санкт-Петербург'>Санкт-Петербург</option>
                        </datalist>
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
