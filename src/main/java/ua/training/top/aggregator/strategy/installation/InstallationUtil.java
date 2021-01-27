package ua.training.top.aggregator.strategy.installation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.training.top.aggregator.Provider;
import ua.training.top.aggregator.strategy.Strategy;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static ua.training.top.aggregator.AggregatorService.allProviders;

public class InstallationUtil {
    private static Logger log = LoggerFactory.getLogger(InstallationUtil.class);
    private static int repeatToCall = 3;
    public static int limitCallPages = 10;
    public static LocalDate reasonDateToLoad = LocalDateTime.now().toLocalDate().minusDays(14);
    public static LocalDate reasonDateToKeep = LocalDateTime.now().toLocalDate().minusDays(28);
    public static Integer reasonTimeBetweenRefresh = 30;

    public static boolean jUnitTest = true;
    public static void setJUnitTest() {
        InstallationUtil.jUnitTest = true;
    }
    public static void dropJUnitTest() {
        InstallationUtil.jUnitTest = false;
    }

    public static void reCall(int listSize, Strategy strategy){
        if (listSize == 0 && repeatToCall > 0){
            log.info("reCall attemptToCall={}", repeatToCall);
            allProviders.addLast(new Provider(strategy));
            repeatToCall--;
        }
    }
}
