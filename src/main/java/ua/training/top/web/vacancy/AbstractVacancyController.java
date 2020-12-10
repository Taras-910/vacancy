package ua.training.top.web.vacancy;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import ua.training.top.SecurityUtil;
import ua.training.top.model.Employer;
import ua.training.top.model.Vacancy;
import ua.training.top.model.Vote;
import ua.training.top.service.VacancyService;
import ua.training.top.service.VoteService;
import ua.training.top.to.VacancyTo;
import ua.training.top.util.VacancyUtil;

import java.util.List;

import static ua.training.top.util.ValidationUtil.assureIdConsistent;
import static ua.training.top.util.ValidationUtil.checkNew;

public abstract class AbstractVacancyController {
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private VacancyService vacancyService;
    @Autowired
    private VoteService voteService;

    public Vacancy get(int id) {
        log.info("get vacancy {}", id);
        return vacancyService.get(id);
    }

    public void delete(int id) {
        log.info("delete vacancy id {}", id);
        vacancyService.delete(id);
    }

    public List<VacancyTo> getAll() {
        int userId = SecurityUtil.authUserId();
        log.info("getAll for user {}", userId);
        List<Vote> list = voteService.getAllForAuthUser(userId);
        return VacancyUtil.getTos(vacancyService.getAll(), list);
    }

    public Vacancy create(Vacancy vacancy, Employer employer) {
        checkNew(vacancy);
        checkNew(employer);
        log.info("create vacancy {} for employer {}", vacancy, employer);
        return vacancyService.create(vacancy, employer);
    }

    public void update(Vacancy vacancy, int id) {
        int userId = SecurityUtil.authUserId();
        assureIdConsistent(vacancy, id);
        log.info("update {} for user {}", vacancy, userId);
        vacancyService.update(vacancy, userId);
    }

    //
//      <ol>Filter separately
//      <li>by date</li>
//      <li>by time for every date</li>
//      </ol>
    public List<VacancyTo> getVacanciesByLangLocFilter(@Nullable String language, @Nullable String workplace) {
        log.info("vacancyLangLocFilter language {} residence {}", language, workplace);
        return VacancyUtil.getTos(vacancyService.getVacanciesByLangLocFilter(language, workplace), voteService.getAll());
    }
}


