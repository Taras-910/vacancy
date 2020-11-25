package ua.training.top.repository.datajpa;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ua.training.top.model.Employer;
import ua.training.top.model.Vacancy;
import ua.training.top.repository.VacancyRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class DataJpaVacancyRepository implements VacancyRepository {
    private Logger log = LoggerFactory.getLogger(getClass());

    CrudVacancyRepository vacancyRepository;
    CrudEmployerRepository employerRepository;

    public DataJpaVacancyRepository(CrudVacancyRepository vacancyRepository, CrudEmployerRepository employerRepository) {
        this.vacancyRepository = vacancyRepository;
        this.employerRepository = employerRepository;
    }

    @Override
    public Vacancy save(Vacancy vacancy, int employerId) {
        Employer employer = employerRepository.getOne(employerId);
        vacancy.setEmployer(employer);
        if(vacancy.isNew()){
            return vacancyRepository.save(vacancy);
        }
        return vacancyRepository.get(vacancy.id(), employerId) != null ? vacancyRepository.save(vacancy) : null;
    }

    @Override
    public boolean delete(int id, int employerId) {
        return Optional.of(vacancyRepository.delete(id, employerId)).orElse(0) != 0;
    }

    @Override
    public Vacancy get(int id, int employerId) {
        return Optional.ofNullable(vacancyRepository.get(id, employerId)).orElse(null);
    }

    @Override
    public List<Vacancy> getAll() {
        return vacancyRepository.findAll();
    }

    @Override
    @Transactional
    public List<Vacancy> saveAll(List<Vacancy> vacancies, int employerId) {
        Employer employer = employerRepository.getOne(employerId);
        if(employer == null) return null;
        List<Vacancy> created = new ArrayList<>();
        vacancies.forEach(v -> {
            v.setEmployer(employer);
            created.add(new Vacancy(vacancyRepository.save(v)));
        });
        return created;
    }

    @Override
    public boolean deleteEmployerVacancies(int employerId) {
        return vacancyRepository.deleteEmployerVacancies(employerId) != 0;
    }
}

