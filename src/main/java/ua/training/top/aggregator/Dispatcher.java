package ua.training.top.aggregator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.training.top.aggregator.strategy.*;

import static ua.training.top.aggregator.installation.InstallationUtil.autoRefreshProviders;
import static ua.training.top.aggregator.installation.InstallationUtil.testProvider;
import static ua.training.top.util.AutoRefreshUtil.getKey;
import static ua.training.top.util.AutoRefreshUtil.mapStrategies;

public class Dispatcher {
    public static final Logger log = LoggerFactory.getLogger(Dispatcher.class);

    public static Starter getAllProviders(){
        if (testProvider) {
            return new Starter(new Provider(new TestStrategy()));
        }
        else if (autoRefreshProviders) {
            return new Starter(
                    mapStrategies.get(getKey(3)),
                    mapStrategies.get(getKey(3) + 3),
                    mapStrategies.get(getKey(3) + 6));
        }
        else {
            return new Starter(
                    new Provider(new DjinniStrategy()),       /*за_рубежем === удаленно*/
                    new Provider(new JobsMarketStrategy()),   /*ТОЛЬКО за_рубежем USA!!!*/
                    new Provider(new JobsStrategy()),         /*полезные статьи*/

                    new Provider(new LinkedinStrategy()),     /*нет удаленно*/                  // нет salary
                    new Provider(new NofluffjobsStrategy()),  /*ТОЛЬКО за_рубежем Poland*/
                    new Provider(new RabotaStrategy()),       /*мало за_рубежем Украина */

                    new Provider(new UAIndeedStrategy()),     /*нет за_рубежем Украина ТОЛЬКО*/ // нет salary
                    new Provider(new UAJoobleStrategy()),     /*меняет теги*/
                    new Provider(new WorkStrategy())          /*нет за_рубежем*/
            );
        }
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


//https://tproger.ru/jobs/?anytime=true
//https://geekjob.ru/vacancies

//https://career.softserveinc.com/en-us/vacancies
//https://jobs.ua/vacancy/kiev/rabota-java-developer
//https://kiev.careerist.ru/jobs-java-developer/
//https://kiev.jobcareer.ru/jobs/java/?feed=
//https://app.headz.io/candidates/new
//https://edc.sale
//https://www.olx.ua
//https://www.ria.com
//https://trud.ua
//http://trudbox.com.ua/kiev/jobs-programmist
