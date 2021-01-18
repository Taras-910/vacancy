package ua.training.top.repository;

import ua.training.top.model.Vacancy;

import java.util.List;

public interface VacancyRepository {

    Vacancy save(Vacancy vacancy, int employerId, int freshenId);

    List<Vacancy> saveList(List<Vacancy> vacancies, int employerId, int freshenId);

    Vacancy get(int id);

    List<Vacancy> getAll();

    boolean delete(int id);

    void deleteList(List<Vacancy> listToDelete);

    List<Vacancy> getByTitleAndSkillsAndEmployer(String title, String skills, String employerName);
}

