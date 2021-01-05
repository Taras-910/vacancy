package ua.training.top.repository;

import ua.training.top.model.Vacancy;

import java.util.List;

public interface VacancyRepository {

    Vacancy save(Vacancy vacancy, int employerId);

    boolean delete(int id);

    Vacancy get(int id);

    List<Vacancy> getAll();

    List<Vacancy> saveAll(List<Vacancy> vacancies, int employerId);

    boolean deleteVacanciesOfEmployer(int employerId);

    List<Vacancy> getAllByFilter(String language, String workplace);

    List<Vacancy> getAllByWorkplace(String language);

    List<Vacancy> getAllByLanguage(String workplace);

    void deleteList(List<Vacancy> listToDelete);
}

