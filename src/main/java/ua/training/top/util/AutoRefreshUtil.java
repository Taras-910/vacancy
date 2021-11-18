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
import static ua.training.top.util.MessageUtil.setting_delay;
import static ua.training.top.util.UserUtil.ADMIN_ID;

public class AutoRefreshUtil {
    private final static Logger log = LoggerFactory.getLogger(AutoRefreshUtil.class);
    public static final Random random = new Random();

    public static void setRandomDelay(int bound) {
        try {
            int delay = random.nextInt(bound);
            log.info(setting_delay, delay/(1000 * 60), delay%(1000 * 60) / 1000);
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
                    Map.entry(3, new Provider(new JobCareerStrategy())),
                    Map.entry(4, new Provider(new JobsMarketStrategy())),
                    Map.entry(5, new Provider(new JobsStrategy())),
                    Map.entry(6, new Provider(new LinkedinStrategy())),
                    Map.entry(7, new Provider(new NofluffjobsStrategy())),
                    Map.entry(8, new Provider(new RabotaStrategy())),
                    Map.entry(9, new Provider(new UAIndeedStrategy())),
                    Map.entry(10, new Provider(new UAJoobleStrategy())),
                    Map.entry(11, new Provider(new WorkStrategy()))
            );

    public static final Map<Integer, String> mapWorkplace =
            Map.of(
                    0, "remote",
                    1, "киев",
                    2, "украина",
                    3, "foreign",
                    4, "all",
                    5, "харьков",
                    6, "львов",
                    7, "москва",
                    8, "санкт-петербург",
                    9, "минск"
            );

    public static final Map<Integer, String> mapLevel =
            Map.of(
                    0, "all",
                    1, "middle",
                    2, "senior",
                    3, "trainee",
                    4, "expert",
                    5, "junior"
                    );

    public static Freshen randomFreshen(String workplace, String level) {
        return new Freshen(null, now(), "java", level, workplace, singleton(UPGRADE), ADMIN_ID);
    }
}
