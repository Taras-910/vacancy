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

import static ua.training.top.aggregator.util.ProviderUtil.getAllProviders;
import static ua.training.top.util.VacancyUtil.getVacancyFromTo;
import static ua.training.top.util.refresh.EmployerUtil.getEmployerFromTo;
import static ua.training.top.util.refresh.RefreshUtil.getMapVacanciesForCreate;
import static ua.training.top.util.refresh.RefreshUtil.getMapVacanciesForUpdate;

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

//        vacancyService.deleteBeforeDate(validReleaseDate);
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

//        List<VacancyTo> vacancyTos = getAllProviders().selectBy(new DoubleString("java", "Киев"));
        List<VacancyTo> vacancyTos = getAllProviders().selectBy(new DoubleString("java", "за_рубежем"));


        AtomicInteger i = new AtomicInteger(1);
        vacancyTos.forEach(vacancyNet -> log.info("\nvacancyNet № {}\n{}\n", i.getAndIncrement(), vacancyNet.toString()));
        log.info("\n\ncommon = {}", vacancyTos.size());


 //       String salary = "15.0k-20.0k (B2B) PLN / month. Requirements: Java, Spring, Hibernate, Apache CXF, SOAP. Tools: Jira, Confluence, GIT, Jenkins, Agile, Scrum. Additionally: Sport subscription, Training budget, Private healthcare, Flat structure, Free coffee, Canteen, Bike parking...";
 //       String text = "...1.0..";
//        String cleaned = "150-200";
//        String result = plnToUsd(cleaned.split("\\W")[0].concat("00")).concat("-").concat(plnToUsd(cleaned.split("\\W")[1].concat("00")));
//        System.out.println(cleaned.matches("\\d+\\W\\d+"));
//       System.out.println("salaryMax=" + salaryMax(getCorrectSalary(salary)));
//        temp = salary.contains("(b2b)") ? temp.substring(temp.indexOf(":") + 1, temp.indexOf("(b2b)") + 1) : temp;
//        temp = temp.contains("(uop)") ? temp.substring(temp.indexOf(":") + 1, temp.indexOf("(uop)") + 1) : temp;
//        System.out.println(salary.substring(salary.indexOf(":") + 1));

        //        System.out.println(text.matches(".*\\W\\d\\W+.*"));

    }

}
