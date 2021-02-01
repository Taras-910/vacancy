package ua.training.top.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.training.top.model.Freshen;
import ua.training.top.model.Vacancy;
import ua.training.top.model.Vote;
import ua.training.top.repository.VacancyRepository;
import ua.training.top.to.VacancyTo;
import ua.training.top.util.VacancyUtil;

import java.util.ArrayList;
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
    private final EmployerService employerService;
    private final VoteService voteService;
    private final FreshenService freshenService;

    public VacancyService(VacancyRepository repository, EmployerService employerRepository, VoteService voteService, FreshenService freshenService) {
        this.vacancyRepository = repository;
        this.employerService = employerRepository;
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
        return checkEmptyList(vacancies, f);
    }

    @Transactional
    public List<VacancyTo> getTosByFilter(Freshen freshen) {
        log.info("getByFilter language={} workplace={}", freshen.getLanguage(), freshen.getWorkplace());
        List<Vote> votes = voteService.getAllForAuth();
        return getTos(getByFilter(freshen), votes);
    }

    public Vacancy getByParams(String title, String skills, int employerId) {
        log.info("getByTitle title={}", title);
        return vacancyRepository.getByParams(title, skills, employerId);
    }

    @Transactional
    public Vacancy update(Vacancy vacancy) {
        if (vacancy != null) log.info("update vacancy {} with freshenId={}", vacancy, vacancy.getFreshen().getId());
        return vacancyRepository.save(vacancy);
    }

    @Transactional
    public Vacancy updateTo(VacancyTo vacancyTo) {
        log.info("update vacancyTo {}", vacancyTo);
        Vacancy vacancyDb = vacancyRepository.get(vacancyTo.id());
        Vacancy newVacancy = getForUpdate(vacancyTo, vacancyDb);
        if(isNotSimilar(vacancyDb, vacancyTo)){
            voteService.deleteListByVacancyId(vacancyTo.id());
        }
        return vacancyRepository.save(newVacancy);
    }

    public Vacancy createVacancyAndEmployer(VacancyTo vacancyTo, Freshen freshenDb) {
        log.info("createVacancyAndEmployer vacancyTo={}", vacancyTo);
        Vacancy vacancy = new Vacancy(fromTo(vacancyTo));
        checkNullDataVacancyTo(vacancyTo);
        vacancy.setEmployer(employerService.getOrCreate(getEmployerFromTo(vacancyTo)));
        vacancy.setFreshen(freshenDb.isNew() ? freshenService.create(getFreshenFromTo(vacancyTo)) : freshenDb);
        return vacancyRepository.save(vacancy);
    }

    @Transactional
    public List<Vacancy> createListVacancyAndEmployer(List<VacancyTo> vacancyTos, Freshen freshenDb) {
        log.info("createListVacancyAndEmployer vacancyTos={}", vacancyTos);
        List<Vacancy> listForCreate = new ArrayList<>();
        for (VacancyTo vTo : vacancyTos) {
            Vacancy vacancy = new Vacancy(fromTo(vTo));
            checkNullDataVacancyTo(vTo);
            vacancy.setEmployer(employerService.getOrCreate(getEmployerFromTo(vTo)));
            vacancy.setFreshen(freshenDb.isNew() ? freshenService.create(getFreshenFromTo(vTo)) : freshenDb);
            listForCreate.add(vacancy);
        }
        return createUpdateList(listForCreate);
    }

    @Transactional
    public List<Vacancy> createUpdateList(List<Vacancy> vacancies) {
        log.info("update vacancies size={}", vacancies.size());
        return checkNotFound(vacancyRepository.saveAll(vacancies), "list vacancies size=" + vacancies.size());
    }

    public void delete(int id) {
        log.info("delete vacancy {}", id);
        checkNotFoundWithId(vacancyRepository.delete(id), id);
        employerService.deleteEmptyEmployers();

    }

    public void deleteList(List<Vacancy> list) {
        log.info("deleteList {}", list);
        vacancyRepository.deleteList(list);
        employerService.deleteEmptyEmployers();
    }
}
