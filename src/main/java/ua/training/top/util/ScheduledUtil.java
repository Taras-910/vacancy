package ua.training.top.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.training.top.aggregator.Provider;
import ua.training.top.aggregator.strategy.*;

import java.util.Map;
import java.util.Random;

public class ScheduledUtil {
    private final static Logger log = LoggerFactory.getLogger(ScheduledUtil.class);
    public static final Random rand = new Random();

    public static void setRandomDelay(int bound) {
        try {
            int delay = rand.nextInt(bound);
            log.info("\n------------ delay={} min {} sec ------------", delay/(1000 * 60), delay%(1000 * 60) / 1000);
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public static int getKey(int bound) {
        return rand.nextInt(bound);
    }

    public static final Map<Integer, Provider> mapStrategies =
            Map.ofEntries(
                    Map.entry(0, new Provider(new GrcStrategy())),
                    Map.entry(1, new Provider(new HabrStrategy())),
                    Map.entry(2, new Provider(new RabotaStrategy())),
                    Map.entry(3, new Provider(new UAIndeedStrategy())),
                    Map.entry(4, new Provider(new WorkStrategy())),
                    Map.entry(5, new Provider(new YandexStrategy())),
                    Map.entry(6, new Provider(new JobsMarketStrategy())),
                    Map.entry(7, new Provider(new JobsStrategy())),
                    Map.entry(8, new Provider(new DjinniStrategy())),
                    Map.entry(9, new Provider(new LinkedinStrategy())),
                    Map.entry(10, new Provider(new NofluffjobsStrategy())),
                    Map.entry(11, new Provider(new UAJoobleStrategy()))
            );

    public static final Map<Integer, String> mapWorkplace =
            Map.of(
                    0, "удаленно",
                    1, "за_рубежем",
                    2, "киев",
                    3, "харьков",
                    4, "львов",
                    5, "днепр",
                    6, "москва",
                    7, "санкт-петербург",
                    8, "киев",
                    9, "минск"
            );
}
