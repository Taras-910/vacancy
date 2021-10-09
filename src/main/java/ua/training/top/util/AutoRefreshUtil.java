package ua.training.top.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.training.top.aggregator.Provider;
import ua.training.top.aggregator.strategy.*;
import ua.training.top.model.Freshen;

import java.util.Map;
import java.util.Random;

import static java.time.LocalDateTime.now;
import static java.util.Collections.singleton;
import static ua.training.top.model.Goal.UPGRADE;
import static ua.training.top.util.UserUtil.ADMIN_ID;

public class AutoRefreshUtil {
    private final static Logger log = LoggerFactory.getLogger(AutoRefreshUtil.class);
    public static final Random random = new Random();

    public static void setRandomDelay(int bound) {
        try {
            int delay = random.nextInt(bound);
            log.info("\n------------ delay={} min {} sec ------------", delay/(1000 * 60), delay%(1000 * 60) / 1000);
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public static int getKey(int bound) {
        return random.nextInt(bound);
    }

    public static final Map<Integer, Provider> mapStrategies =
            Map.ofEntries(
                    Map.entry(0, new Provider(new DjinniStrategy())),
                    Map.entry(1, new Provider(new GrcStrategy())),
                    Map.entry(2, new Provider(new HabrStrategy())),
                    Map.entry(3, new Provider(new JobsMarketStrategy())),
                    Map.entry(4, new Provider(new JobsStrategy())),
                    Map.entry(5, new Provider(new LinkedinStrategy())),
                    Map.entry(6, new Provider(new NofluffjobsStrategy())),
                    Map.entry(7, new Provider(new RabotaStrategy())),
                    Map.entry(8, new Provider(new UAIndeedStrategy())),
                    Map.entry(9, new Provider(new UAJoobleStrategy())),
                    Map.entry(10, new Provider(new WorkStrategy())),
                    Map.entry(11, new Provider(new YandexStrategy()))
            );

    public static final Map<Integer, String> mapWorkplace =
            Map.of(
                    0, "remote",
                    1, "foreign",
                    2, "киев",
                    3, "украина",
                    4, "харьков",
                    5, "львов",
                    6, "москва",
                    7, "санкт-петербург",
                    8, "киев",
                    9, "минск"
            );

    public static final Map<Integer, String> mapLevel =
            Map.of(
                    0, "trainee",
                    1, "middle",
                    2, "junior",
                    3, "senior",
                    4, "expert"
                    );

    public static Freshen randomFreshen(String workplace, String level) {
        return new Freshen(null, now(), "java", level, workplace, singleton(UPGRADE), ADMIN_ID);
    }
}
