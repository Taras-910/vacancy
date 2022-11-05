DELETE FROM RATE;
DELETE FROM FRESHEN;
DELETE FROM VOTE;
DELETE FROM USER_ROLES;
DELETE FROM EMPLOYER;
DELETE FROM VACANCY;
DELETE FROM USERS;

ALTER SEQUENCE GLOBAL_SEQ RESTART WITH 100000;


INSERT INTO USERS (NAME, EMAIL, PASSWORD, REGISTERED)
VALUES ('Admin', 'admin@gmail.com', '{noop}admin', '2020-01-30 8:00:00'),
       ('User', 'user@yandex.ru', '{noop}password', '2020-01-30 8:00:00');

INSERT INTO USER_ROLES (ROLE, USER_ID)
VALUES ('ADMIN', 100000),
       ('USER', 100001);

INSERT INTO EMPLOYER (NAME, ADDRESS)
VALUES ('Huuuge Games', 'Киев'),
       ('RedLab', 'Киев');

INSERT INTO FRESHEN (RECORDED_DATE, LANGUAGE, LEVEL, WORKPLACE, USER_ID)
VALUES ('2020-10-25 12:00:00', 'java', 'middle', 'киев', 100000),
       ('2020-10-25 13:00:00', 'php', 'middle', 'киев', 100001);

INSERT INTO FRESHEN_GOAL (GOAL, FRESHEN_ID)
VALUES ('UPGRADE', 100004),
       ('FILTER', 100005);


INSERT INTO VACANCY (TITLE, SALARY_MIN, SALARY_MAX, LINK, SKILLS, RELEASE_DATE, EMPLOYER_ID, FRESHEN_ID)
VALUES ('Middle Game Developer', 100000, 200000, 'https://grc.ua/vacancy/40006938?query=java', 'Still haven’t found your dream job? Huuuge Games is a gaming company on a mission to build the world’s largest real-time casual gaming platform connecting…', '2020-10-25',100002, 100004),
       ('Middle Java-разработчик', 150000, 300000, 'https://grc.ua/vacancy/40006938?query=java', '...на Java от 2-х лет. Понимание устройства и основных принципов работы платформы JVM. Умение отлаживать и профилировать java-приложения', '2020-10-25', 100003, 100005);

INSERT INTO VOTE (DATE_VOTE, VACANCY_ID, USER_ID)
VALUES ('2020-10-25', 100006, 100000),
       ('2020-10-25', 100007, 100001);

INSERT INTO RATE (NAME, VALUE_RATE, DATE_RATE)
VALUES ('USDUSD', 1.0, '2020-10-25'),
       ('USDUAH', 36.53, '2020-10-25'),
       ('USDPLN', 4.8544, '2020-10-25'),
       ('USDKZT', 469.5, '2020-10-25'),
       ('USDGBP', 0.87148, '2020-10-25'),
       ('USDEUR', 1.00711, '2020-10-25'),
       ('USDCZK', 24.7275, '2020-10-25'),
       ('USDCAD', 1.35791, '2020-10-25'),
       ('USDBGN', 1.9701, '2020-10-25'),
       ('USDBYR', 2.52, '2020-10-25');
