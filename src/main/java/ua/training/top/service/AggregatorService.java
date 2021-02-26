package ua.training.top.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.training.top.model.Employer;
import ua.training.top.model.Freshen;
import ua.training.top.model.Vacancy;
import ua.training.top.model.Vote;
import ua.training.top.to.SubVacancyTo;
import ua.training.top.to.VacancyTo;
import ua.training.top.util.AggregatorUtil;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static ua.training.top.SecurityUtil.setTestAuthorizedUser;
import static ua.training.top.aggregator.installation.InstallationUtil.limitVacanciesToKeep;
import static ua.training.top.aggregator.installation.InstallationUtil.reasonPeriodToKeep;
import static ua.training.top.aggregator.strategy.provider.ProviderUtil.getAllProviders;
import static ua.training.top.model.Goal.UPGRADE;
import static ua.training.top.util.AggregatorUtil.*;
import static ua.training.top.util.EmployerUtil.getEmployerMap;
import static ua.training.top.util.EmployerUtil.getEmployersFromTos;
import static ua.training.top.util.FreshenUtil.asNewFreshen;
import static ua.training.top.util.UserUtil.asAdmin;
import static ua.training.top.util.VacancyUtil.getTos;

@Service
public class AggregatorService {
    private final static Logger log = LoggerFactory.getLogger(AggregatorService.class);
    @Autowired
    private VacancyService vacancyService;
    @Autowired
    private EmployerService employerService;
    @Autowired
    private VoteService voteService;
    @Autowired
    private FreshenService freshenService;

    public void refreshDB(Freshen freshen) {
        log.info("refreshDB by freshen {}", freshen);
        List<VacancyTo> vacancyTos = getAllProviders().selectBy(freshen);

        if (!vacancyTos.isEmpty()) {
            List<VacancyTo> vacancyTosForCreate = new ArrayList<>(vacancyTos);
            List<VacancyTo> vacancyToForUpdate = new ArrayList<>(vacancyTos);
            List<Vacancy> vacanciesDb = vacancyService.getAll();
            List<Vote> votes = voteService.getAll();
            List<VacancyTo> vacancyTosDb = getTos(vacanciesDb, votes);
            Map<VacancyTo, Vacancy> parallelMap = getParallelMap(vacanciesDb, votes);
            Map<SubVacancyTo, VacancyTo> mapAllVacancyTos = getMapVacancyTos(vacancyTosDb);
            Map<String, Employer> mapAllEmployers = getMapAllEmployers(vacancyTos);

            /*https://stackoverflow.com/questions/9933403/subtracting-one-arraylist-from-another-arraylist*/
            vacancyTosDb.forEach(vacancyTosForCreate::remove);
            vacancyTosForCreate.forEach(vacancyToForUpdate::remove);
            List<VacancyTo> ListTosForUpdate = new ArrayList<>(vacancyToForUpdate);
            Map<SubVacancyTo, VacancyTo> mapForUpdate = getMapVacancyTos(ListTosForUpdate);
            List<Vacancy> vacanciesForUpdate = new ArrayList<>();
            for (SubVacancyTo subVacancyTo : mapForUpdate.keySet()) {
                vacanciesForUpdate.add(AggregatorUtil.getForUpdate(mapForUpdate.get(subVacancyTo),
                        mapAllVacancyTos.get(subVacancyTo), parallelMap));
            }
            refresh(vacancyTosForCreate, vacanciesForUpdate, freshen, mapAllEmployers, vacanciesDb);
        }
    }

