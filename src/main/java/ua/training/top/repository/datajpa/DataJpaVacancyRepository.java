package ua.training.top.repository.datajpa;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ua.training.top.model.Employer;
import ua.training.top.model.Vacancy;
import ua.training.top.repository.VacancyRepository;

import java.util.List;
import java.util.Optional;

@Repository
public class DataJpaVacancyRepository implements VacancyRepository {
    private static final Sort SORT_DATE_NAME = Sort.by(Sort.Direction.DESC, "releaseDate","title");
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
    public boolean delete(int id) {
        return Optional.of(vacancyRepository.delete(id)).orElse(0) != 0;
    }

    @Override
    public Vacancy get(int id) {
        return vacancyRepository.findById(id).orElse(null);
    }

    @Override
    public List<Vacancy> getAll() {
        return vacancyRepository.findAll(SORT_DATE_NAME);
    }

    @Override
    @Transactional
    public List<Vacancy> saveAll(List<Vacancy> vacancies, int employerId) {
        Employer employer = employerRepository.getOne(employerId);
        if(employer != null) {
            vacancies.forEach(v -> v.setEmployer(employer));
        }
        return vacancyRepository.saveAll(vacancies);
    }

    @Override
    public boolean deleteEmployerVacancies(int employerId) {
        return vacancyRepository.deleteEmployerVacancies(employerId) != 0;
    }

    @Override
    public void deleteAll() {
        vacancyRepository.deleteAll();
    }

    @Override
    public List<Vacancy> getVacanciesByLangLocFilter(String language, String workplace) {
        List<Vacancy> vacancies = Optional.ofNullable(vacancyRepository.getVacanciesByLangLocFilter(language, workplace)).orElse(null);
        log.info("vacancies {}", vacancies);
        return vacancies;
    }

}

