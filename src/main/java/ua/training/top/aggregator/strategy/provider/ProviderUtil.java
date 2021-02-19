package ua.training.top.aggregator.strategy.provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.training.top.aggregator.Provider;
import ua.training.top.aggregator.strategy.*;
import ua.training.top.repository.AggregatorRepository;

import static ua.training.top.aggregator.installation.InstallationUtil.*;
import static ua.training.top.util.ScheduledUtil.getKey;
import static ua.training.top.util.ScheduledUtil.mapStrategies;

public class ProviderUtil {
    public static final Logger log = LoggerFactory.getLogger(ProviderUtil.class);

    public static AggregatorRepository getAllProviders(){

        if (testProvider) {
            return new AggregatorRepository(new Provider(new TestStrategy()));
        }
        else if (scheduledOneProvider) {
            return new AggregatorRepository(
                    mapStrategies.get(getKey(10)));
        }
        else if (scheduledTwoProviders) {
            return new AggregatorRepository(
                    mapStrategies.get(getKey(5)),
                    mapStrategies.get(getKey(5) + 5));
        }
        else if (scheduledFourProviders) {
            int key1 = getKey(5), key2 = getKey(5), key3 = getKey(5) + 5, key4 = getKey(5) + 5;
            while(key2 == key1) {
                key2 = getKey(5);
            }
            while(key4 == key3) {
                key4 = getKey(5) + 5;
            }
            return new AggregatorRepository(
                    mapStrategies.get(key1),
                    mapStrategies.get(key2),
                    mapStrategies.get(key3),
                    mapStrategies.get(key4));
        }
        else {
            return new AggregatorRepository(
                    new Provider(new DjinniStrategy()),
                    new Provider(new GrcStrategy()),          /*нет за_рубежем, меняет salary*/
                    new Provider(new HabrStrategy()),         /*проблемный*/
                    new Provider(new JobsStrategy()),
                    new Provider(new LinkedinStrategy()),
                    new Provider(new NofluffjobsStrategy()),
                    new Provider(new RabotaStrategy()),       /*всего 2 за_рубежем*/
                    new Provider(new UAIndeedStrategy()),     /*нет за_рубежем*/
                    new Provider(new UAJoobleStrategy()),     /*меняет теги*/
                    new Provider(new WorkStrategy())          /*нет за_рубежем*/
            );
        }
    }
}

//        https://app.headz.io/candidates/new
//        https://distillery.com/careers/senior-backend-developer-java-tg/
