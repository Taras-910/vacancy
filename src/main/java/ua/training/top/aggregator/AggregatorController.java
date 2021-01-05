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
            Vacancy vacancyFromTo = getVacancyFromTo(vacancyTo);
            List<Employer> tempEmployersForUpdate = null;
            List<Employer> tempEmployersForCreate = null;
            List<Vacancy> tempVacanciesForUpdate = null;
            List<VacancyTo> tempTosExistEmployers = null;

            for( Employer employer : employersDb) {
                tempEmployersForUpdate = new ArrayList<>();
                tempEmployersForCreate = new ArrayList<>();
                tempVacanciesForUpdate = new ArrayList<>();
                tempTosExistEmployers = new ArrayList<>();
                if(!vacanciesDb.contains(vacancyFromTo)){
                    if (vacancyTo.getEmployerName().equals(employer.getName())) {
                        vacancyFromTo.setEmployer(employer);
                        tempVacanciesForUpdate.add(vacancyFromTo);
                        tempEmployersForUpdate.add(employer);
                        unDouble.set(false);
                    } else if (unDouble.get()) {
                        tempEmployersForCreate.add(getEmployerFromTo(vacancyTo));
                        tempTosExistEmployers.add(vacancyTo);
                    }
                }
                else{
                    Vacancy vFind = vacanciesDb.stream()
                            .filter(vDb -> vDb.getSkills().equals(vacancyFromTo.getSkills()) && vDb.getTitle().equals(vacancyFromTo.getTitle())
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
/*

//        List<VacancyTo> vacancyTos = getAllProviders().selectBy(new DoubleString("java", "киев"));
        List<VacancyTo> vacancyTos = getAllProviders().selectBy(new DoubleString("java", "за_рубежем"));

        AtomicInteger i = new AtomicInteger(1);
        vacancyTos.forEach(vacancyNet -> log.info("\nvacancyNet № {}\n{}\n", i.getAndIncrement(), vacancyNet.toString()));
        log.info("\n\ncommon = {}", vacancyTos.size());

*/


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




//        String corrected = "salary: 1000k—1300k () pln / day. requirements: java";
//        String corrected = "salary: 100—110 () pln / hour. requirements: java";
//        String corrected = "salary: 10000k—20000k () pln / month. requirements: java";
//        String corrected = "salary: 750—800 () pln / day. requirements: java";
//        String corrected = "salary: 4000k—5300k () usd / month. requirements: java";
//        String corrected = "salary: 16800k—21800k () pln / month. requirements: java";
//        String corrected = "salary: 9000k—14000k () pln / month. requirements: java";
//        String corrected = "salary: 80—120 () pln / hour. requirements: java";
//        String corrected = "salary: 12000k—20000k () pln / month. requirements: java";
//        String corrected = "salary: 700—1000k () pln / day. requirements: scala";
//        String corrected = "salary: 110—125 () pln / hour. requirements: java";
//        String corrected = "salary: 20100k—25200k () pln / month. requirements: java";
//        String corrected = "salary: pln per month: 7000k—13500k ()";
//        String corrected = "salary: 10000k—16000k (uop) pln / month. requirements: java";
//        String corrected = "salary: 10900k—17100k () pln / month. requirements: java";
//        String corrected = "salary: pln per month: 11000k—16000k ()";
//        String corrected = "salary: 50—100 () pln / hour. requirements: java";
//        String corrected = "salary: 140 () pln / hour. requirements: java";
//        String corrected = "salary: 15000k—20000k () pln / month. requirements: java";
//        String corrected = "salary: pln per month: 16000k—25000k ()";
//        String corrected = "salary: pln per month: 12000k—17000k ()";
//        String corrected = "salary: pln: 100—140 () / hour";
//        String corrected = "salary: 1000k () pln / day. requirements: java";
//        String corrected = "salary: 125—145 () pln / hour. requirements: java";
//        String corrected = "salary: pln per month: 17000k—21000k ()";
//        String corrected = "salary: pln: 640—1000k () / day";

//        String corrected = "Salary: 10.0k-16.0k (UoP) PLN / month. Requirements: Java, C#, SQL, English. Tools: Agile. Additionally: Sport subscription, Private healthcare, No dress code, Modern office. Java, C#, SQL, English, AWS, azure Senior Engineer for Cloud Services @ Link GroupESSENTIALS...";
//        String corrected = "Salary: 11.0k-18.0k (B2B) PLN / month. Requirements: Java, Spring, RESTful API, Hibernate, microservices. Tools: Agile, Scrum. Additionally: Sport subscription, Private healthcare, No dress code. Java, Spring, RESTful API, Hibernate, microservices, English, AWS, Docker...";
//        String corrected = "Salary: 10.9k-17.1k (B2B) PLN / month. Requirements: Java, Spring, Git, Maven, JUnit. Tools: Jira, Trello, Confluence, GitLab, Code, Gerrit, Sputnik, GIT, Jenkins, GitlabCI, Agile, Scrum. Additionally: Training budget, Flat structure, In-house trainings, In-house hack...";
//        String corrected = "Salary: PLN per month: 11.0k-16.0k (B2B), 10.0k-14.0k (UoP). Requirements: Java, Spring, REST, Java EE, Jenkins. Tools: Agile. Additionally: Sport subscription, Private healthcare, Flat structure, Small teams, International projects, Free coffee, Canteen, Bike parking,...";
//        String corrected = "50-100 (B2B) PLN / hour. Requirements: Java, Spring, Hibernate, REST, JUnit. Tools: Jira, Confluence, Wiki, Bitbucket, GitLab, Sonar, GIT, Jenkins, Agile, Scrum, Kanban. Additionally: In-house trainings, Conference budget, Team Events, Private health care, Kitchen...";
//        String corrected = "Salary: 140 (B2B) PLN / hour. Requirements: Java, Spring, Angular, English, PostgreSQL. Tools: GIT, Jenkins, Agile, Scrum. Additionally: Sport subscription, Training budget, Private healthcare, Flat structure, International projects, Modern office, No dress code, In-house..";
//        String corrected = "Salary: PLN per month: 12.0k-17.0k (B2B), 12.0k-17.0k (UoP). Requirements: Java, Spring, Git, NoSQL, English. Tools: Jira, Confluence, GitLab, GIT, Jenkins, Agile, Scrum. Additionally: Sport subscription, Training budget, Private healthcare, Flat structure, Small teams...";
//        String corrected = "Salary: PLN: 100-140 (B2B) / hour, 13.0k-18.0k (UoP) / month. Requirements: Java, Java EE, Hibernate, XML, EJB. Tools: Jira, GIT, Jenkins, Agile. Additionally: Sport subscription, Private healthcare, No dress code, Free parking, Canteen. Java, Java EE, Hibernate, XML,...";
//        String corrected = "Salary: 1.0k (B2B) PLN / day. Requirements: Java, Spring, English, microservices, xml. Tools: GIT, Agile. Additionally: Sport subscription, Private healthcare, Training budget. Java, Spring, English, microservices, xml, Maven, Git, REST, soapui Senior Java Engineer @...";

//        System.out.println("correct="+ getCorrectSalary(corrected));
//        System.out.println("min="+ salaryMin(getCorrectSalary(corrected)) + " max="+ salaryMax(getCorrectSalary(corrected)));

//    String text = "киев, м. тараса шевченко";
//    salaryMax(getCorrectSalary(getXssCleaned(text)));
//        System.out.println("correct="+ getCorrectSalary(text));
//        System.out.println("min="+ salaryMin(getCorrectSalary(text)) + " max="+ salaryMax(getCorrectSalary(text)));

/*
        String salary = "ASAPSalary: 620 - 720 (B2B) PLN / dayCategory...";
//        String test = "..dress code, Kitchen. English, Team player, Communication skills, Leadership skills, Critical thinking, Problem solving, Proactivity, Java, AIX Team Captain with Java background @ Dynatrace ESSENTIALS:Location: Gdańsk POLStart Date: ASAPSalary: 14.0k-22.0k (UoP)...";
        salary = salary.replaceAll(" ", "").replaceAll(" ", "").replaceAll("b2b", "");
        salary = salary.replaceAll("–", "—").replaceAll("-", "—");

        System.out.println("getCorrectSalary=" + getCorrectSalary(salary));
        System.out.println("salaryMax=" + salaryMax(getCorrectSalary(salary)));

*/
