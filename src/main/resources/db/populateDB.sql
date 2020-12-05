DELETE FROM vote;
DELETE FROM user_roles;
DELETE FROM employer;
DELETE FROM vacancy;
DELETE FROM users;

ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO users (name, email, password, registered)
VALUES ('Admin', 'admin@gmail.com', 'admin', '2020-01-30 8:00:00'),
       ('User', 'user@yandex.ru', 'password', '2020-01-30 8:00:00');

INSERT INTO user_roles (role, user_id)
VALUES ('ADMIN', 100000),
       ('USER', 100001);

INSERT INTO employer (name, address, site_name)
VALUES ('Huuuge Games', 'Киев', 'https://grc.ua'),
       ('RedLab', 'Киев', 'https://grc.ua');

INSERT INTO vacancy (title, local_date, salary_min, salary_max, link, skills, employer_id)
VALUES ('Middle Game Developer','2020-10-25', 0, 1, 'https://grc.ua/vacancy/40006938?query=java', 'Still haven’t found your dream job? Huuuge Games is a gaming company on a mission to build the world’s largest real-time casual gaming platform connecting…',100002),
       ('Middle Java-разработчик','2020-10-25', 0, 1, 'https://grc.ua/vacancy/40006938?query=java', '...на Java от 2-х лет. Понимание устройства и основных принципов работы платформы JVM. Умение отлаживать и профилировать java-приложения', 100003);

INSERT INTO vote (local_date, vacancy_id, user_id)
VALUES ('2020-10-25', 100004, 100000),
       ('2020-10-25', 100005, 100001);

