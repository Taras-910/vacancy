package ua.training.top.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ua.training.top.model.Freshen;

import static ua.training.top.SecurityUtil.setTestAuthorizedUser;
import static ua.training.top.aggregator.installation.InstallationUtil.offAutoRefreshProviders;
import static ua.training.top.aggregator.installation.InstallationUtil.setAutoRefreshProviders;
import static ua.training.top.util.AutoRefreshUtil.*;
import static ua.training.top.util.UserUtil.asAdmin;

@Component
@EnableScheduling
@EnableAsync
public class AutoRefreshService {
    public static final Logger log = LoggerFactory.getLogger(AutoRefreshService.class);

    @Autowired
    private FreshenService service;

//    @Scheduled(cron = "0 0,5,10,15,20,25,30,35,40,45,50,55 6-23 * * *")
//    @Scheduled(cron = "0 0,10,20,30,40,50 6-20 * * MON-SAT")
    @Scheduled(cron = "0 0,15,30,45 6-20 * * MON-SAT")
    public void weekDay() {
//        int delayWithinMinutes = 4;
//        int delayWithinMinutes = 9;
        int delayWithinMinutes = 14;
        log.info("someTimesByHour delayWithinMinutes={}", delayWithinMinutes);
        setRandomDelay(1000 * 60 * delayWithinMinutes);
        setTestAuthorizedUser(asAdmin());
        setAutoRefreshProviders();
        String level = mapLevel.get(getKey(2));
        service.refreshDB(new Freshen(randomFreshen(mapWorkplace.get(getKey(level.equals("trainee") ? 4 : 10)), level)));
        offAutoRefreshProviders();
    }

    @Scheduled(cron = "0 0 9,11,13,15,17 * * SUN")
    public void weekEnd() {
        int delayMinutesMax = 120;
        log.info("someTimesByHour delayMinutesMax={}", delayMinutesMax);
        setRandomDelay(1000 * 60 * delayMinutesMax);
        setTestAuthorizedUser(asAdmin());
        service.refreshDB(new Freshen(randomFreshen(mapWorkplace.get(getKey(4)), mapLevel.get(1))));
    }
}
