package ua.training.top.web;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import ua.training.top.aggregator.AggregatorController;
import ua.training.top.model.Vacancy;
import ua.training.top.service.VacancyService;
import ua.training.top.service.VoteService;
import ua.training.top.to.DoubleString;
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

    public List<VacancyTo> getAll() {
        int userId = authUserId();
        log.info("getAll for user {}", userId);
        return VacancyUtil.getTos(vacancyService.getAll(), voteService.getAllForAuthUser(userId));
    }

    public void delete(int id) {
        log.info("delete vacancy id {}", id);
        vacancyService.delete(id);
    }

    public Vacancy createVacancyAndEmployer(VacancyTo vacancyTo) {
        log.info("create vacancy {}", vacancyTo);
        return vacancyService.createVacancyAndEmployer(vacancyTo);
    }

    public void update(VacancyTo vacancyTo) {
        log.info("update {}", vacancyTo);
        vacancyService.update(vacancyTo);
    }

    public List<VacancyTo> getByFilter(@Nullable String language, @Nullable String workplace) {
        log.info("getByFilter language={} workplace={}", language, workplace);
        return getTos(vacancyService.getByFilter(language, workplace), voteService.getAll());
    }

    public static List<Vacancy> filteredTos(List<Vacancy> byFilter, String language, String workplace){
        byFilter.forEach(v -> {
        });
        return byFilter;
    }

    public void setVote(int vacancyId, boolean enabled) {
        log.info(enabled ? "enable {}" : "disable {}", vacancyId);
        voteService.setVote(vacancyId, enabled);
    }

    protected void refreshDB(DoubleString task){
        log.info("refreshDB {}", task);
        aggregatorController.refreshDB(getLowerCase(task));
    }
}


