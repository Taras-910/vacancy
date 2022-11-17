DROP TABLE IF EXISTS rate;
DROP TABLE IF EXISTS vote;
DROP TABLE IF EXISTS user_roles;
DROP TABLE IF EXISTS vacancy;
DROP TABLE IF EXISTS freshen_goal;
DROP TABLE IF EXISTS freshen;
DROP TABLE IF EXISTS employer;
DROP TABLE IF EXISTS users;
DROP SEQUENCE IF EXISTS global_seq;

CREATE SEQUENCE global_seq START WITH 100000;

CREATE TABLE users
(
    id INTEGER PRIMARY KEY DEFAULT nextval('global_seq'),
    name         VARCHAR        NOT NULL,
    email        VARCHAR        NOT NULL,
    password     VARCHAR        NOT NULL,
    registered   TIMESTAMP DEFAULT now(),
    enabled      BOOLEAN   DEFAULT TRUE
);
CREATE UNIQUE INDEX users_unique_email_idx ON users (email);

CREATE TABLE user_roles
(
    user_id      INTEGER        NOT NULL,
    role         VARCHAR        NOT NULL,
    CONSTRAINT user_roles_idx UNIQUE (user_id, role),
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE employer
(
    id INTEGER PRIMARY KEY DEFAULT nextval('global_seq'),
    name         TEXT           NOT NULL,
    address      TEXT           NOT NULL,
    CONSTRAINT employer_idx UNIQUE (name, address)
);

CREATE TABLE freshen
(
    id INTEGER PRIMARY KEY DEFAULT nextval('global_seq'),
    recorded_date TIMESTAMP DEFAULT now(),
    language      TEXT           NOT NULL,
    level         TEXT           NOT NULL,
    workplace     TEXT           NOT NULL,
    user_id       INTEGER        NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE freshen_goal
(
    freshen_id   INTEGER        NOT NULL,
    goal         VARCHAR        NOT NULL,
    FOREIGN KEY (freshen_id) REFERENCES freshen (id) ON DELETE CASCADE
);

CREATE TABLE vacancy
(
    id INTEGER PRIMARY KEY DEFAULT nextval('global_seq'),
    title        TEXT           NOT NULL,
    salary_min   INTEGER        NOT NULL,
    salary_max   INTEGER        NOT NULL,
    link         TEXT           NOT NULL,
    skills       TEXT           NOT NULL,
    release_date TIMESTAMP      NOT NULL,
    employer_id  INTEGER        NOT NULL,
    freshen_id   INTEGER        NOT NULL,
    CONSTRAINT vacancy_idx UNIQUE (title, skills, employer_id),
    FOREIGN KEY (employer_id) REFERENCES employer (id) ON DELETE CASCADE,
    FOREIGN KEY (freshen_id)  REFERENCES freshen (id) ON DELETE CASCADE
);

CREATE TABLE vote
(
    id INTEGER PRIMARY KEY DEFAULT nextval('global_seq'),
    date_vote    TIMESTAMP      NOT NULL,
    vacancy_id   INTEGER        NOT NULL,
    user_id      INTEGER        NOT NULL,
    CONSTRAINT votes_idx UNIQUE (vacancy_id, user_id),
    FOREIGN KEY (vacancy_id) REFERENCES vacancy (id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE rate
(
    id INTEGER PRIMARY KEY DEFAULT nextval('global_seq'),
    name       TEXT             NOT NULL,
    value_rate DOUBLE PRECISION NOT NULL,
    date_rate  TIMESTAMP        NOT NULL,
    CONSTRAINT rate_idx UNIQUE (name)
);

