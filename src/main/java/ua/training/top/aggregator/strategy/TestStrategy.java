package ua.training.top.aggregator.strategy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.training.top.model.Freshen;
import ua.training.top.to.VacancyTo;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public class TestStrategy implements Strategy{
    private final static Logger log = LoggerFactory.getLogger(TestStrategy.class);
    @Override
    public List<VacancyTo> getVacancies(Freshen doubleString) throws IOException {
        log.info("getVacancies language={} workplace={}", doubleString.getWorkplace(), doubleString.getLanguage());
        return getTestList();
    }

    public static List<VacancyTo> getTestList(){
        VacancyTo vacancyTo1 = new VacancyTo(null,"Junior java", "Games", "Poznan",300000, 450000, "https://grc.ua/vacancy/40006938?query=java",
                "Still haven’t found your dream job? Huuuge Games is a gaming company on a mission to build the world’s largest real-time casual gaming platform connecting…",
                LocalDate.now().minusDays(5), "https://grc.ua", null, null, false);

        VacancyTo vacancyTo2 = new VacancyTo(null,"Middle проверка vote Developer", "New Company Games", "киев",200000, 300000, "https проверка vote",
                "Still haven’t found your dream job? Huuuge Games is a gaming company on a mission to build the world’s largest real-time casual gaming platform connecting java…",
                LocalDate.now().minusDays(7), "https://ukr.net/проверка/vote", null, null, false);

        VacancyTo vacancyTo3 = new VacancyTo(null, "Java-разработчик", "New Company", "Warshaw",
                100000, 150000, "https://grc.ua/vacancy/40006938?query=java",
                "Java. Понимание javaScript",
                LocalDate.now().minusDays(3), "https://grc.ua", null, null, false);
        return List.of(vacancyTo1, vacancyTo2, vacancyTo3);
    }
}
