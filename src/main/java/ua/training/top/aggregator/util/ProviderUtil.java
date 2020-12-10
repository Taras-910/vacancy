package ua.training.top.aggregator.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.training.top.aggregator.AggregatorService;
import ua.training.top.aggregator.Provider;
import ua.training.top.aggregator.strategy.*;

import static ua.training.top.aggregator.AggregatorService.allProviders;

public class ProviderUtil {
    private static Logger log = LoggerFactory.getLogger(ProviderUtil.class);
    public static int limitCallPages = 1;
    private static int repeatToCall = 3;

    public static AggregatorService getAllProviders(){
        return new AggregatorService(
                new Provider(new GrcStrategy()),
                new Provider(new HabrStrategy()),
                new Provider(new JobsStrategy()),
                new Provider(new RabotaStrategy()),
                new Provider(new UAIndeedStrategy()),  //
                new Provider(new UAJoobleStrategy()),  //
                new Provider(new WorkStrategy())
        );
    }

    public static void reCall(int listSize, Strategy strategy){
        if (listSize == 0 && repeatToCall > 0){
            log.info("reCall attemptToCall={}", repeatToCall);
            allProviders.addLast(new Provider(strategy));
            repeatToCall--;
        }
    }
}
