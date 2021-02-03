package ua.training.top.aggregator.strategy.provider;

import ua.training.top.aggregator.Provider;
import ua.training.top.aggregator.strategy.*;
import ua.training.top.repository.AggregatorRepository;

import static ua.training.top.aggregator.strategy.installation.InstallationUtil.testProvider;

public class ProviderUtil {
    public static AggregatorRepository getAllProviders(){
        if(testProvider) {
            return new AggregatorRepository(
                    new Provider(new TestStrategy()));
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
