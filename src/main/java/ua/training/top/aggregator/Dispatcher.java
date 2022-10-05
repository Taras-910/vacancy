package ua.training.top.aggregator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.training.top.aggregator.strategies.*;

import static ua.training.top.aggregator.InstallationUtil.autoRefreshProviders;
import static ua.training.top.aggregator.InstallationUtil.testProvider;
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
                    mapStrategies.get(getKey(4)),     //0...3
                    mapStrategies.get(getKey(5) + 4), //4...8
                    mapStrategies.get(getKey(4) + 9));//9...12
        }
        else {
            return new Starter(
                    /*new Provider(new CaIndeedStrategy()),*/   /*только ca*/
                    /*new Provider(new CwJobsStrategy())*/      /*только uk*/                // не работает!!!!!!!!!!
                    new Provider(new DjinniStrategy()),       /*за_рубежем === удаленно*/
                    new Provider(new ItJobsStrategy()),       /*только ca*/
                    new Provider(new ItJobsWatchStrategy()),  /*только uk*/
                    new Provider(new JobBankStrategy()),      /*только ca - от правительства канады*/

                    new Provider(new JobsBGStrategy()),       /*только bg*/
                    new Provider(new JobsMarketStrategy()),   /*только за_рубежем USA!!!*/
                    new Provider(new JobsMarketUA()),          /*только UA !!!*/
                    new Provider(new JobsDouStrategy()),      /*полезные статьи  */
                    new Provider(new LinkedinStrategy()),     /*нет удаленно  нет salary*/

                    new Provider(new NofluffjobsStrategy()),  /*только pl*/
                    /*new Provider(new RabotaStrategy()),*/     /*мало за_рубежем Украина ??? страница: js-функция */     //!!!!!!!!!!
                    /*new Provider(new ReedStrategy())*/        /*только uk*/             // не работает!!!!!!!!!!
                    /*new Provider(new UAIndeedStrategy()),*/   /*только ua // нет salary*/
                    new Provider(new UAJoobleStrategy()),     /*меняет теги //ua, bg, ca, uk, de */
                    new Provider(new WorkStrategy()),         /*нет за_рубежем*/
                    new Provider(new ZaplataStrategy())       /*только bg*/
            );
        }
    }
}