    @Transactional
    protected void refresh(List<VacancyTo> vacancyTosForCreate, List<Vacancy> vacanciesForUpdate,
                           Freshen freshen, Map<String, Employer> mapAllEmployers, List<Vacancy> vacanciesDb) {
        deleteVacanciesOutdated(vacanciesDb, reasonPeriodToKeep);
        Freshen freshenDb = freshenService.create(freshen);
        List<Vacancy> vacanciesForCreate = getForCreate(vacancyTosForCreate, mapAllEmployers, freshenDb);
        Set<Vacancy> vacancies = new HashSet<>(vacanciesForUpdate);
        vacancies.addAll(vacanciesForCreate);
        if (!vacancies.isEmpty()) {
            vacancyService.createUpdateList(new ArrayList<>(vacancies));
        }
        deleteVacanciesOutLimited(limitVacanciesToKeep);
        deleteFreshensOutLimit(limitVacanciesToKeep / 7);
        employerService.deleteEmptyEmployers();
    }

    public Map<String, Employer> getMapAllEmployers(List<VacancyTo> vacancyTos){
        Set<Employer> employersDb = new HashSet<>(employerService.getAll());
        Set<Employer> employersForCreate = new HashSet<>(getEmployersFromTos(vacancyTos));
        employersDb.forEach(employersForCreate::remove);
        if (!employersForCreate.isEmpty()) {
            Set<Employer> employersNew = new HashSet<>(employerService.createList(new ArrayList<>(employersForCreate)));
            employersDb.addAll(employersNew);
        }
        return getEmployerMap(employersDb);
    }

    @Transactional
    public void deleteVacanciesOutdated(List<Vacancy> vacanciesDb, LocalDate reasonDateToKeep) {
        log.info("deleteVacanciesBeforeDate reasonDateToKeep={}", reasonDateToKeep);
        List<Vacancy> listToDelete = vacanciesDb.parallelStream()
                .filter(vacancyTo -> reasonDateToKeep.isAfter(vacancyTo.getReleaseDate()))
                .collect(Collectors.toList());
        if (!listToDelete.isEmpty()) {
            log.info("deleteList {}", listToDelete.size());
            vacancyService.deleteList(listToDelete);
        }
    }

    @Transactional
    public void deleteFreshensOutLimit(int limitFreshenToKeep) {
        log.info("deleteFreshensOutLimit limitFreshenToKeep={}", limitFreshenToKeep);
        List<Freshen> allFreshens = freshenService.getAll();
        if (allFreshens.size() >= limitFreshenToKeep) {
        List<Freshen> listToDelete = allFreshens.parallelStream()
                .sorted((f1, f2) -> f1.getRecordedDate().isAfter(f2.getRecordedDate()) ? 1 : 0)
                .skip(limitFreshenToKeep)
                .collect(Collectors.toList());
        freshenService.deleteList(listToDelete);
        }
    }

    public void deleteVacanciesOutLimited(int limitVacanciesToKeep) {
        List<Vacancy> vacancies = vacancyService.getAll();
        log.info("deleteVacanciesOutLimited limitVacanciesToKeep={} vacancies={}", limitVacanciesToKeep, vacancies.size());
        List<Vacancy> listToDelete = Optional.of(vacancies.parallelStream()
                .sorted((v1, v2) -> v1.getReleaseDate().isAfter(v2.getReleaseDate()) ? 1 : 0)
                .sequential()
                .skip(limitVacanciesToKeep)
                .collect(Collectors.toList())).orElse(new ArrayList<>());
        if (!listToDelete.isEmpty()) {
            log.info("deleteList={}", listToDelete.size());
            vacancyService.deleteList(listToDelete);
        }
    }

