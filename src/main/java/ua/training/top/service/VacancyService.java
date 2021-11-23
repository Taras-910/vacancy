package ua.training.top.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.training.top.model.Freshen;
import ua.training.top.model.Vacancy;
import ua.training.top.repository.VacancyRepository;
import ua.training.top.to.VacancyTo;
import ua.training.top.util.VacancyUtil;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;

import static ua.training.top.SecurityUtil.authUserId;
import static ua.training.top.aggregator.installation.InstallationUtil.*;
import static ua.training.top.model.Goal.FILTER;
import static ua.training.top.util.EmployerUtil.getEmployerFromTo;
import static ua.training.top.util.FreshenUtil.getFreshenFromTo;
import static ua.training.top.util.VacancyCheckUtil.isNotSimilar;
import static ua.training.top.util.VacancyCheckUtil.isNullPointerException;
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
        return VacancyUtil.getTo(get(id), voteService.getAllForAuth());
    }

    public List<Vacancy> getAll() {
        log.info("getAll");
        return repository.getAll();
    }

    public List<VacancyTo> getAllTos() {
        log.info("getAllTos for user {}", authUserId());
        if(firstDownload) {
            offFirstDownload();
            return getTos(repository.getFirstPage(PageRequest.of(0,450)), voteService.getAllForAuth());
        }
        return getTos(getAll(), voteService.getAllForAuth());
    }

    @Transactional
    public List<VacancyTo> getTosByFilter(Freshen freshen) {
        log.info("getTosByFilter language={} level={} workplace={}", freshen.getLanguage(), freshen.getLevel(), freshen.getWorkplace());
        freshen.setGoals(Collections.singleton(FILTER));
        freshenService.create(freshen);
        return getTos(repository.getByFilter(freshen), voteService.getAllForAuth());
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

    public void delete(int id) {
        log.info("delete vacancy {}", id);
        checkNotFoundWithId(repository.delete(id), id);
        employerService.deleteEmptyEmployers();

    }

    @Transactional
    public void deleteList(List<Vacancy> listToDelete) {
        log.info("deleteList");
        if (!listToDelete.isEmpty()) {
            repository.deleteList(listToDelete);
        }
    }

    @Transactional
    public List<Vacancy> deleteOutDatedAndGetAll() {
        log.info("deleteOutDateAndGetAll reasonPeriodKeeping {}", reasonPeriodKeeping);
        freshenService.deleteOutDated(LocalDateTime.of(reasonPeriodKeeping, LocalTime.MIN));
        repository.deleteOutDated(reasonPeriodKeeping);
        List<Vacancy> resumes = repository.getAll();
        voteService.deleteOutDated(reasonPeriodKeeping);
        return resumes;
    }

    @Transactional
    public void deleteExceedLimit(int exceed) {
        if (exceed > 0) {
            log.info("start delete exceed {}", exceed);
            deleteList(repository.getList(exceed));
            freshenService.deleteExceedLimit(limitFreshensKeeping);
            voteService.deleteExceedLimit(limitVotesKeeping);
        }
    }
}
