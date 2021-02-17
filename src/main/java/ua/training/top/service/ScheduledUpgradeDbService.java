package ua.training.top.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ua.training.top.model.Freshen;

import java.time.LocalDateTime;
import java.util.Random;

import static ua.training.top.SecurityUtil.setTestAuthorizedUser;
import static ua.training.top.util.FreshenUtil.schedulingFreshen;
import static ua.training.top.util.UserUtil.asAdmin;

@Component
@EnableScheduling
public class ScheduledUpgradeDbService {
    public static final Logger log = LoggerFactory.getLogger(ScheduledUpgradeDbService.class);
    @Autowired
    public static FreshenService freshenService;
    public static final Random rand = new Random();

    @Scheduled(cron = "0 0 4,10,16 * * *")
    public void scheduleWorkplaceKyiv() {
        LocalDateTime start = LocalDateTime.now();
        setDelay();
        log.info("scheduleWorkplaceKyiv delayMinutes={}", start.getMinute() - LocalDateTime.now().getMinute());
        setTestAuthorizedUser(asAdmin());
        freshenService.refreshDB(new Freshen(schedulingFreshen("киев")));
    }

    @Scheduled(cron = "0 0 6,12,18 * * *")
    public void scheduleWorkplaceForeign() {
        LocalDateTime start = LocalDateTime.now();
        setDelay();
        log.info("scheduleWorkplaceForeign delayMinutes={}", start.getMinute() - LocalDateTime.now().getMinute());
        setTestAuthorizedUser(asAdmin());
        freshenService.refreshDB(new Freshen(schedulingFreshen("за_рубежем")));
    }

    @Scheduled(cron = "0 0 8,14,20 * * *")
    public void scheduleWorkplaceRemote() {
        LocalDateTime start = LocalDateTime.now();
        setDelay();
        log.info("scheduleWorkplaceRemote delayMinutes={}", start.getMinute() - LocalDateTime.now().getMinute());
        setTestAuthorizedUser(asAdmin());
        freshenService.refreshDB(new Freshen(schedulingFreshen("удаленно")));
    }

    /*@Scheduled(cron = "0,10,20,30,40,50 * * * * *")
    public void scheduleTest() {
        log.info("\n===================================================================\n");
        log.info("scheduleTest before delay {} ", LocalDateTime.now());
        try {
            Thread.sleep(1000 * (rand.nextInt(60)));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        log.info("scheduleTest after delay {} ", LocalDateTime.now());
    }*/

    private void setDelay() {
        try {
            Thread.sleep(1000 * 60 * (rand.nextInt(60)));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
