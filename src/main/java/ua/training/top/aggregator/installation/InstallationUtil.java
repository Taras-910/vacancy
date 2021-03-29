package ua.training.top.aggregator.installation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.training.top.aggregator.Provider;
import ua.training.top.aggregator.strategy.Strategy;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static ua.training.top.repository.AggregatorRepository.allProviders;

public class InstallationUtil {
    private static final Logger log = LoggerFactory.getLogger(InstallationUtil.class);
    public static final float HRN_TO_USD_RATE = 28.5f;
    public static final float PLN_TO_USD_RATE = 3.7f;
    public static final float EUR_TO_USD_RATE = 1.2f;
    public static final float GBP_TO_USD_RATE = 1.4f;
    public static final float RUB_TO_USD_RATE = 76.9f;

    private static int repeatToCall = 3;
    public static int limitCallPages = 10;
//    private static int repeatToCall = 1;
//    public static int limitCallPages = 2;
    public static LocalDate reasonDateToLoad = LocalDateTime.now().toLocalDate().minusDays(14);
    public static LocalDate reasonPeriodToKeep = LocalDateTime.now().toLocalDate().minusDays(21);
    public static int limitVacanciesToKeep = 5001;

    public static double freshenPerHour = 6;
    public static double freshenPerHourForAdmin = 12;
    public static double limitPerHourForAuth = 10;
    public static double limitPerHourForAdmin = 15;

//    public static boolean testProvider = true;
    public static boolean testProvider = false;

    public static void setTestProvider() { InstallationUtil.testProvider = true; }
    public static void offTestProvider() {
        InstallationUtil.testProvider = false;
    }

    public static boolean scheduledTwoProviders = false;
    public static void setTwoProviders() { InstallationUtil.scheduledTwoProviders = true; }
    public static void offTwoProviders() { InstallationUtil.scheduledTwoProviders = false; }

    public static void setTestFreshenPerHour() {
        InstallationUtil.freshenPerHour = 60;
    }
    public static void setTestReasonPeriodToKeep() {
        InstallationUtil.reasonPeriodToKeep = LocalDateTime.now().toLocalDate().minusDays(1265);
    }

    public static void reCall(int listSize, Strategy strategy){
        if (listSize == 0 && repeatToCall > 0){
            log.info("reCall attemptToCall={}", repeatToCall);
            allProviders.addLast(new Provider(strategy));
            repeatToCall--;
        }
    }
}
