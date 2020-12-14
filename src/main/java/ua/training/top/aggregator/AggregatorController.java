package ua.training.top.aggregator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ua.training.top.aggregator.util.VacancyNetUtil;
import ua.training.top.model.Employer;
import ua.training.top.model.Vacancy;
import ua.training.top.model.Vote;
import ua.training.top.service.EmployerService;
import ua.training.top.service.VacancyService;
import ua.training.top.service.VoteService;
import ua.training.top.to.DoubleWordTo;
import ua.training.top.to.VacancyNet;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static ua.training.top.aggregator.util.EmployerNetUtil.getEmployers;
import static ua.training.top.aggregator.util.ProviderUtil.getAllProviders;

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

    public void refreshDB(DoubleWordTo task){
        log.info("refreshDB by task {}", task);
        // проверка на наличе обновления в этот день ?
        List<VacancyNet> vacanciesNet = getAllProviders().selectBy(task);

        List<Employer> preparedEmployers = getEmployers(vacanciesNet).stream()
                .distinct()
                .collect(Collectors.toList());

        List<Employer> oldEmployers = employerService.getAll();
        Map<Integer, Employer> oldEmployersMap = new HashMap<>();
        oldEmployers.forEach(e ->  oldEmployersMap.put(e.getId(), e));
        employerService.deleteAll();
        List<Employer> newEmployers = employerService.createAll(preparedEmployers);

        List<Vacancy> oldVacancies = vacancyService.getAll();
        vacancyService.deleteAll();
        List<Vacancy> newVacancies = new ArrayList<>();
        Map<Integer, List<Vacancy>> mapVacancies = VacancyNetUtil.getMapVacancies(newEmployers, vacanciesNet, task);
        mapVacancies.forEach((employerId, vacancies) -> newVacancies.addAll(vacancyService.createAll(vacancies, employerId)));

        refreshVotes(oldVacancies, newVacancies);
}

    private void refreshVotes(List<Vacancy> oldVacancies, List<Vacancy> newVacancies) {
        List <Vote> oldVotes = voteService.getAll();
        Set <Vote> preparedVotes = new LinkedHashSet<>();

        newVacancies.forEach(newVacancy -> {
            oldVacancies.forEach(oldVacancy -> {
                if (oldVacancy.equals(newVacancy)) {
                    int oldVacancyId = oldVacancy.getId();
                    int newVacancyId = newVacancy.getId();
                    oldVotes.forEach(oldVote ->{
                        if (oldVote.getVacancyId() == oldVacancyId) {
                            preparedVotes.add(new Vote(null, oldVote.getLocalDate(), newVacancyId, oldVote.getUserId()));
                        }
                    });
                }
            });
        });
        voteService.deleteAll();
        voteService.createAll(new ArrayList<>(preparedVotes));
    }

    public static void main(String[] args) throws IOException {
//        List<VacancyNet> vacanciesFrom = getAllProviders().selectBy("за_рубежем", "java");
//        List<VacancyNet> vacanciesFrom = getAllProviders().selectBy("за_рубежем", "java");
//        List<VacancyNet> vacanciesFrom = getAllProviders().selectBy("киев", "java");

 /*       AtomicInteger i = new AtomicInteger(1);
        vacanciesFrom.forEach(vacancyNet -> log.info("\nvacancyNet № {}\n{}\n", i.getAndIncrement(), vacancyNet.toString()));
        log.info("\n\ncommon = {}", vacanciesFrom.size());
*/


/*
        String text = "Europa Workintense spol. s r.o. · Агентство · Другие страны";
        System.out.println("test   " + getCorrectSalary(text));
*/

        //"yyyy-MM-dd"
//        String text = "https://datatables.net/examples/basic_init/hidden_columns.html";


//        System.out.println(text.contains("http") ? text.replace("/", " ").split(":")[1].trim().split(" ")[0] : text);
//        System.out.println(text.replace("https://", "").split("/")[0]);
        DoubleWordTo task = new DoubleWordTo("", "Киев");

        String test = null;
        System.out.println(test.isEmpty());
        System.out.println(test.equals(""));
        System.out.println(test.translateEscapes());



    }

}
