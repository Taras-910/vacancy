package ua.training.top.repository.dataJpa;

import org.springframework.stereotype.Repository;
import ua.training.top.model.Employer;
import ua.training.top.repository.EmployerRepository;

import java.util.List;

@Repository
public class DataJpaEmployerRepository implements EmployerRepository {
    @Override
    public Employer save(Employer employer) {
        return null;
    }

    @Override
    public boolean delete(int id) {
        return false;
    }

    @Override
    public Employer getById(int id) {
        return null;
    }

    @Override
    public List<Employer> getAll() {
        return null;
    }

/*
    @Override
    public List<Employer> getAllWithVacanciesOfDate(Date localDate) {
        return null;
    }

    @Override
    public Employer getByIdWithVacanciesOfDate(int employerId, Date localDate) {
        return null;
    }
*/

    @Override
    public Employer getByName(String name) {
        return null;
    }

    @Override
    public List<Employer> getAllWithVacancies() {
        return null;
    }
}
