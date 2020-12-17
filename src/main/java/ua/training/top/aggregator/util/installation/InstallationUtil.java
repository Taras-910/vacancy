package ua.training.top.aggregator.util.installation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.training.top.aggregator.Provider;
import ua.training.top.aggregator.strategy.Strategy;

import java.time.LocalDateTime;

import static ua.training.top.aggregator.AggregatorService.allProviders;

public class InstallationUtil {
    private static Logger log = LoggerFactory.getLogger(InstallationUtil.class);
    public static int limitCallPages = 10;
    private static int repeatToCall = 3;
    public static LocalDateTime validReleaseDate = LocalDateTime.now().minusDays(7);

    public static void reCall(int listSize, Strategy strategy){
        if (listSize == 0 && repeatToCall > 0){
            log.info("reCall attemptToCall={}", repeatToCall);
            allProviders.addLast(new Provider(strategy));
            repeatToCall--;
        }
    }

}
