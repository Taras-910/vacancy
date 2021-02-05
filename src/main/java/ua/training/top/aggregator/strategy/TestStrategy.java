package ua.training.top.aggregator.strategy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.training.top.model.Freshen;
import ua.training.top.to.VacancyTo;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public class TestStrategy implements Strategy {
    private final static Logger log = LoggerFactory.getLogger(TestStrategy.class);

    @Override
    public List<VacancyTo> getVacancies(Freshen doubleString) throws IOException {
        log.info("getVacancies language={} workplace={}", doubleString.getWorkplace(), doubleString.getLanguage());
        return getTestList();
    }

    public static List<VacancyTo> getTestList() {
        VacancyTo vacancyTo1 = new VacancyTo(null, "Junior", "IBM", "Киев",
                80000, 120000, "https://ibm.ua/vacancy/40006938?query=java", "Java Core",
                LocalDate.now().minusDays(5), "https://ibm.ua", null, null, false);

        VacancyTo vacancyTo2 = new VacancyTo(null, "Middle", "Expo", "Львов",
                100000, 200000, "https://expo.net/vacancy?123", "SpringBoot, Javax",
                LocalDate.now().minusDays(7), "https://expo.net", null, null, false);

        VacancyTo vacancyTo3 = new VacancyTo(null, "Java-Developer", "Company", "Харьков",
                120000, 150000, "https://nc.com/vacancy/123a", "Java, JavaScript",
                LocalDate.now().minusDays(3), "https://nc.com", null, null, false);

        return List.of(vacancyTo1, vacancyTo2, vacancyTo3);
    }
}
