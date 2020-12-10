package ua.training.top.aggregator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ua.training.top.aggregator.util.VacancyNetUtil;
import ua.training.top.model.Employer;
import ua.training.top.model.Vacancy;
import ua.training.top.model.Vote;
import ua.training.top.to.VacancyNet;
import ua.training.top.web.jsp.EmployerController;
import ua.training.top.web.jsp.VacancyController;
import ua.training.top.web.jsp.VoteController;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static ua.training.top.aggregator.util.EmployerNetUtil.getEmployers;
import static ua.training.top.aggregator.util.ProviderUtil.getAllProviders;
import static ua.training.top.aggregator.util.ToCorrectDataUtil.getCorrectSalary;

@Controller
public class AggregatorController {
    private final static Logger log = LoggerFactory.getLogger(AggregatorController.class);
    @Autowired
    private final VacancyController vacancyController;
    @Autowired
    private final EmployerController employerController;
    @Autowired
    private final VoteController voteController;

    public AggregatorController(VoteController voteController, EmployerController employerController, VacancyController vacancyController) {
        this.vacancyController = vacancyController;
        this.employerController = employerController;
        this.voteController = voteController;
    }

    public void refreshDB(String workplace, String language){
        log.info("refreshDB by city {} and language {}", workplace, language);
        // проверка на наличе обновления в этот день ?
        List<VacancyNet> vacanciesNet = getAllProviders().selectBy(workplace, language);

        List<Employer> preparedEmployers = getEmployers(vacanciesNet).stream()
                .distinct()
                .collect(Collectors.toList());

        List<Employer> oldEmployers = employerController.getAll();
        Map<Integer, Employer> oldEmployersMap = new HashMap<>();
        oldEmployers.forEach(e ->  oldEmployersMap.put(e.getId(), e));
        employerController.deleteAll();
        List<Employer> newEmployers = employerController.createAll(preparedEmployers);

        List<Vacancy> oldVacancies = vacancyController.getAll();
        vacancyController.deleteAll();
        List<Vacancy> newVacancies = new ArrayList<>();
        Map<Integer, List<Vacancy>> mapVacancies = VacancyNetUtil.getMapVacancies(newEmployers, vacanciesNet, language, workplace);
        mapVacancies.forEach((employerId, vacancies) -> newVacancies.addAll(vacancyController.createAll(vacancies, employerId)));

        refreshVotes(oldVacancies, newVacancies);
}

    private void refreshVotes(List<Vacancy> oldVacancies, List<Vacancy> newVacancies) {
        List <Vote> oldVotes = voteController.getAll();
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
        voteController.deleteAll();
        voteController.createAll(new ArrayList<>(preparedVotes));
    }

    public static void main(String[] args) throws IOException {
        List<VacancyNet> vacanciesFrom = getAllProviders().selectBy("за_рубежем", "java");
//        List<VacancyNet> vacanciesFrom = getAllProviders().selectBy("за_рубежем", "java");
//        List<VacancyNet> vacanciesFrom = getAllProviders().selectBy("киев", "java");

 /*       AtomicInteger i = new AtomicInteger(1);
        vacanciesFrom.forEach(vacancyNet -> log.info("\nvacancyNet № {}\n{}\n", i.getAndIncrement(), vacancyNet.toString()));
        log.info("\n\ncommon = {}", vacanciesFrom.size());
*/


        String text = "Europa Workintense spol. s r.o. · Агентство · Другие страны";
        System.out.println("test   " + getCorrectSalary(text));
    }
}
