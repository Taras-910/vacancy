package ua.training.top.repository;

import ua.training.top.model.Freshen;
import ua.training.top.model.Vacancy;

import java.time.LocalDate;
import java.util.List;

public interface VacancyRepository {

    Vacancy save(Vacancy vacancy);

    List<Vacancy>  saveAll(List<Vacancy> vacancies);

    Vacancy get(int id);

    List<Vacancy> getAll();

    boolean delete(int id);

    void deleteList(List<Vacancy> listToDelete);

    Vacancy getByParams(String title, String skills, int employerId);

    List<Vacancy> getByFilter(Freshen freshen);

    List<Vacancy> deleteOutDated(LocalDate reasonPeriodKeeping);

    List<Vacancy> getList(int number);

    List<Vacancy> getFirstPortion(int limit);
}

