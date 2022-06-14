package ua.training.top.aggregator.installation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.training.top.aggregator.Provider;
import ua.training.top.aggregator.strategy.Strategy;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static ua.training.top.aggregator.Starter.allProviders;

public class InstallationUtil {
    private static final Logger log = LoggerFactory.getLogger(InstallationUtil.class);
    public static int
            periodKeeping = 28,
            limitVacanciesKeeping = 4250,
            limitFreshensFilterKeeping = 200,
            limitVotesKeeping = limitVacanciesKeeping / 10,
            maxLengthText = 250,
            repeatToCall = 3,
            limitCallPages = 8;

    public static LocalDate
            reasonDateLoading = LocalDateTime.now().toLocalDate().minusDays(periodKeeping / 4 * 3),
            reasonPeriodKeeping = LocalDateTime.now().toLocalDate().minusDays(periodKeeping);

//    public static boolean testProvider = true;
    public static boolean testProvider = false;

    public static void setTestProvider() { InstallationUtil.testProvider = true; }
    public static void offTestProvider() {
        InstallationUtil.testProvider = false;
    }

    public static boolean autoRefreshProviders = false;
    public static void setAutoRefreshProviders() { InstallationUtil.autoRefreshProviders = true; }
    public static void offAutoRefreshProviders() { InstallationUtil.autoRefreshProviders = false; }

    public static void setTestReasonPeriodToKeep() {
        InstallationUtil.reasonPeriodKeeping = LocalDateTime.now().toLocalDate().minusDays(3650);
    }

    public static void reCall(int listSize, Strategy strategy){
        if (listSize == 0 && repeatToCall > 0){
            log.info("reCall attemptToCall={}", repeatToCall);
            allProviders.addLast(new Provider(strategy));
            repeatToCall--;
        }
    }
}
