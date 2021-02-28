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
import static ua.training.top.aggregator.installation.InstallationUtil.offTwoProviders;
import static ua.training.top.aggregator.installation.InstallationUtil.setTwoProviders;
import static ua.training.top.util.FreshenUtil.scheduledFreshen;
import static ua.training.top.util.ScheduledUtil.*;
import static ua.training.top.util.UserUtil.asAdmin;

@Component
@EnableScheduling
@EnableAsync
public class AutoScheduledFreshen {
    public static final Logger log = LoggerFactory.getLogger(AutoScheduledFreshen.class);

    @Autowired
    private FreshenService service;

    @Scheduled(cron = "0 0,15,30,45 6-18 * * MON-SAT")
//    @Scheduled(cron = "0 0,5,10,15,20,25,30,35,40,45,50,55 6-23 * * *")
    public void weekDay() {
        int delayMinutesMax = 14;
//        int delayMinutesMax = 4;
        log.info("someTimesByHour delayMinutesMax={}", delayMinutesMax);
        setRandomDelay(1000 * 60 * delayMinutesMax);
        setTestAuthorizedUser(asAdmin());
        setTwoProviders();
        service.refreshDB(new Freshen(scheduledFreshen(mapWorkplace.get(getKey(10)))));
        offTwoProviders();
    }

    @Scheduled(cron = "0 0 9,14 * * SUN")
    public void weekEnd() {
        int delayMinutesMax = 120;
        log.info("someTimesByHour delayMinutesMax={}", delayMinutesMax);
        setRandomDelay(1000 * 60 * delayMinutesMax);
        setTestAuthorizedUser(asAdmin());
        service.refreshDB(new Freshen(scheduledFreshen(mapWorkplace.get(getKey(3)))));
    }
}
