package ua.training.top.aggregator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import ua.training.top.model.Employer;
import ua.training.top.model.Vacancy;
import ua.training.top.service.EmployerService;
import ua.training.top.service.VacancyService;
import ua.training.top.service.VoteService;
import ua.training.top.to.DoubleString;
import ua.training.top.to.VacancyTo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static ua.training.top.aggregator.strategy.provider.ProviderUtil.getAllProviders;
import static ua.training.top.util.VacancyUtil.getVacancyFromTo;
import static ua.training.top.util.fromto.EmployerUtil.getEmployerFromTo;
import static ua.training.top.util.fromto.MapVacanciesUtil.getMapVacanciesForCreate;
import static ua.training.top.util.fromto.MapVacanciesUtil.getMapVacanciesForUpdate;

@Controller
public class AggregatorController {
    private final static Logger log = LoggerFactory.getLogger(AggregatorController.class);
    @Autowired
    private final VacancyService vacancyService;
    @Autowired
    private final EmployerService employerService;
    @Autowired
    private final VoteService voteService;

    public AggregatorController(VoteService voteService, EmployerService employerService, VacancyService vacancyService) {
        this.vacancyService = vacancyService;
        this.employerService = employerService;
        this.voteService = voteService;
    }

    @Transactional
    public void refreshDB(DoubleString doubleString){
        log.info("refreshDB by doubleString {}", doubleString);

        // проверка на наличе обновления в этот день ?
        List<VacancyTo> vacancyTos = getAllProviders().selectBy(doubleString);
//        List<VacancyTo> vacancyTos = getTestList();    // test data

        List<Employer> employersDb = employerService.getAll();
        List<Vacancy> vacanciesDb = vacancyService.getByFilter(doubleString.getLanguageTask(), doubleString.getWorkplaceTask());

//        vacancyService.deleteBeforeDate(reasonToKeepDate);
        Set<Employer> employersForCreate = new HashSet();
        Set<Employer> employersForUpdate = new HashSet();
        Set<Vacancy> vacanciesForUpdate = new HashSet<>();
        Set<VacancyTo> tosExistEmployers = new HashSet();

        vacancyTos.forEach(vacancyTo -> {
            AtomicBoolean unDouble = new AtomicBoolean(true);
            Vacancy v = getVacancyFromTo(vacancyTo);
            List<Employer> tempEmployersForUpdate = null;
            List<Employer> tempEmployersForCreate = null;
            List<Vacancy> tempVacanciesForUpdate = null;
            List<VacancyTo> tempTosExistEmployers = null;

            for( Employer employer : employersDb) {
                tempEmployersForUpdate = new ArrayList<>();
                tempEmployersForCreate = new ArrayList<>();
                tempVacanciesForUpdate = new ArrayList<>();
                tempTosExistEmployers = new ArrayList<>();
                if(!vacanciesDb.contains(v)){
                    if (vacancyTo.getEmployerName().equals(employer.getName())) {
                        v.setEmployer(employer);
                        tempVacanciesForUpdate.add(v);
                        tempEmployersForUpdate.add(employer);
                        unDouble.set(false);
                    } else if (unDouble.get()) {
                        tempEmployersForCreate.add(getEmployerFromTo(vacancyTo));
                        tempTosExistEmployers.add(vacancyTo);
                    }
                }
                else{
                    Vacancy vFind = vacanciesDb.stream()
                            .filter(vDb -> vDb.getSkills().equals(v.getSkills()) && vDb.getTitle().equals(v.getTitle())
//                            && vDb.getEmployer().getName().equals(vacancyTo.getEmployerName())
                            && vDb.getEmployer().getName().equals(employer.getName()))
                            .findAny().orElse(null);
                    if (vFind != null && unDouble.get()) {
                        vacancyTo.setId(vFind.getId());
                        vacancyService.update(vacancyTo);
                        unDouble.set(false);
                    }
                }
            }
            employersForUpdate.addAll(tempEmployersForUpdate);
            employersForCreate.addAll(tempEmployersForCreate);
            vacanciesForUpdate.addAll(tempVacanciesForUpdate);
            tosExistEmployers.addAll(tempTosExistEmployers);
        });
        List<Employer> employersCreated = employerService.createAll(new ArrayList<>(employersForCreate));
        vacancyService.createByMap(getMapVacanciesForCreate(employersCreated, tosExistEmployers));
        vacancyService.createByMap(getMapVacanciesForUpdate(employersForUpdate, vacanciesForUpdate));
        employerService.deleteEmptyEmployers();
    }

