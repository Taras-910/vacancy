package ua.training.top.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ua.training.top.model.Freshen;

import static ua.training.top.SecurityUtil.setTestAuthorizedUser;
import static ua.training.top.aggregator.installation.InstallationUtil.*;
import static ua.training.top.util.FreshenUtil.schedulingFreshen;
import static ua.training.top.util.ScheduledUtil.*;
import static ua.training.top.util.UserUtil.asAdmin;

@Component
@EnableScheduling
public class AutoScheduledFreshen {
    public static final Logger log = LoggerFactory.getLogger(AutoScheduledFreshen.class);

    @Autowired
    private FreshenService service;

    @Scheduled(cron = "0 0 4,7,10,13,16 ? * *")
    public void scheduleWorkplaceKyiv() {
        log.info("scheduleWorkplaceKyiv");
        upgrade("киев");
    }

    @Scheduled(cron = "0 0 5,8,11,14,17 ? * *")
    public void scheduleWorkplaceRemote() {
        log.info("scheduleWorkplaceRemote");
        upgrade("удаленно");
    }

    @Scheduled(cron = "0 0 6,9,12,15,18 ? * *")
    public void scheduleWorkplaceForeign() {
        log.info("scheduleWorkplaceForeign");
        upgrade("за_рубежем");
    }

    private void upgrade(String workplace) {
        int delayMinutes = 19;
        setRandomDelay(1000 * 60 * delayMinutes);
        setTestAuthorizedUser(asAdmin());
        setFourProviders();
        service.refreshDB(new Freshen(schedulingFreshen("киев")));
        offFourProviders();
    }

//    @Scheduled(cron = "0 0,5,10,15,20,25,30,35,40,45,50,55 7-23 ? * *")
    @Scheduled(cron = "0 20,40 7-15 ? * *")
    public void someTimesByHour() {
        log.info("someTimesByHour");
        int delayMinutes = 19;
        setRandomDelay(1000 * 60 * delayMinutes);
        setTestAuthorizedUser(asAdmin());
        setOneProvider();
        service.refreshDB(new Freshen(schedulingFreshen(mapWorkplace.get(getKey(10)))));
        offOneProvider();
    }
}
