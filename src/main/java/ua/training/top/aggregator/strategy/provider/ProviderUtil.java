package ua.training.top.aggregator.strategy.provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.training.top.aggregator.Provider;
import ua.training.top.aggregator.strategy.*;
import ua.training.top.repository.AggregatorRepository;

import static ua.training.top.aggregator.installation.InstallationUtil.autoRefreshProviders;
import static ua.training.top.aggregator.installation.InstallationUtil.testProvider;
import static ua.training.top.util.AutoRefreshUtil.getKey;
import static ua.training.top.util.AutoRefreshUtil.mapStrategies;

public class ProviderUtil {
    public static final Logger log = LoggerFactory.getLogger(ProviderUtil.class);

    public static AggregatorRepository getAllProviders(){
        if (testProvider) {
            return new AggregatorRepository(new Provider(new TestStrategy()));
        }
        else if (autoRefreshProviders) {
            log.info("autoRefreshProviders");
            return new AggregatorRepository(
                    mapStrategies.get(getKey(4)),
                    mapStrategies.get(getKey(4) + 4),
                    mapStrategies.get(getKey(4) + 8));
        }
        else {
            log.info("allProviders");
            return new AggregatorRepository(
                    new Provider(new DjinniStrategy()),       /*за_рубежем === удаленно*/
                    new Provider(new GrcStrategy()),          /*нет за_рубежем, меняет salary*/
                    new Provider(new HabrStrategy()),         /*нет за_рубежем*/
                    new Provider(new JobsMarketStrategy()),   /*ТОЛЬКО за_рубежем USA!!!*/

                    new Provider(new JobsStrategy()),         /*полезные статьи*/
                    new Provider(new LinkedinStrategy()),     /*нет удаленно*/
                    new Provider(new NofluffjobsStrategy()),  /*ТОЛЬКО за_рубежем Poland*/
                    new Provider(new RabotaStrategy()),       /*мало за_рубежем Украина */

                    new Provider(new UAIndeedStrategy()),     /*нет за_рубежем Украина ТОЛЬКО*/
                    new Provider(new UAJoobleStrategy()),     /*меняет теги*/
                    new Provider(new WorkStrategy()),         /*нет за_рубежем*/
                    new Provider(new YandexStrategy())        /*нет за_рубежем*/
            );
        }
    }
}
//https://career.softserveinc.com/en-us/vacancies


//https://jobs.ua/vacancy/kiev/rabota-java-developer
//https://kiev.careerist.ru/jobs-java-developer/
//https://kiev.jobcareer.ru/jobs/java/?feed=
//https://app.headz.io/candidates/new
//https://distillery.com/careers/senior-backend-developer-java-tg/
//https://edc.sale
//https://www.olx.ua
//https://www.ria.com
//https://trud.ua
//http://trudbox.com.ua/kiev/jobs-programmist