    public static void main(String[] args) throws IOException {
        setTestAuthorizedUser(asAdmin());
//        setTestProvider();
        List<VacancyTo> vacancyTos = getAllProviders().selectBy(asNewFreshen("java", "санкт-петербург", UPGRADE));
//        List<VacancyTo> vacancyTos = getAllProviders().selectBy(asNewFreshen("java", "за_рубежем", UPGRADE));
//        List<VacancyTo> vacancyTos = getAllProviders().selectBy(asNewFreshen("java", "удаленно", UPGRADE));
//        List<VacancyTo> vacancyTos = getAllProviders().selectBy(asNewFreshen("java", "киев", UPGRADE));
        AtomicInteger i = new AtomicInteger(1);
        vacancyTos.forEach(vacancyNet -> log.info("\nvacancyNet № {}\n{}\n", i.getAndIncrement(), vacancyNet.toString()));
        log.info("\n\ncommon = {}", vacancyTos.size());
//        offTestProvider();

    }
}
// language='java', workplace='удаленно'
//07:24:14.488 INFO  u.t.top.aggregator.strategy.UAJoobleStrategy.getVacancies:48 - getVacancies city удаленно language=java
//07:24:15.884 ERROR o.s.s.support.TaskUtils$LoggingErrorHandler.handleError:95 - Unexpected error occurred in scheduled task
//java.time.format.DateTimeParseException: Text 'вулиця,-01-0харків,' could not be parsed at index 0

//07:25:17.098 INFO  u.t.top.aggregator.strategy.UAJoobleStrategy.getVacancies:48 - getVacancies city киев language=java
//07:25:29.593 ERROR ua.training.top.util.parser.ElementUtil.getVacanciesJooble:311 - there is error
//e=Index 1 out of bounds for length 1
// for UAJoobleStrategy for parse element
//<article class="_77074 _8b8c3" id="984307080289922515" data-test-name="_jobCard">
// <span class="_045f1"><h2 class="_448d5"><a rel="noopener nofollow" href="/desc/984307080289922515?ckey=java&amp;rgn=1&amp;pos=139&amp;elckey=1445516408188308946&amp;p=1&amp;sid=-3388254218757206631&amp;age=79&amp;relb=100&amp;brelb=115&amp;bscr=28732.7807752948&amp;scr=28732.7807752948&amp;iid=-734399283511047451" target="_blank" class="baa11 _1d27a button_size_M d95a3 _2c371 _03bd6"><span class="_4ef07 _32b89 ce5a3"><span class="c92c5"><span>Стажер по роботі з CRM (відділ IT)</span></span></span></a></h2></span>
// <section>
//  <div class="b2a33">
//   <p class="_44ec3">8 000 грн</p>
//   <p class="_9e28c">Стажування</p>
//   <div class="d4590">
//    &nbsp;...<span>базі власної онлайн-платформи. У нас працює потужна команда з понад 40 ІТ-співробітників, яку складають: інтегратори, розробники </span><b>Java/</b><span>PHP/C++/IOS/Android/АВАР/ESQL, системні/бізнес-аналітики, спеціалісти helpdesk та системні адміністратори. Щоб посилити нашу IT-</span>...&nbsp;
//   </div>
//  </div>
//  <div class="_209b7">
//   <div>
//    <div class="_7f0ce _8f5b0 b6b87">
//     <p class="_786d5">INTERTOP, сеть магазинов</p>
//    </div>
//    <div class="_77a3a d3fee _356ae _419f5">
//     <svg class="_36dc5 d6b7e _4f6da a4850 _2128e _8e9e1">
//      <use xlink:href="#icon-map_marker"></use>
//     </svg>
//     <div class="caption e5e03">
//      Київ, м. Лук'янівська
//     </div>
//    </div>
//    <div class="_77a3a d3fee _356ae _108aa ab7d6">
//     <svg class="_36dc5 d6b7e _4f6da a4850 _2128e _8e9e1">
//      <use xlink:href="#icon-clock"></use>
//     </svg>
//     <div class="caption">
//      3 дні тому
//     </div>
//    </div>
//   </div>
//  </div>
// </section>
//</article>
//07:25:29.595 ERROR ua.training.top.util.parser.ElementUtil.getVacanciesJooble:311 - there is error
//e=Index 1 out of bounds for length 1
// for UAJoobleStrategy for parse element
//<article class="_77074 _8b8c3" id="-6520427670303535541" data-test-name="_jobCard">
// <span class="_045f1"><h2 class="_448d5"><a rel="noopener nofollow" href="/desc/-6520427670303535541?ckey=java&amp;rgn=1&amp;pos=142&amp;elckey=1445516408188308946&amp;p=1&amp;sid=-3388254218757206631&amp;age=55&amp;relb=100&amp;brelb=115&amp;bscr=27893.3104644841&amp;scr=27893.3104644841&amp;iid=6765774679979256189" target="_blank" class="baa11 _1d27a button_size_M d95a3 _2c371 _03bd6"><span class="_4ef07 _32b89 ce5a3"><span class="c92c5"><span>Media Streaming Developer</span></span></span></a></h2></span>
// <section>
//  <div class="b2a33">
//   <p class="_44ec3">1 500 €</p>
//   <p class="_9e28c">Повний робочий день</p>
//   <div class="d4590">
//    &nbsp;...<span>разработке и развитии платформы видеовещания на Rust. Требования: - опыт профессиональной работы с одним из следующих языков: </span><b>Java,</b><span> Scala, Kotlin, Go, Rust, C, C++, C#, F#; - умение читать и понимать техническую литературу на английском языке; - знание</span>...&nbsp;
//   </div>
//  </div>
//  <div class="_209b7">
//   <div>
//    <div class="_7f0ce _8f5b0 b6b87">
//     <p class="_786d5">TR Logic LLC</p>
//    </div>
//    <div class="_77a3a d3fee _356ae _419f5">
//     <svg class="_36dc5 d6b7e _4f6da a4850 _2128e _8e9e1">
//      <use xlink:href="#icon-map_marker"></use>
//     </svg>
//     <div class="caption e5e03">
//      Київ
//     </div>
//    </div>
//    <div class="_77a3a d3fee _356ae _108aa ab7d6">
//     <svg class="_36dc5 d6b7e _4f6da a4850 _2128e _8e9e1">
//      <use xlink:href="#icon-clock"></use>
//     </svg>
//     <div class="caption">
//      2 дні тому
//     </div>
//    </div>
//   </div>
//  </div>
// </section>
//</article>


