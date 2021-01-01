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
    public static int limitCallPages = 1;
    private static int repeatToCall = 3;
    public static LocalDate reasonToLoadDate = LocalDateTime.now().toLocalDate().minusDays(7);
    public static LocalDate reasonToKeepDate = LocalDateTime.now().toLocalDate().minusDays(21);

    public static void reCall(int listSize, Strategy strategy){
        if (listSize == 0 && repeatToCall > 0){
            log.info("reCall attemptToCall={}", repeatToCall);
            allProviders.addLast(new Provider(strategy));
            repeatToCall--;
        }
    }

}
