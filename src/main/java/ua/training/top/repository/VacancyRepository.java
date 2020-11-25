package ua.training.top.repository;

import ua.training.top.model.Vacancy;

import java.util.List;

public interface VacancyRepository {

    Vacancy save(Vacancy vacancy, int employerId);

    boolean delete(int id, int employerId);

    Vacancy get(int id, int employerId);

    List<Vacancy> getAll();

    List<Vacancy> saveAll(List<Vacancy> vacancies, int employerId);

    boolean deleteEmployerVacancies(int employerId);
}