//07:43:08.531 INFO  u.t.top.aggregator.strategy.UAJoobleStrategy.getVacancies:48 - getVacancies city минск language=java
//07:43:09.476 ERROR ua.training.top.util.parser.salary.MinMax.salaryMin:17 - there is exception on getCorrectSalary method salaryMin data salary=we are currently looking for a remote middle software engineer with 3+ years of java experience
//07:43:10.416 ERROR ua.training.top.util.parser.salary.MinMax.salaryMin:17 - there is exception on getCorrectSalary method salaryMin data salary=we are currently looking for a remote senior java software engineer with 3+ years of development experience and unit test experience to join our team. the customer is one of the largest banks of russia
//07:43:11.316 ERROR ua.training.top.util.parser.salary.MinMax.salaryMin:17 - there is exception on getCorrectSalary method salaryMin data salary=we are currently looking for a remote senior java software engineer with 3+ years of experience working with java8+ to join our team. the customer is one of the world’s leading broadband
//07:43:12.028 ERROR ua.training.top.util.parser.data.CorrectSkills.getCorrectSkills:12 - there is skills is null
//07:43:12.028 ERROR ua.training.top.util.parser.data.CorrectSkills.getCorrectSkills:12 - there is skills is null
//07:43:12.029 ERROR ua.training.top.util.parser.data.CorrectSkills.getCorrectSkills:12 - there is skills is null
//07:43:12.029 ERROR ua.training.top.util.parser.data.CorrectSkills.getCorrectSkills:12 - there is skills is null
//07:43:12.029 ERROR ua.training.top.util.parser.data.CorrectSkills.getCorrectSkills:12 - there is skills is null
//07:43:12.029 ERROR ua.training.top.util.parser.data.CorrectSkills.getCorrectSkills:12 - there is skills is null
