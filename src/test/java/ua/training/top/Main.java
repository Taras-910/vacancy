package ua.training.top;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ua.training.top.service.EmployerService;
import ua.training.top.service.VacancyService;
import ua.training.top.to.DoubleString;

import static ua.training.top.aggregator.strategy.installation.InstallationUtil.reasonToKeepDate;

public class Main {
    public static void main(String[] args) {

        // safe from xss
        /*String unsafe =
                "<p><a href='http://example.com/' onclick='stealCookies()'>Link</a></p>";
        String safe = Jsoup.clean(unsafe, Whitelist.basic());
        // now: <p><a href="http://example.com/" rel="nofollow">Link</a></p>
        System.out.println(safe);*/


        ConfigurableApplicationContext appCtx =
                new ClassPathXmlApplicationContext(new String[]{"spring/spring-app.xml", "spring/spring-db.xml"}, false);
        appCtx.refresh();
/*
        System.out.println("Bean definition names: ");
        System.out.println("========================================");
        for(String s : appCtx.getBeanDefinitionNames()) {
            System.out.println(s);
        }
        System.out.println("========================================");
*/

/*
        VacancyRepository vacancyRepository = appCtx.getBean(VacancyRepository.class);

        System.out.println(vacancyRepository.getAll());
        System.out.println("========================================");
        System.out.println(VACANCY2);
*/

/*
        EmployerService employerService = appCtx.getBean(EmployerService.class);
        System.out.println("========================================");
        System.out.println(employerService.getAll());
        System.out.println("========================================");
*/
//        AggregatorController aggregatorController = appCtx.getBean(AggregatorController.class);
//        aggregatorController.refreshDB(new DoubleString("java", "киев"));
        VacancyService vacancyService = appCtx.getBean(VacancyService.class);
        EmployerService employerService = appCtx.getBean(EmployerService.class);
        DoubleString doubleString = new DoubleString("java", "киев");


//        System.out.println("------------------------------------------------------------------------------");
//        System.out.println("before="+ vacancyService.getAll().size());
//        System.out.println("==============================================================================");
        vacancyService.deleteBeforeDate(reasonToKeepDate);
        System.out.println("------------------------------------------------------------------------------");
        System.out.println("after="+ vacancyService.getAll().size());
        System.out.println("==============================================================================");



        appCtx.close();

    }
}
