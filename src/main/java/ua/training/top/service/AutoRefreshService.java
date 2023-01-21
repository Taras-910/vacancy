package ua.training.top.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ua.training.top.model.Freshen;
import ua.training.top.util.MessageUtil;

import static ua.training.top.SecurityUtil.setTestAuthorizedUser;
import static ua.training.top.aggregator.InstallationUtil.offAutoRefreshProviders;
import static ua.training.top.aggregator.InstallationUtil.setAutoRefreshProviders;
import static ua.training.top.util.AutoRefreshUtil.*;
import static ua.training.top.util.UserUtil.asAdmin;

@Component
@EnableScheduling
@EnableAsync
public class AutoRefreshService {
    public static final Logger log = LoggerFactory.getLogger(AutoRefreshService.class);

    @Autowired
    private AggregatorService aggregatorService;

    //    @Scheduled(cron = "0 0,5,10,15,20,25,30,35,40,45,50,55 6-23 * * *")
    @Scheduled(cron = "0 0,10,20,30,40,50 10-18 * * MON-FRI")
    public void workDay() {
//        int delayWithinMinutes = 4;
        int delayWithinMinutes = 9;
        Freshen freshen = new Freshen(randomFreshen(
                mapLanguage.get(getKey(4)),
                mapLevel.get(getKey(4)),
                mapWorkplace.get(getKey(8))
        ));
        log.info(MessageUtil.delay_week_day_freshen, delayWithinMinutes, freshen);
        setRandomDelay(1000 * 60 * delayWithinMinutes);
        setTestAuthorizedUser(asAdmin());
        setAutoRefreshProviders();
        aggregatorService.refreshDB(freshen);
        offAutoRefreshProviders();
    }

    @Scheduled(cron = "0 0,20,40 11-17 * * SAT")
    public void weekEnd() {
        int delayMinutesMax = 19;
        log.info(MessageUtil.delay, delayMinutesMax);
        setRandomDelay(1000 * 60 * delayMinutesMax);
        setTestAuthorizedUser(asAdmin());
        setAutoRefreshProviders();
        aggregatorService.refreshDB(new Freshen(randomFreshen(
                mapLanguage.get(getKey(2)),
                mapLevel.get(getKey(6)),
                mapWorkplace.get(getKey(8))
                )));
        offAutoRefreshProviders();
    }

    @Scheduled(cron = "0 55 9 * * MON-SAT")
    public void everyDay() {
        log.info("Scheduled everyDay");
        setTestAuthorizedUser(asAdmin());
        aggregatorService.deleteOutDated();
    }

    @Scheduled(cron = "0 0 10 * * MON,THU")
    public void twiceInWeek() {
        log.info("Scheduled twiceByWeek");
        int delayWithinMinutes = 480; // 8 hours
        log.info(MessageUtil.delay, delayWithinMinutes);
        setRandomDelay(1000 * 60 * delayWithinMinutes);
        setTestAuthorizedUser(asAdmin());
        aggregatorService.updateRateDB();
    }
}
