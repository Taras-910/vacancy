package ua.training.top.aggregator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.training.top.aggregator.strategies.Strategy;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static ua.training.top.aggregator.Provider.getRates;
import static ua.training.top.aggregator.Starter.allProviders;
import static ua.training.top.service.AggregatorService.herokuRestriction;
import static ua.training.top.util.aggregatorUtil.data.ConstantsUtil.recall;

public class InstallationUtil {
    private static final Logger log = LoggerFactory.getLogger(InstallationUtil.class);
    public static final String baseCurrency = "usd";
    public static final int
            periodRatesKeeping = 7,
            periodVacanciesKeeping = 28,
            limitVacanciesKeeping = 4250,
            limitFreshensFilterKeeping = 200,
            limitVotesKeeping = limitVacanciesKeeping / 10,
            maxLengthText = 250,
            limitCallPages = herokuRestriction ? 1 : 2;
    public static int repeatToCall = 3;

    public static final LocalDate reasonDateLoading = LocalDateTime.now().toLocalDate().minusDays(periodVacanciesKeeping / 2);
    public static LocalDate reasonPeriodKeeping = LocalDateTime.now().toLocalDate().minusDays(periodVacanciesKeeping);
    public static final LocalDate reasonValidRate = LocalDateTime.now().toLocalDate().minusDays(periodRatesKeeping);

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
            log.info(recall, repeatToCall);
            allProviders.addLast(new Provider(strategy));
            repeatToCall--;
        }
    }

    public static void reCallRate(int size) {
        if (size == 0 && repeatToCall > 0){
            log.info(recall, repeatToCall);
            repeatToCall--;
            getRates();
        }
    }

}
