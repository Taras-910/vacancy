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
        VacancyTo vacancyTo1 = new VacancyTo(null, "Junior", "IBM", null,
                80000, 120000, "https://example1.ua/vacancy/40006938?query=java", "Java, JavaScript, Php",
                LocalDate.now().minusDays(5), "https://example1.ua", null, null, false);

        VacancyTo vacancyTo2 = new VacancyTo(null, "Middle", "Expo", "Львов",
                100000, 200000, "https://example2.net/vacancy?123", "SpringBoot, Java",
                LocalDate.now().minusDays(7), "https://example2.net", null, null, false);

        VacancyTo vacancyTo3 = new VacancyTo(null, "Java-Developer", "Company", "Харьков",
                120000, 150000, "https://example3.com/vacancy/123a", "Java, JavaScript",
                LocalDate.now().minusDays(3), "https://example3.com", null, null, false);

        VacancyTo vacancyTo4 = new VacancyTo(null, "Middle C++ Engineer IRC107203", "GlobalLogic",
                "Киев", 1, 1,
                "https://example4.com/%D0%BE%D0%BF%D0%B8%D1%81%D0%B0%D0%BD%D0%B8%D0%B5-%D0%B2%D0%B0%D0%BA%D0%B0%D0%BD%D1%81%D0%B8%D0%B8?jk=b25fbd86b8fb297e",
                "Experience with java app server, e.g. Weblogic, Tomcat. Skills: Apache HTTP Server, bash scripting, Docker, Kubernetes, Oracle-DBA, Perl, Tomcat, WebLogic.",
                LocalDate.now().minusDays(3), "https://example4.com", null, null, false);

        return List.of(vacancyTo1);
    }
}
