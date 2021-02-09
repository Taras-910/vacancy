package ua.training.top.repository.datajpa;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ua.training.top.model.Freshen;
import ua.training.top.model.Vacancy;
import ua.training.top.repository.VacancyRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
@Repository
public class DataJpaVacancyRepository implements VacancyRepository {
    private static final Sort SORT_DATE_NAME = Sort.by(Sort.Direction.DESC, "releaseDate", "title");
    private final Logger log = LoggerFactory.getLogger(getClass());
    private final CrudVacancyRepository vacancyRepository;

    public DataJpaVacancyRepository(CrudVacancyRepository vacancyRepository) {
        this.vacancyRepository = vacancyRepository;
    }

    @Transactional
    @Override
    public Vacancy save(Vacancy vacancy) {
        Vacancy vacancyDouble = getByParams(vacancy.getTitle(), vacancy.getSkills(), vacancy.getEmployer().getId());
        if (vacancyDouble != null && (vacancy.isNew() || vacancyDouble.getId() != vacancy.getId())) {
            delete(vacancyDouble.getId());
            log.error("same vacancy " + vacancyDouble + " already existed in the database but was replaced by " + vacancy);
        }
        return vacancyRepository.save(vacancy);
    }

    @Transactional
    @Override
    public List<Vacancy> saveAll(List<Vacancy> vacancies) {
        List<Vacancy> vacanciesDb = new ArrayList<>();
        try {
            vacanciesDb = vacancyRepository.saveAll(vacancies);
        } catch (Exception e) {
            for(Vacancy v : vacancies) {
                log.error("error " + v + " redirect on method save");
                vacanciesDb.add(save(v));
            }
        }
        return vacanciesDb;
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
    public Vacancy getByParams(String title, String skills, int employerId) {
        try {
            return vacancyRepository.getByParams(title, skills, employerId);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List<Vacancy> getByFilter(Freshen freshen) {
        String language = freshen.getLanguage(), workplace = freshen.getWorkplace();
        return vacancyRepository.getByFilter(language.equals("all") ? "" : language, workplace.equals("all") ? "" : workplace);
    }

    @Override
    public Vacancy get(int id) {
        return vacancyRepository.findById(id).orElse(null);
    }

    @Override
    public List<Vacancy> getAll() {
        return vacancyRepository.getAll();
    }
}

