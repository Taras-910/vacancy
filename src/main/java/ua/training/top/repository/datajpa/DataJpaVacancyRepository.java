package ua.training.top.repository.datajpa;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ua.training.top.model.Employer;
import ua.training.top.model.Freshen;
import ua.training.top.model.Vacancy;
import ua.training.top.repository.VacancyRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
@Repository
public class DataJpaVacancyRepository implements VacancyRepository {
    private static final Sort SORT_DATE_NAME = Sort.by(Sort.Direction.DESC, "releaseDate","title");
    private final Logger log = LoggerFactory.getLogger(getClass());
    private final CrudVacancyRepository vacancyRepository;
    private final CrudEmployerRepository employerRepository;
    private final CrudFreshenRepository freshenRepository;

    public DataJpaVacancyRepository(CrudVacancyRepository vacancyRepository, CrudEmployerRepository employerRepository, CrudFreshenRepository freshenRepository) {
        this.vacancyRepository = vacancyRepository;
        this.employerRepository = employerRepository;
        this.freshenRepository = freshenRepository;
    }

    @Transactional
    @Override
    public Vacancy save(Vacancy vacancy, int employerId, int freshenId) {
        Employer employer = employerRepository.getOne(employerId);
        Freshen freshen = freshenRepository.getOne(freshenId);
        vacancy.setEmployer(employer);
        vacancy.setFreshen(freshen);
        if(vacancy.isNew()){
            return vacancyRepository.save(vacancy);
        }
        return vacancyRepository.get(vacancy.id(), employerId) != null ? vacancyRepository.save(vacancy) : null;
    }

    @Transactional
    @Override
    public List<Vacancy> saveList(List<Vacancy> vacancies, int employerId, int freshenId) {
        Employer employer = employerRepository.getOne(employerId);
        Freshen freshen = freshenRepository.getOne(freshenId);
        if(employer != null && freshen != null) {
            vacancies.forEach(v -> {
                v.setEmployer(employer);
                v.setFreshen(freshen);
            });
        }
        return Optional.of(vacancyRepository.saveAll(vacancies)).orElse(new ArrayList<>());
    }

    @Transactional
    @Override
    public boolean delete(int id) {
        return Optional.of(vacancyRepository.delete(id)).orElse(0) != 0;
    }

    @Transactional
    @Override
    public void deleteList(List<Vacancy> listToDelete) {
        vacancyRepository.deleteAll(listToDelete);
    }

    @Override
    public List<Vacancy> getByTitle(String title) {
        List<Vacancy> vacancies = null;
        try {
            vacancies = vacancyRepository.getByTitle(title);
        } catch (Exception e) {}
        return vacancies;
    }

    @Override
    public Vacancy get(int id) {
        return vacancyRepository.findById(id).orElse(null);
    }

    @Override
    public List<Vacancy> getAll() {
        return vacancyRepository.findAll(SORT_DATE_NAME);
    }
}

