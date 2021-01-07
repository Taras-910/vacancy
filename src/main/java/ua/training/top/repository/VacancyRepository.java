package ua.training.top.repository;

import ua.training.top.model.Vacancy;

import java.util.List;

public interface VacancyRepository {

    Vacancy save(Vacancy vacancy, int employerId);

    List<Vacancy> saveList(List<Vacancy> vacancies, int employerId);

    Vacancy get(int id);

    List<Vacancy> getAll();

    boolean delete(int id);

    void deleteList(List<Vacancy> listToDelete);
}

