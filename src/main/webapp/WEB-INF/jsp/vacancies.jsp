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
<div class="jumbotron pt-md-">
    <div class="container-fluid row-cols-md-10">
        <div class="card border-dark">
            <div class="card-body pb-1" style="width:100%; background-color: #e5e7e7">
                <div class="container rounded-lg" style="width:90%;">
                    <div class="row justify-content-md-between align-items-center align-items-baseline">
                        <form class="col-sm-8 form-row needs-validation" id="filter">
                            <div class="col-md-4 mb-3 col-form-label">
                                <input class="form-control" type="text" name="language" id="language"
                                       list="language_name" style="width:98%;border:2px solid #0397ba"
                                       placeholder="please enter language...">
                                <datalist id="language_name">
                                    <option value='all' selected>all</option>
                                    <option value='Java'>Java</option>
                                    <option value='Ruby'>Ruby</option>
                                    <option value='Kotlin'>Kotlin</option>
                                    <option value='Python'>Python</option>
                                    <option value='JavaScript'>JavaScript</option>
                                    <option value='TypeScript'>TypeScript</option>
                                    <option value='Php'>Php</option>
                                    <option value='React'>React</option>
                                    <option value='Angular'>Angular</option>
                                    <option value='Android'>Android</option>
                                    <option value='C++'>C++</option>
                                    <option value='C#'>C#</option>
                                    <option value='.Net'>.Net</option>
                                    <option value='Ios'>Ios</option>
                                    <option value='Node.js'>Node.js</option>
                                    <option value='Sql'>Sql</option>
                                    <option value='Goland'>Goland</option>
                                    <option value='Ruby on rails'>Ruby on rails</option>
                                    <option value='Scala'>Scala</option>
                                    <option value='Asw'>Asw</option>
                                    <option value='Azure'>Azure</option>
                                    <option value='Html&css'>Html&css</option>
                                    <option value='Html'>Html</option>
                                    <option value='css'>css</option>
                                    <option value='Spark'>Spark</option>
                                    <option value='React native'>React native</option>
                                    <option value='Vue.js'>Vue.js</option>
                                    <option value='Flutter'>Flutter</option>
                                    <option value='Elixir'>Elixir</option>
                                    <option value='Hadoop'>Hadoop</option>
                                </datalist>
                            </div>
                            <div class="col-md-4 mb-3 col-form-label">
                                <input class="form-control" type="text" name="level" id="level" list="level_name"
                                       style="width:101%;border:2px solid #0397ba"
                                       placeholder="level...">
                                <datalist id="level_name">
                                    <option value='all' selected>all</option>
                                    <option value='Trainee'>Trainee</option>
                                    <option value='Junior'>Junior</option>
                                    <option value='Middle'>Middle</option>
                                    <option value='Senior'>Senior</option>
                                    <option value='Expert'>Expert</option>
                                    <option value='all'>all</option>
                                </datalist>
                            </div>
                            <div class="col-md-4 mb-3 col-form-label">
                                <input class="form-control" type="text" name="workplace" id="workplace" list="city_name"
                                       style="width:101%;border:2px solid #0397ba"
                                       placeholder="workplace...">
                                <datalist id="city_name">
                                    <option value='all' selected>all</option>
                                    <option value='Киев'>Kyiv</option>
                                    <option value='Минск'>Minsk</option>
                                    <option value='Львов'>Lviv</option>
                                    <option value='Харьков'>Kharkiv</option>
                                    <option value='Днепр'>Dnipro</option>
                                    <option value='Украина'>Ukraine</option>
                                    <option value='Канада'>Canada</option>
                                    <option value='Польша'>Poland</option>
                                    <option value='remote'>remote</option>
                                    <option value='foreign'>foreign</option>
                                </datalist>
                            </div>
                        </form>
                        <div class="col-sm-2">
                            <button class="btn-sm btn-outline-info" onclick="updateFilteredTable()">
                                <span class="fa fa-filter"></span>
                                Filter
                            </button>
                        </div>
                        <div class="col-md-2 ml-md-auto">
                            <button class="btn-sm btn-outline-danger" onclick="clearFilter()">
                                <span class="fa fa-remove"></span>
                                Reset
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
                    <button class="col-md-2 btn btn-primary mt-2" onclick="add()">
                        <span class="fa fa-plus text-left"></span>
                         new vacancy
                    </button>
                </sec:authorize>
                </div>
                <button class=" col-md-2 btn btn-info bs-popover-right mt-2" onclick="refreshDB()">
                    <span class="fa fa-refresh text-left pull-right"></span>
                    Refresh DB
                </button>
            </div>
        </div>
        <table class="table table-striped table-bordered" id="datatable" style="width: 100%">
            <div class="row">
                <thead>
                <tr>
                    <th hidden>id</th>
                    <th hidden>link</th>
                    <th class="col-auto">Vacancy</th>
                    <th class="col-auto">Employer</th>
                    <th class="col-auto">Workplace</th>
                    <th class="col-auto"><h7>from</h7></th>
                    <th class="col-auto"><h7>to $</h7></th>
                    <th class="col" style="text-align: center;">Skills</th>
                    <th class="col-auto text-nowrap">Date</th>
                    <th class="toVote"></th>
                    <th hidden>work place</th>
                    <th hidden>language</th>
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
                    <h4 class="modal-title">Add</h4>
                    <button type="button" class="close" data-dismiss="modal" onclick="closeNoty()">&times;</button>
                </div>
                <div class="modal-body">
                    <form id="detailsForm">
                        <input type="hidden" id="id" name="id">
                        <div class="form-group not_update_visible">
                            <label for="title" class="col-form-label">Vacancy name</label>
                            <input type="text" class="form-control" id="title" name="title">
                        </div>
                        <div class="form-group not_update_visible">
                            <label for="employerName" class="col-form-label">Employer</label>
                            <input type="text" class="form-control" id="employerName" name="employerName">
                        </div>
                        <div class="form-group not_update_visible">
                            <label for="address" class="col-form-label">Address</label>
                            <input type="text" class="form-control" id="address" name="address">
                        </div>
                        <div class="form-group">
                            <label for="salaryMin" class="col-form-label">from (usd cent)</label>
                            <input type="number" class="form-control" id="salaryMin" name="salaryMin">
                        </div>
                        <div class="form-group">
                            <label for="salaryMax" class="col-form-label">to (usd cent)</label>
                            <input type="number" class="form-control" id="salaryMax" name="salaryMax">
                        </div>
                        <div class="form-group">
                            <label for="url" class="col-form-label">url</label>
                            <input type="text" class="form-control" id="url" name="url"
                                   placeholder="https://www.example.com">
                        </div>
                        <div class="form-group">
                            <label for="skills" class="col-form-label">Skills, навыки, ...</label>
                            <input type="text" class="form-control" id="skills" name="skills">
                        </div>
                        <div class="form-group">
                            <input type="hidden" class="form-control" id="releaseDate" name="releaseDate">
                        </div>
                        <div class="form-group">
                            <label for="languageCode" class="col-form-label">Language</label>
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
                        back
                    </button>
                    <button type="button" class="btn btn-primary" onclick="save()">
                        <span class="fa fa-check"></span>
                        save
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
                            <label for="title" class="col-form-label">Vacancy</label>
                            <input type="text" class="form-control" id="titleUpdate" name="title">
                        </div>
                        <div class="form-group not_update_visible">
                            <input type="hidden" class="form-control" id="employerNameUpdate" name="employerName">
                        </div>
                        <div class="form-group not_update_visible">
                            <input type="hidden" class="form-control" id="addressUpdate" name="address">
                        </div>
                        <div class="form-group">
                            <label for="salaryMin" class="col-form-label">from (usd cent)</label>
                            <input type="number" class="form-control" id="salaryMinUpdate" name="salaryMin">
                        </div>
                        <div class="form-group">
                            <label for="salaryMax" class="col-form-label">to (usd cent)</label>
                            <input type="number" class="form-control" id="salaryMaxUpdate" name="salaryMax">
                        </div>
                        <div class="form-group">
                            <label for="url" class="col-form-label"></label>
                            <input type="text" class="form-control" id="urlUpdate" name="url"
                                   placeholder="https://www.example.com">
                        </div>
                        <div class="form-group">
                            <label for="skills" class="col-form-label">Skills</label>
                            <input type="text" class="form-control" id="skillsUpdate" name="skills">
                        </div>
                        <div class="form-group">
                            <input type="hidden" class="form-control" id="releaseDateUpdate" name="releaseDate">
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
                        back
                    </button>
                    <button type="button" class="btn btn-primary" onclick="updateVacancyTo()">
                        <span class="fa fa-check"></span>
                        save
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
                    <h6><em>Confirm pls !</em></h6>
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
                        back
                    </button>
                    <button type="button" class="btn btn-primary" onclick="deleteVacancyTo()">
                        <span class="fa fa-check"></span>
                        delete
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
                <h5 class="modal-title">Refresh DB by datas:</h5>
                <button type="button" class="close" data-dismiss="modal" onclick="closeNoty()">&times;</button>
            </div>
            <div class="modal-body">
                <form id="detailsRefreshForm">
                    <div class="form-group">
                        <label type="hidden" for="recordedDate" class="col-form-label"></label>
                        <input type="hidden" class="form-control" id="recordedDate" name="recordedDate">
                    </div>
                    <div class="form-group">
                        <label for="languageTask"><h7 class="btn-outline-info"><em>please enter language...</em></h7></label>
                        <input class="form-control" type="text" name="language" id="languageTask" list="language_name_2"
                        placeholder="please enter any language">
                        <datalist id="language_name_2">
                            <option value='all' selected>all</option>
                            <option value='Java'>Java</option>
                            <option value='Ruby'>Ruby</option>
                            <option value='Kotlin'>Kotlin</option>
                            <option value='Python'>Python</option>
                            <option value='JavaScript'>JavaScript</option>
                            <option value='TypeScript'>TypeScript</option>
                            <option value='Php'>Php</option>
                            <option value='React'>React</option>
                            <option value='Angular'>Angular</option>
                            <option value='Android'>Android</option>
                            <option value='C++'>C++</option>
                            <option value='C#'>C#</option>
                            <option value='.Net'>.Net</option>
                            <option value='Ios'>Ios</option>
                            <option value='Node.js'>Node.js</option>
                            <option value='Sql'>Sql</option>
                            <option value='Goland'>Goland</option>
                            <option value='Ruby on rails'>Ruby on rails</option>
                            <option value='Scala'>Scala</option>
                            <option value='Asw'>Asw</option>
                            <option value='Azure'>Azure</option>
                            <option value='Html&css'>Html&css</option>
                            <option value='Html'>Html</option>
                            <option value='css'>css</option>
                            <option value='Spark'>Spark</option>
                            <option value='React native'>React native</option>
                            <option value='Vue.js'>Vue.js</option>
                            <option value='Flutter'>Flutter</option>
                            <option value='Elixir'>Elixir</option>
                            <option value='Hadoop'>Hadoop</option>
                        </datalist>
                    </div>
                    <div class="form-group">
                        <label for="levelTask"><h7 class="btn-outline-info"><em>level...</em></h7></label>
                        <input class="form-control" type="text" name="level" id="levelTask" list="level_2"
                        placeholder="please enter any level">
                        <datalist id="level_2">
                            <option value='Trainee'>Trainee</option>
                            <option value='Junior'>Junior</option>
                            <option value='Middle'>Middle</option>
                            <option value='Senior'>Senior</option>
                            <option value='Expert'>Expert</option>
                            <option value='all'>all</option>
                        </datalist>
                    </div>
                    <div class="form-group">
                        <label for="workplaceTask"><h7 class="btn-outline-info"><em>location...</em></h7></label>
                        <input class="form-control" type="text" name="workplace" id="workplaceTask" list="city_name_2"
                        placeholder="please enter the place of work you need">
                        <datalist id="city_name_2">
                            <option value='all' selected>all</option>
                            <option value='Киев'>Kyiv</option>
                            <option value='Минск'>Minsk</option>
                            <option value='Львов'>Lviv</option>
                            <option value='Харьков'>Kharkiv</option>
                            <option value='Днепр'>Dnipro</option>
                            <option value='Украина'>Ukraine</option>
                            <option value='Канада'>Canada</option>
                            <option value='Польша'>Poland</option>
                            <option value='remote'>remote</option>
                            <option value='foreign'>foreign</option>
                        </datalist>
                    </div>
                    <div class="form-group">
                        <label type="hidden" for="userId" class="col-form-label"></label>
                        <input type="hidden" class="form-control" id="userId" name="userId">
                    </div>
                </form>
            </div>
            <span class="d-flex justify-content-center" id="spinner2" style="visibility: hidden">
                <h7><em>loading... wait min or two...<br>
                    after 2 min set  </em><h6 class="fa fa-filter text-info">  Filter</h6>
                </h7>
            </span>
            <h7 class="modal-title"></h7>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-dismiss="modal" onclick="closeNoty()">
                    <span class="fa fa-close"></span>
                    Back
                </button>
                <button type="button" class="btn btn-info" onclick="sendRefresh()">
                    Enter  
                    <span class="spinner-border spinner-border-sm" id="spinner1" style="visibility: hidden"></span>
                </button>
            </div>
        </div>
    </div>
</div>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>
