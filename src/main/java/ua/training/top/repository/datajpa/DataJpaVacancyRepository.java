package ua.training.top.repository.datajpa;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ua.training.top.model.Freshen;
import ua.training.top.model.Vacancy;
import ua.training.top.repository.VacancyRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static ua.training.top.util.MessageUtil.exist_end_replace;
import static ua.training.top.util.MessageUtil.update_error_and_redirect;
import static ua.training.top.util.aggregatorUtil.data.ConstantsUtil.all;

@Transactional(readOnly = true)
@Repository
public class DataJpaVacancyRepository implements VacancyRepository {
    private final Logger log = LoggerFactory.getLogger(getClass());
    private final CrudVacancyRepository vacancyRepository;

    public DataJpaVacancyRepository(CrudVacancyRepository vacancyRepository) {
        this.vacancyRepository = vacancyRepository;
    }

    @Override
    public Vacancy get(int id) {
        return vacancyRepository.findById(id).orElse(null);
    }

    @Override
    public List<Vacancy> getAll() {
        return vacancyRepository.findAll();
    }

    @Override
    public List<Vacancy> getOutNumber(int number) {
        return vacancyRepository.getOutNumber(number);
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
        String language = freshen.getLanguage(), workplace = freshen.getWorkplace(), level = freshen.getLevel();
        return vacancyRepository.getByFilter(language.equals(all) ? "" : language, level.equals(all) ? "" : level);
    }

    @Transactional
    @Override
    public Vacancy save(Vacancy vacancy) {
        Vacancy vacancyDouble = getByParams(vacancy.getTitle(), vacancy.getSkills(), vacancy.getEmployer().getId());
        if (vacancyDouble != null && (vacancy.isNew() || !vacancyDouble.getId().equals(vacancy.getId()))) {
            delete(vacancyDouble.getId());
            log.error(exist_end_replace, vacancyDouble, vacancy);
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
            for (Vacancy v : vacancies) {
                log.error(update_error_and_redirect, v);
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

    @Transactional
    @Override
    public void deleteOutDated(LocalDate reasonPeriodToKeep) {
        deleteList(vacancyRepository.getOutDated(reasonPeriodToKeep));
    }
}

