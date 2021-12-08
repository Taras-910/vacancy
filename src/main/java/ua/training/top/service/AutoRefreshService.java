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
    private AggregatorService aggregatorService;

//    @Scheduled(cron = "0 0,5,10,15,20,25,30,35,40,45,50,55 6-23 * * *")
    @Scheduled(cron = "0 0,10,20,30,40,50 10-18 * * MON-FRI")
    public void weekDay() {
//        int delayWithinMinutes = 4;
        int delayWithinMinutes = 9;
        log.info(MessageUtil.delay, delayWithinMinutes);
        setRandomDelay(1000 * 60 * delayWithinMinutes);
        setTestAuthorizedUser(asAdmin());
        setAutoRefreshProviders();
        aggregatorService.refreshDB(
                new Freshen(randomFreshen(mapWorkplace.get(getKey(10)), mapLevel.get(getKey(4)))));
        offAutoRefreshProviders();
    }

    @Scheduled(cron = "0 0,20,40 11-17 * * SAT")
    public void weekEnd() {
        int delayMinutesMax = 19;
        log.info("someTimesByHour delayMinutesMax={}", delayMinutesMax);
        setRandomDelay(1000 * 60 * delayMinutesMax);
        setTestAuthorizedUser(asAdmin());
        setAutoRefreshProviders();
        aggregatorService.refreshDB(
                new Freshen(randomFreshen(mapWorkplace.get(getKey(5)), mapLevel.get(getKey(2)))));
        offAutoRefreshProviders();
    }

    @Scheduled(cron = "0 55 9 * * MON-SAT")
    public void everyDay() {
        log.info("Scheduled everyDay");
        setTestAuthorizedUser(asAdmin());
        aggregatorService.deleteOutDated();
    }
}
//                                   *      *
//  djinni*12 grc*20 habr*25 jobMar jobs linked nof rab*40 indeed joble work jobcareer total
//all     100   40     20    10     14   2х14    4    25   25*20 10*20   27    2        291
//Украина   6    6      -     -     14   2х14    -     6    25    2*14*  27    2        128
//foreign 120    1      1    10     14   2х14    4     1     -    1*14*   3    -
//Киев    100    4     20     -      1      2    -    12    25    22     13    2
//remote  100   33     13    10      1      3    4     9     7    13x20   9    -
//Минск   100    6     20     -      1      2    -     1     -     2      3    2
//Львов    40    -      -     -      1      2    -     3          20      3    1
//Харьков  46    2      -     -      1      2    -     4          11      5    1
//Одесса   30    -      -     -      1      2    -     2     2     6      3    1
//Санкт-Петербург20    20     -      1      3    -     -     -     -      -    4
//Москва    -   40     20     -      1      3    -     -     -     -      -    3
//                              trainee=164
// djinni address ???




