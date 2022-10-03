DELETE FROM rate;
DELETE FROM freshen;
DELETE FROM vote;
DELETE FROM user_roles;
DELETE FROM employer;
DELETE FROM vacancy;
DELETE FROM users;

ALTER SEQUENCE global_seq RESTART WITH 100000;


INSERT INTO users (name, email, password, registered)
VALUES ('Admin', 'admin@gmail.com', '{noop}admin', '2020-01-30 8:00:00'),
       ('User', 'user@yandex.ru', '{noop}password', '2020-01-30 8:00:00');

INSERT INTO user_roles (role, user_id)
VALUES ('ADMIN', 100000),
       ('USER', 100001);

INSERT INTO employer (name, address)
VALUES ('Huuuge Games', 'Киев'),
       ('RedLab', 'Киев');

INSERT INTO freshen (recorded_date, language, level, workplace, user_id)
VALUES ('2020-10-25 12:00:00', 'java', 'middle', 'киев', 100000),
       ('2020-10-25 13:00:00', 'php', 'middle', 'киев', 100001);

INSERT INTO freshen_goal (goal, freshen_id)
VALUES ('UPGRADE', 100004),
       ('FILTER', 100005);


INSERT INTO vacancy (title, salary_min, salary_max, link, skills, release_date, employer_id, freshen_id)
VALUES ('Middle Game Developer', 100000, 200000, 'https://grc.ua/vacancy/40006938?query=java', 'Still haven’t found your dream job? Huuuge Games is a gaming company on a mission to build the world’s largest real-time casual gaming platform connecting…', '2020-10-25',100002, 100004),
       ('Middle Java-разработчик', 150000, 300000, 'https://grc.ua/vacancy/40006938?query=java', '...на Java от 2-х лет. Понимание устройства и основных принципов работы платформы JVM. Умение отлаживать и профилировать java-приложения', '2020-10-25', 100003, 100005);

INSERT INTO vote (local_date, vacancy_id, user_id)
VALUES ('2020-10-25', 100006, 100000),
       ('2020-10-25', 100007, 100001);

INSERT INTO rate (name, value, local_date)
VALUES ('USDEUR', 1.02021, '2020-10-25'),
       ('USDGBP', 0.89565, '2020-10-25');



