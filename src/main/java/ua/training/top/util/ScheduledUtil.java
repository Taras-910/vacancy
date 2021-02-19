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
            log.info("\n------- delay={} min {} sec -------", delay/(1000 * 60), delay%(1000 * 60) / 1000);
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public static int getKey(int bound) {
        return rand.nextInt(bound);
    }

    public static final Map<Integer, Provider> mapStrategies =
            Map.of(0, new Provider(new DjinniStrategy()),
                    1, new Provider(new GrcStrategy()),
                    2, new Provider(new HabrStrategy()),
                    3, new Provider(new JobsStrategy()),
                    4, new Provider(new LinkedinStrategy()),
                    5, new Provider(new NofluffjobsStrategy()),
                    6, new Provider(new RabotaStrategy()),
                    7, new Provider(new UAIndeedStrategy()),
                    8, new Provider(new UAJoobleStrategy()),
                    9, new Provider(new WorkStrategy()));

    public static final Map<Integer, String> mapWorkplace =
            Map.of(0, "днепр",
                    1, "харьков",
                    2, "львов",
                    3, "санкт-петербург",
                    4, "киев",
                    5, "удаленно",
                    6, "за_рубежем",
                    7, "москва",
                    8, "минск",
                    9, "варшава");

}
