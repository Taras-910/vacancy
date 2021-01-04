package ua.training.top.aggregator.strategy.provider;

import ua.training.top.aggregator.AggregatorService;
import ua.training.top.aggregator.Provider;
import ua.training.top.aggregator.strategy.*;

public class ProviderUtil {
    public static AggregatorService getAllProviders(){
        return new AggregatorService(
                new Provider(new DjinniStrategy()),
                new Provider(new GrcStrategy()),          // нет за_рубежем
                new Provider(new HabrStrategy()),         // проблемный
                new Provider(new JobsStrategy()),
                new Provider(new LinkedinStrategy()),
                new Provider(new RabotaStrategy()),       // 2 за_рубежем
                new Provider(new UAIndeedStrategy()),     // нет за_рубежем
                new Provider(new UAJoobleStrategy()),     // проблемный меняет теги
                new Provider(new WorkStrategy())          // по запросу за_рубежем нет
               /* new Provider(new TestStrategy())*/
        );
    }
}
//https://nofluffjobs.com/jobs/warszawa/java?criteria=city%3Dwroclaw
//        https://app.headz.io/candidates/new
