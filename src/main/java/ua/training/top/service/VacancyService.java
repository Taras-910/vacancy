package ua.training.top.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import ua.training.top.model.Employer;
import ua.training.top.model.Vacancy;
import ua.training.top.repository.EmployerRepository;
import ua.training.top.repository.VacancyRepository;
import ua.training.top.to.VacancyTo;
import ua.training.top.util.VacancyUtil;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

import static ua.training.top.SecurityUtil.authUserId;
import static ua.training.top.util.VacancyUtil.*;
import static ua.training.top.util.ValidationUtil.checkNotFound;
import static ua.training.top.util.ValidationUtil.checkNotFoundWithId;
import static ua.training.top.util.jsoup.EmployerUtil.getEmployerFromTo;

@Service
public class VacancyService {
    private static final Logger log = LoggerFactory.getLogger(VacancyService.class);
    private final VacancyRepository vacancyRepository;
    private final EmployerRepository employerRepository;
    private final VoteService voteService;

    public VacancyService(VacancyRepository repository, EmployerRepository employerRepository, VoteService voteService) {
        this.vacancyRepository = repository;
        this.employerRepository = employerRepository;
        this.voteService = voteService;
    }

    public Vacancy get(int id) {
        log.info("get vacancy for id {}", id);
        return checkNotFoundWithId(vacancyRepository.get(id), id);
    }

    public VacancyTo getTo(int id) {
        log.info("getTo vacancy {}", id);
        return createTo(get(id), voteService.getAllForAuthUser());
    }

    public List<Vacancy> getAll() {
        log.info("getAll");
        return vacancyRepository.getAll();
    }

    public List<VacancyTo> getAllTos() {
        log.info("getAllTos for user {}", authUserId());
        return VacancyUtil.getTos(getAll(), voteService.getAllForAuthUser());
    }

    public List<Vacancy> getByFilter(String language, String workplace) {
        log.info("getByFilter language={} workplace={}", language, workplace);
        return getAll().stream()
                .filter(v -> workplace.isEmpty() || v.getWorkplace().contains(workplace))
                .filter(v -> language.isEmpty() || v.getLanguage().contains(language))
                .collect(Collectors.toList());
    }

    public List<VacancyTo> getTosByFilter(@Nullable String language, @Nullable String workplace) {
        log.info("getTosByFilter language={} workplace={}", language, workplace);
        return getTos(getByFilter(language, workplace), voteService.getAllForAuthUser());
    }

    @Transactional
    public Vacancy createVacancyAndEmployer(@Valid VacancyTo vacancyTo) {
        log.info("createVacancyAndEmployer vacancyTo={}", vacancyTo);
        Employer employer = employerRepository.getOrCreate(getEmployerFromTo(vacancyTo));
        return checkNotFound(vacancyRepository.save(getVacancyFromTo(vacancyTo), employer.getId()), "employerId=" + employer.getId());
    }

    @Transactional
    public List<Vacancy> createList(List<@Valid Vacancy> vacancies, int employerId) {
        if (vacancies != null) log.info("createAll {} vacancies for employerId {}", vacancies.size(), employerId);
        vacancies.forEach(v -> Assert.notNull(v, "vacancy must not be null"));
        vacancies.stream().map(vacancy -> vacancy.getTitle().toLowerCase()).distinct().collect(Collectors.toList());
        return checkNotFound(vacancyRepository.saveList(vacancies, employerId), "employerId=" + employerId);
    }

    @Transactional
    public Vacancy update(@Valid VacancyTo vacancyTo) {
        log.info("update vacancyTo {}", vacancyTo);
        Vacancy vacancyDb = get(vacancyTo.id());
        Vacancy newVacancy = getVacancyForUpdate(vacancyTo, vacancyDb);
        if(checkFaultVote(vacancyTo, vacancyDb, newVacancy)){
            voteService.deleteListByVacancyId(vacancyTo.id());
        }
        return checkNotFoundWithId(vacancyRepository.save(newVacancy, vacancyDb.getEmployer().getId()), vacancyTo.id());
    }

    @Transactional
    public void delete(int id) {
        log.info("delete vacancy {}", id);
        checkNotFoundWithId(vacancyRepository.delete(id), id);
    }

    @Transactional
    public void deleteList(List<Vacancy> list) {
        log.info("deleteList {}", list);
        vacancyRepository.deleteList(list);
    }

    public boolean checkFaultVote(VacancyTo vacancyTo, Vacancy vacancyDb, Vacancy newVacancy) {
        return !vacancyDb.getSkills().equals(newVacancy.getSkills()) || !vacancyDb.getTitle().equals(newVacancy.getTitle()) ||
                !vacancyDb.getEmployer().getName().equals(vacancyTo.getEmployerName());
    }
}
