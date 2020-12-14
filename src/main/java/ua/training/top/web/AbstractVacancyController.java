package ua.training.top.web;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import ua.training.top.aggregator.AggregatorController;
import ua.training.top.model.Vacancy;
import ua.training.top.service.VacancyService;
import ua.training.top.service.VoteService;
import ua.training.top.to.DoubleWordTo;
import ua.training.top.to.VacancyTo;
import ua.training.top.util.VacancyUtil;

import java.util.List;

import static ua.training.top.SecurityUtil.authUserId;
import static ua.training.top.util.VacancyUtil.*;

public abstract class AbstractVacancyController {

    private final Logger log = LoggerFactory.getLogger(getClass());
    @Autowired
    private VacancyService vacancyService;
    @Autowired
    private VoteService voteService;
    @Autowired
    private AggregatorController aggregatorController;

    public VacancyTo get(int id) {
        log.info("get vacancy {}", id);
        return createTo(vacancyService.get(id), voteService.getAllForAuthUser(authUserId()));
    }

    public void delete(int id) {
        log.info("delete vacancy id {}", id);
        vacancyService.delete(id);
    }

    public List<VacancyTo> getAll() {
        int userId = authUserId();
        log.info("getAll for user {}", userId);
        return VacancyUtil.getTos(vacancyService.getAll(), voteService.getAllForAuthUser(userId));
    }

    public Vacancy createVacancyAndEmployer(VacancyTo vacancyTo) {
        log.info("create vacancy {}", vacancyTo);
        return vacancyService.createVacancyAndEmployer(vacancyTo);
    }

    public void update(VacancyTo vacancyTo) {
        log.info("update {}", vacancyTo);
        vacancyService.update(vacancyTo);
    }

    public List<VacancyTo> getTosByFilter(@Nullable String language, @Nullable String workplace) {
        log.info("vacancyLangLocFilter language {} residence {}", language, workplace);
        List<Vacancy> vacancy = vacancyService.getByFilter(language, workplace);
        return vacancy.isEmpty() ? VacancyUtil.getEmptyVacancyTos() : getTos(vacancy, voteService.getAll());
    }

    public void setVote(int vacancyId, boolean enabled) {
        log.info(enabled ? "enable {}" : "disable {}", vacancyId);
        voteService.setVote(vacancyId, enabled);
    }

    protected void refreshDB(DoubleWordTo task){
        log.info("refreshDB {}", task);
        aggregatorController.refreshDB(getLowerCase(task));
    }
}


