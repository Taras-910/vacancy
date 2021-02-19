package ua.training.top.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
public class AutoScheduledFreshen {
    public static final Logger log = LoggerFactory.getLogger(AutoScheduledFreshen.class);

    @Autowired
    private FreshenService service;

    @Scheduled(cron = "0 0,15,30,45 6-18 ? * *")
    public void someTimesByHour() {
        int delayMAxMinutes = 14;
        log.info("someTimesByHour delayMAxMinutes={}", delayMAxMinutes);
        setRandomDelay(1000 * 60 * delayMAxMinutes);
        setTestAuthorizedUser(asAdmin());
        setTwoProviders();
        service.refreshDB(new Freshen(scheduledFreshen(mapWorkplace.get(getKey(10)))));
        offTwoProviders();
    }
}
