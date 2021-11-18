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
                    mapStrategies.get(getKey(4)),
                    mapStrategies.get(getKey(4) + 4),
                    mapStrategies.get(getKey(4) + 8));
        }
        else {
            return new Starter(
                    new Provider(new DjinniStrategy()),       /*за_рубежем === удаленно*/
                    new Provider(new GrcStrategy()),          /*нет за_рубежем, меняет salary*/
                    new Provider(new HabrStrategy()),         /*нет за_рубежем*/
                    new Provider(new JobCareerStrategy()),    /* мало UA RU BL*/
                    new Provider(new JobsMarketStrategy()),   /*ТОЛЬКО за_рубежем USA!!!*/

                    new Provider(new JobsStrategy()),         /*полезные статьи*/
                    new Provider(new LinkedinStrategy()),     /*нет удаленно*/                  // нет salary
                    new Provider(new NofluffjobsStrategy()),  /*ТОЛЬКО за_рубежем Poland*/
                    new Provider(new RabotaStrategy()),       /*мало за_рубежем Украина */

                    new Provider(new UAIndeedStrategy()),     /*нет за_рубежем Украина ТОЛЬКО*/ // нет salary
                    new Provider(new UAJoobleStrategy()),     /*меняет теги*/
                    new Provider(new WorkStrategy())         /*нет за_рубежем*/
            );
        }
    }
}
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