    public static void main(String[] args) throws IOException {

        List<VacancyTo> vacancyTos = getAllProviders().selectBy(new DoubleString("java", "киев"));
//        List<VacancyTo> vacancyTos = getAllProviders().selectBy(new DoubleString("java", "за_рубежем"));

        AtomicInteger i = new AtomicInteger(1);
        vacancyTos.forEach(vacancyNet -> log.info("\nvacancyNet № {}\n{}\n", i.getAndIncrement(), vacancyNet.toString()));
        log.info("\n\ncommon = {}", vacancyTos.size());

    }
}
//String temp = "..we are currently looking for a remote lead java software engineer with 5+ years of experience working with java8+ to join our team. the customer is one of the world’s leading broadband";
//String temp = "1v510810";
//String temp = "salary: 120000k—160000k (uop) pln / year. requirements: java";
//String temp = "salary: 175000k—200000k (uop) pln / year. requirements: java";
//String temp = "1v378378";
//String temp = "...code platforms like decisions";
//String temp = "...agile";
//String temp = "Salary: intermediate or higher ~Ability to develop native iOS and / or Android components to integrate with React Native (Swift/Objective-C/2 300 - 3 600 $";
//String temp = "Salary: PLN per month: 7.0k-13.5k (B2B), 6.5k-10.0k (UoP).·";
//           String temp = "Salary: 4.0k-5.3k (B2B) USD / month.·4 000 - 5 300 $";
//String temp = "Salary: 16.8k-21.8k (B2B) PLN / month.·";
//String temp = "Salary: 10.0k-20.0k (B2B) PLN / month.·";
//          String temp = "Salary: 1.0k-1.3k (B2B) PLN / day.·";
// String temp = "Salary: 750-800 (B2B) PLN / day.·";
//          String temp = "Salary: 700-1.0k (B2B) PLN / day.· Scala,";
//        String temp = "Salary: 700-0.7k (B2B) PLN / day.· Scala,";
//        String temp = "Salary: 0.7K-700 (B2B) PLN / day.· Scala,";
//String temp = "Salary: 100-110 (B2B) PLN / hour.·";
//          String temp = "Salary: PLN: 100-130 (B2B) / hour, 12.0k-16.5k (UoP) / month.·";
//        String temp = "Salary: 700-1.0k (B2B) PLN / day. Requirements: Scala,";
//
//
//        System.out.println("correct="+ getCorrectSalary(temp));
//        System.out.println("min="+ salaryMin(getCorrectSalary(temp)) + " max="+ salaryMax(getCorrectSalary(temp)));

//String temp = "48 000 — 67 000 грн";
//String temp = "130 000 — 145 000 грн";
//String temp = "30 000 — 90 000 грн";

//        System.out.println("validateAndFormat="+validateAndFormat(temp));
//        System.out.println("correct="+ getCorrectSalary(temp));
//        System.out.println("min="+ salaryMin(getCorrectSalary(temp)) + " max="+ salaryMax(getCorrectSalary(temp)));

//        String temp = "Extreme Idea";
//
//        System.out.println("validateAndFormat="+validateAndFormat(temp));
//        System.out.println("correct="+ getCorrectSalary(temp));
//        System.out.println("min="+ salaryMin(getCorrectSalary(temp)) + " max="+ salaryMax(getCorrectSalary(temp)));


//        salary = salary.replaceAll("–", "—");
//        salary = salary.replaceAll("-", "—");


//        String text = "31 декабря 2020";
//        System.out.println(text);
//        System.out.println("supportDate=" + supportDate(text));
//        System.out.println("parse=" + parse(supportDate(text), null));
//        System.out.println("clearTime=" + clearTime(parse(supportDate(text), null)));
