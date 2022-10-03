package ua.training.top.aggregator.strategies;

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
        VacancyTo vacancyTo1 = new VacancyTo(null, "Junior", "IBM",
                "Киев",
                80000, 120000, "https://example1.ua/vacancy/40006938?query=java", "Java, JavaScript, Php",
                LocalDate.now().minusDays(5), null, "middle", null, false);
        VacancyTo vacancyTo2 = new VacancyTo(null, "BeckEnd", "Company",
                "Харьков",
                120000, 150000, "https://example3.com/vacancy/123a", "Java, JavaScript",
                LocalDate.now().minusDays(3), null, "senior", null, false);
        VacancyTo vacancyTo3 = new VacancyTo(null, "Java-Developer", "Company",
                "Харьков",
                120000, 150000, "https://example3.com/vacancy/123a", "Java, JavaScript",
                LocalDate.now().minusDays(3), null, "middle", null, false);
        VacancyTo vacancyTo4 = new VacancyTo(null, "Middle C++ Engineer IRC107203", "GlobalLogic",
                "Киев", 1, 1,
                "https://example4.com/%D0%BE%D0%BF%D0%B8%D1%81%D0%B0%D0%BD%D0%B8%D0%B5-%D0%B2%D0%B0%D0%BA%D0%B0%D0%BD%D1%81%D0%B8%D0%B8?jk=b25fbd86b8fb297e",
                "Experience with java app server, e.g. Weblogic, Tomcat. Skills: Apache HTTP Server, bash scripting, Docker, Kubernetes, Oracle-DBA, Perl, Tomcat, WebLogic.",
                LocalDate.now().minusDays(3), "java", "middle", null, false);
        VacancyTo vacancyTo5 = new VacancyTo(null, "Middle/Senior", "GlobalLogic",
                "Варшава", 1, 1,
                "https://example4.com/%D0%BE%D0%BF%D0%B8%D1%81%D0%B0%D0%BD%D0%B8%D0%B5-%D0%B2%D0%B0%D0%BA%D0%B0%D0%BD%D1%81%D0%B8%D0%B8?jk=b25fbd86b8fb297e",
                "Experience with java app server, e.g. Weblogic, Tomcat. Skills: Apache HTTP Server, bash scripting, Docker, Kubernetes, Oracle-DBA, Perl, Tomcat, WebLogic.",
                LocalDate.now().minusDays(3), "java", "middle", null, false);
        VacancyTo vacancyTo6 = new VacancyTo(null, "Senior/Engineer", "GlobalLogic",
                "Минск", 1, 1,
                "https://example4.com/%D0%BE%D0%BF%D0%B8%D1%81%D0%B0%D0%BD%D0%B8%D0%B5-%D0%B2%D0%B0%D0%BA%D0%B0%D0%BD%D1%81%D0%B8%D0%B8?jk=b25fbd86b8fb297e",
                "Experience with java app server, e.g. Weblogic, Tomcat. Skills: Apache HTTP Server, bash scripting, Docker, Kubernetes, Oracle-DBA, Perl, Tomcat, WebLogic.",
                LocalDate.now().minusDays(3), "java", "middle", null, false);
        VacancyTo vacancyTo7 = new VacancyTo(null, "Canada Amazon — Java/Rust developer", "Amazon",
                "за кордоном",
                1, 1, "https://jobs.dou.ua/companies/amazon/vacancies/206565/?from=list_hot",
                "Interested in living and working in Toronto or Vancouver, Canada ? Are you a talented Software " +
                        "Development Engineer that is excited by the idea of exploring a career with Amazon AWS? " +
                        "Amazon is hosting a virtual hiring event for Ukrainians!",
                LocalDate.of(2022, 04, 22), "java", "middle",
                "за кордоном", true);
        VacancyTo vacancyTo8 = new VacancyTo(null, "IBM iSeries machines (RPG language) Engineer",
                "Intellias",
                "торонто",
                1, 1, "https://example1.ua/vacancy/40006938?query=java",
                "The client is a leading multi-brand technology solutions provider to business, government, " +
                        "education and healthcare customers in the United States, the United Kingdom and Canada",
                LocalDate.of(2022, 04, 21), "java", "middle",
                "канада", true);
        return List.of(vacancyTo1, vacancyTo2, vacancyTo3, vacancyTo4);
    }
}
