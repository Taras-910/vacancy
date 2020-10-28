package ua.training.top.repository.dataJpa;

import org.springframework.stereotype.Repository;
import ua.training.top.model.Vacancy;
import ua.training.top.repository.VacancyRepository;

import java.util.List;

@Repository
public class DataJpaVacancyRepository implements VacancyRepository {
    @Override
    public Vacancy save(Vacancy vacancy, int employerId) {
        return null;
    }

    @Override
    public boolean delete(int id, int employerId) {
        return false;
    }

    @Override
    public Vacancy get(int id, int employerId) {
        return null;
    }

    @Override
    public List<Vacancy> getAll() {
        return null;
    }

    @Override
    public List<Vacancy> saveAll(List<Vacancy> vacancies, int employerId) {
        return null;
    }

    @Override
    public boolean deleteListOfVacancies(int employerId) {
        return false;
    }
}
