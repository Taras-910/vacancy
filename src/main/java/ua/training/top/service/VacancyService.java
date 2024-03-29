package ua.training.top.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.training.top.model.Freshen;
import ua.training.top.model.Vacancy;
import ua.training.top.repository.VacancyRepository;
import ua.training.top.to.VacancyTo;
import ua.training.top.util.VacancyUtil;
import ua.training.top.util.ValidationUtil;

import java.util.Collections;
import java.util.List;

import static ua.training.top.SecurityUtil.authUserId;
import static ua.training.top.aggregator.InstallationUtil.reasonPeriodKeeping;
import static ua.training.top.model.Goal.FILTER;
import static ua.training.top.util.EmployerUtil.getEmployerFromTo;
import static ua.training.top.util.FilterUtil.getFilter;
import static ua.training.top.util.FreshenUtil.getFreshenFromTo;
import static ua.training.top.util.VacancyUtil.*;
import static ua.training.top.util.ValidationUtil.checkNotFoundWithId;

@Service
public class VacancyService {
    private static final Logger log = LoggerFactory.getLogger(VacancyService.class);
    private final VacancyRepository repository;
    private final EmployerService employerService;
    private final VoteService voteService;
    private final FreshenService freshenService;

    public VacancyService(VacancyRepository repository, EmployerService employerRepository, VoteService voteService, FreshenService freshenService) {
        this.repository = repository;
        this.employerService = employerRepository;
        this.voteService = voteService;
        this.freshenService = freshenService;
    }

    public Vacancy get(int id) {
        log.info("get vacancy for id {}", id);
        return checkNotFoundWithId(repository.get(id), id);
    }

    public VacancyTo getTo(int id) {
        log.info("getTo vacancy {}", id);
        return VacancyUtil.getTo(get(id), voteService.getAllAuth());
    }

    public List<Vacancy> getAll() {
        log.info("getAll");
        return repository.getAll();
    }

    public List<VacancyTo> getAllTos() {
        log.info("getAllTos for user {}", authUserId());
        return getTos(getAll(), voteService.getAllAuth());
    }

    @Transactional
    public List<VacancyTo> getTosByFilter(Freshen freshen) {
        log.info("getTosByFilter language={} level={} workplace={}", freshen.getLanguage(), freshen.getLevel(), freshen.getWorkplace());
        freshen.setGoals(Collections.singleton(FILTER));
        return getTos(getFilter(repository.getAll(), freshen), voteService.getAllAuth());
    }

    public Vacancy getByParams(String title, String skills, int employerId) {
        log.info("getByTitle title={}", title);
        return repository.getByParams(title, skills, employerId);
    }

    @Transactional
    public Vacancy updateTo(VacancyTo vacancyTo) {
        log.info("update vacancyTo {}", vacancyTo);
        Vacancy vacancyDb = repository.get(vacancyTo.id());
        if(isNotSimilar(vacancyDb, vacancyTo)){
            voteService.deleteListByVacancyId(vacancyTo.id());
        }
        return repository.save(getForUpdate(vacancyTo, vacancyDb));
    }

    @Transactional
    public Vacancy createVacancyAndEmployer(VacancyTo vacancyTo, Freshen freshenDb) {
        log.info("createVacancyAndEmployer vacancyTo={}", vacancyTo);
        isNullPointerException(vacancyTo);
        ValidationUtil.validate(vacancyTo);
        Vacancy vacancy = new Vacancy(fromTo(vacancyTo));
        vacancy.setEmployer(employerService.getOrCreate(getEmployerFromTo(vacancyTo)));
        vacancy.setFreshen(freshenDb.isNew() ? freshenService.create(getFreshenFromTo(vacancyTo)) : freshenDb);
        return repository.save(vacancy);
    }

    @Transactional
    public List<Vacancy> createUpdateList(List<Vacancy> vacancies) {
        log.info("update vacancies size={}", vacancies.size());
        return repository.saveAll(vacancies);
    }

    @Transactional
    public void delete(int id) {
        log.info("delete vacancy {}", id);
        checkNotFoundWithId(repository.delete(id), id);
    }

    @Transactional
    public void deleteList(List<Vacancy> list) {
        log.info("delete deleteList");
        repository.deleteList(list);
    }

    @Transactional
    public void deleteOutDated() {
        log.info("deleteOutDated reasonPeriodKeeping {}", reasonPeriodKeeping);
        repository.deleteOutDated(reasonPeriodKeeping);
    }

    @Transactional
    public void deleteExceed(int exceed) {
        if (exceed > 0) {
            log.info("start delete exceed {}", exceed);
            repository.deleteList(repository.getOutNumber(exceed));
//            freshenService.deleteExceed();   // limit DB
//            voteService.deleteExceed();
        }
    }
}
