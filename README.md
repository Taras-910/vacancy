Java Enterprise Project Vacancy
==========================================

Specification
==========================================
Design and implement:
- REST API using Hibernate/Spring/SpringMVC 
- UI API using additionally JSP, JSTL, DataTables plugin, jQuery, jQuery notification, Bootstrap, Ajax

The task is:
Build a vacancies search, choice and store in dataBase, which are filling from public resources
The most popular IT resources were selected:
# Djinni, HH, Habr, DOU, LinkedIn, No Fluff Jobs, robota, Indeed, Jooble, Work
All query statistics (updates, filtering, voting) are stored in the database

2 types of users: admin and regular users
Admin can input/delete/update/get a vacancies and users, reload DB by every reques (language, workplace) 1 time / 2 hours

Users can control own profile and vote vacancy which like him, reload DB by every reques (language, workplace) 1 time / 4 hours

Java Enterprise: Maven/ Spring/ Security/ JPA(Hibernate)/ REST(Jackson).
=======================================================================
- основные классы приложения: [Vacancy, Employer, User, Vote, Freshen]
- все данные хранятся в базе данных [PostgreSQL, Heroku PostgreSQL]
- обслуживаются запросы авторизованных пользователей (profile)
- анонимных пользователям (anonymous) предлагается пройти регистрацию и авторизацию
- обновление данных из ресурсов, также ответы на запросы используют класс ТО [VacancyTo]
- бизнес-логика находится в слое сервисов 
- выборка данных из ресурсов и сортировка настроены преимущественно на сегмент `middle`
- репозиторий [Spring Data JPA]
- транзакционность [Springframework]
- пул коннектов [Tomcat]
- кеш [EhCache-based Cache], кешируются "список всех юзеров"
- для Rest базовая авторизация [SpringSecurity]
- доступ к ресурсам по ролям:
  `/rest/admin/**'`  - 'ADMIN'
  `/rest/profile/**` - 'USER'
  `/anonymous/**`    - доступ для регистрации
- для UI учетные данные авторизованного юзера хранятся в сессии
- тестирование REST контроллеров [Junit5]
- тестирование сервисов [Junit4]
- браузер [AJAX, DataTables, jQuery, jQuery notification plugin, Bootstrap]   
- обработка ошибок [ExceptionInfoHandler, GlobalExceptionHandler]
- приложение развернуто на Heroku `http://vacancy021.herokuapp.com`

- `curl` - примеры команд тестирования для rest profile:

#### rest profile vacancies getAll
`curl --location --request GET 'http://localhost:8080/vacancy/rest/profile/vacancies' \
--header 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu'`

#### rest profile vacancies get
`curl --location --request GET 'http://localhost:8080/vacancy/rest/profile/vacancies/100006' \
--header 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu'`

#### rest profile vacancies getByFilter
`curl --location --request GET 'http://localhost:8080/vacancy/rest/profile/vacancies/filter?language=java&workplace=%D0%BA%D0%B8%D0%B5%D0%B2' \
--header 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu'`


#### rest profile votes get
`curl --location --request GET 'http://localhost:8080/vacancy/rest/profile/votes/100008' \
--header 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu'`

#### rest profile votes getAll
`curl --location --request GET 'http://localhost:8080/vacancy/rest/profile/votes/' \
--header 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu'`

#### rest profile votes setVote
`curl --location --request POST 'http://localhost:8080/vacancy/rest/profile/votes/100007?enabled=true' \
--header 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu'`


#### rest profile employers get
`curl --location --request GET 'http://localhost:8080/vacancy/rest/profile/employers/100002' \
--header 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu'`

#### rest profile employers getAll
`curl --location --request GET 'http://localhost:8080/vacancy/rest/profile/employers' \
--header 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu'`


#### rest profile freshen get
`curl --location --request GET 'http://localhost:8080/vacancy/rest/profile/freshen/100004' \
--header 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu'`

#### rest profile freshen getAllOwn
`curl --location --request GET 'http://localhost:8080/vacancy/rest/profile/freshen/' \
--header 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu'`


#### rest profile users get
`curl --location --request GET 'http://localhost:8080/vacancy/rest/profile/users/' \
--header 'Authorization: Basic YWRtaW5AZ21haWwuY29tOmFkbWlu'`

#### rest profile users update
`curl --location --request PUT 'http://localhost:8080/vacancy/rest/profile/users' \
--header 'Authorization: Basic dXNlckB5YW5kZXgucnU6cGFzc3dvcmQ=' \
--header 'Content-Type: application/json' \
--header 'Cookie: JSESSIONID=EE571DE1FBD36762DF2AD1D865276721' \
--data-raw '{
"id": 100001,
"name": "UpdatedUser",
"email": "updatedEmail@mail.com",
"password": "updated",
"registered": "2020-07-30T06:00:00.000+00:00",
"roles": [
"USER"
]
}'`

#### rest profile users delete
`curl --location --request DELETE 'http://localhost:8080/vacancy/rest/profile/users/' \
--header 'Authorization: Basic dXNlckB5YW5kZXgucnU6cGFzc3dvcmQ='`
