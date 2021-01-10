package ua.training.top.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import ua.training.top.model.Employer;
import ua.training.top.model.Freshen;
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
import static ua.training.top.util.ValidationUtil.*;

@Service
public class VacancyService {
    private static final Logger log = LoggerFactory.getLogger(VacancyService.class);
    private final VacancyRepository vacancyRepository;
    private final EmployerRepository employerRepository;
    private final VoteService voteService;
    private final FreshenService freshenService;

    public VacancyService(VacancyRepository repository, EmployerRepository employerRepository, VoteService voteService, FreshenService freshenService) {
        this.vacancyRepository = repository;
        this.employerRepository = employerRepository;
        this.voteService = voteService;
        this.freshenService = freshenService;
    }

    public Vacancy get(int id) {
        log.info("get vacancy for id {}", id);
        return checkNotFoundWithId(vacancyRepository.get(id), id);
    }

    public VacancyTo getTo(int id) {
        log.info("getTo vacancy {}", id);
        return VacancyUtil.getTo(get(id), voteService.getAllForAuthUser());
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
                .filter(v -> workplace.isEmpty() || v.getFreshen().getWorkplace().contains(workplace))
                .filter(v -> language.isEmpty() || v.getFreshen().getLanguage().contains(language))
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
        Freshen freshen = freshenService.create(getFreshenFromTo(vacancyTo));
        return checkNotFound(vacancyRepository.save(fromTo(vacancyTo), employer.getId(), freshen.getId()), "employerId=" + employer.getId());
    }

    @Transactional
    public List<Vacancy> createList(List<@Valid Vacancy> vacancies, int employerId, int freshenId) {
        if (vacancies != null) log.info("createAll {} vacancies for employerId {}", vacancies.size(), employerId);
        vacancies.forEach(v -> Assert.notNull(v, "vacancy must not be null"));
        vacancies.stream().map(vacancy -> vacancy.getTitle().toLowerCase()).distinct().collect(Collectors.toList());
        return checkNotFound(vacancyRepository.saveList(vacancies, employerId, freshenId), "employerId=" + employerId);
    }

    @Transactional
    public Vacancy update(VacancyTo vacancyTo) {
        log.info("update vacancyTo {}", vacancyTo);
        Vacancy vacancyDb = get(vacancyTo.id());
        Vacancy newVacancy = getForUpdate(vacancyTo, vacancyDb);
        if(checkValidVote(vacancyTo, vacancyDb, newVacancy)){
            voteService.deleteListByVacancyId(vacancyTo.id());
        }
        return checkNotFoundWithId(vacancyRepository.save(newVacancy, vacancyDb.getEmployer().getId(), vacancyDb.getFreshen().getId()), vacancyTo.id());
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
}
