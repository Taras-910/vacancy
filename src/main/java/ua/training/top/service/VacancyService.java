package ua.training.top.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.training.top.model.Employer;
import ua.training.top.model.Freshen;
import ua.training.top.model.Vacancy;
import ua.training.top.model.Vote;
import ua.training.top.repository.EmployerRepository;
import ua.training.top.repository.VacancyRepository;
import ua.training.top.to.VacancyTo;
import ua.training.top.util.VacancyUtil;
import ua.training.top.util.ValidationUtil;

import java.util.List;
import java.util.stream.Collectors;

import static ua.training.top.SecurityUtil.authUserId;
import static ua.training.top.util.EmployerUtil.getEmployerFromTo;
import static ua.training.top.util.FreshenUtil.getFreshenFromTo;
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
        return VacancyUtil.getTo(get(id), voteService.getAllForAuth());
    }

    public List<Vacancy> getAll() {
        log.info("getAll");
        return vacancyRepository.getAll();
    }

    public List<VacancyTo> getAllTos() {
        log.info("getAllTos for user {}", authUserId());
        return VacancyUtil.getTos(getAll(), voteService.getAllForAuth());
    }

    public List<Vacancy> getByFilter(Freshen f) {
        log.info("getByFilter language={} workplace={}", f.getLanguage(), f.getWorkplace());
        List<Vacancy> vacancies = getAll().stream()
                .filter(v -> f.getWorkplace().equals("all") || v.getFreshen().getWorkplace().contains(f.getWorkplace()))
                .filter(v -> f.getLanguage().equals("all") || v.getFreshen().getLanguage().contains(f.getLanguage()))
                .collect(Collectors.toList());
        if(vacancies.isEmpty()) {
            log.error("no suitable vacancies on request: language="+ f.getLanguage() + " workplace="+ f.getWorkplace());
//            throw new NotFoundException("no suitable vacancies on request: language="+ f.getLanguage() + " workplace="+ f.getWorkplace());
        }
        return vacancies;
    }

    @Transactional
    public List<VacancyTo> getTosByFilter(Freshen freshen) {
        log.info("getByFilter language={} workplace={}", freshen.getLanguage(), freshen.getWorkplace());
        List<Vote> votes = voteService.getAllForAuth();
        return getTos(getByFilter(freshen), votes);
    }

    public List<Vacancy> getByParams(String title, String skills, String employerName) {
        log.info("getByTitle title={}", title);
        return vacancyRepository.getByParams(title, skills, employerName);
    }

    @Transactional
    public Vacancy createVacancyAndEmployer(VacancyTo vacancyTo) {
        log.info("createVacancyAndEmployer vacancyTo={}", vacancyTo);
        if(!getByParams(vacancyTo.getTitle(), vacancyTo.getSkills(), vacancyTo.getEmployerName()).isEmpty()){
            throw new DataIntegrityViolationException("vacancy already exists in the database");
        }
        Vacancy vacancy = new Vacancy(fromTo(vacancyTo));
        checkNullDataVacancyTo(vacancyTo);
        Employer employer = employerRepository.getOrCreate(getEmployerFromTo(vacancyTo));
        Freshen freshen = freshenService.create(getFreshenFromTo(vacancyTo));
        return checkNotFound(vacancyRepository.save(vacancy, employer.getId(), freshen.getId()), "employerId=" + employer.getId());
    }

    @Transactional
    public List<Vacancy> createList(List<Vacancy> vacancies, Integer employerId, Integer freshenId) {
        if (vacancies != null) log.info("createAll {} vacancies for employerId={} with freshenId={}", vacancies.size(), employerId, freshenId);
        vacancies.forEach(ValidationUtil::checkNew);
        if (employerId == null || freshenId == null) {
            throw new IllegalArgumentException("must not null employerId=" + employerId + " or freshenId=" + freshenId);
        }
        vacancies.stream().map(vacancy -> vacancy.getTitle().toLowerCase()).distinct().collect(Collectors.toList());
        return checkNotFound(vacancyRepository.saveList(vacancies, employerId, freshenId), "employerId=" + employerId);
    }

    @Transactional
    public Vacancy updateTo(VacancyTo vacancyTo) {
        log.info("update vacancyTo {}", vacancyTo);
        Vacancy vacancyDb = vacancyRepository.get(vacancyTo.id());
        Vacancy newVacancy = getForUpdate(vacancyTo, vacancyDb);
        if(checkValidVote(vacancyTo, vacancyDb, newVacancy)){
            voteService.deleteListByVacancyId(vacancyTo.id());
        }
        return checkNotFoundWithId(vacancyRepository.save(newVacancy, vacancyDb.getEmployer().getId(), vacancyDb.getFreshen().getId()), vacancyTo.id());
    }

    public void delete(int id) {
        log.info("delete vacancy {}", id);
        checkNotFoundWithId(vacancyRepository.delete(id), id);
    }

    public void deleteList(List<Vacancy> list) {
        log.info("deleteList {}", list);
        vacancyRepository.deleteList(list);
    }
}
