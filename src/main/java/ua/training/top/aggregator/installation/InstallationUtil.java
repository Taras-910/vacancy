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

    private static int repeatToCall = 3;
    public static int limitCallPages = 10;
    public static LocalDate reasonDateToLoad = LocalDateTime.now().toLocalDate().minusDays(14);
    public static LocalDate reasonPeriodToKeep = LocalDateTime.now().toLocalDate().minusDays(49);
    public static Integer freshenPerHour = 3;
    public static Integer freshenPerHourForAdmin = 30;

    public static boolean testProvider = false;
//    public static boolean testProvider = true;

    public static void setTestProvider() {
    InstallationUtil.testProvider = true;
}

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
