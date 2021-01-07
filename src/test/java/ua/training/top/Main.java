package ua.training.top;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ua.training.top.service.VacancyService;
import ua.training.top.service.VoteService;

import static ua.training.top.util.VacancyUtil.getTos;

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

//        EmployerService employerService = appCtx.getBean(EmployerService.class);
//        VoteService voteService = appCtx.getBean(VoteService.class);
//        System.out.println("========================================");
//        System.out.println(employerService.getAll().size());
//        System.out.println(voteService.getAll().size());
//        System.out.println("------------------------------------------------------------------------------");
//        employerService.delete(100025);
//        System.out.println(employerService.getAll().size());
//        System.out.println(voteService.getAll().size());
//        System.out.println("========================================");
//        AggregatorController aggregatorController = appCtx.getBean(AggregatorController.class);
//        aggregatorController.refreshDB(new DoubleTo("java", "киев"));
//        VacancyRepository vacancyRepository = appCtx.getBean(VacancyRepository.class);
        VacancyService vacancyService = appCtx.getBean(VacancyService.class);
        VoteService voteService = appCtx.getBean(VoteService.class);
/*
        VacancyUIController vacancyUIController = appCtx.getBean(VacancyUIController.class);
        EmployerService employerService = appCtx.getBean(EmployerService.class);
        DoubleTo doubleString = new DoubleTo("java", "киев");
        EmployerRepository employerRepository = appCtx.getBean(EmployerRepository.class);
*/

        System.out.println(getTos(vacancyService.getAll(), voteService.getAllForAuthUser()));




/*
        System.out.println("------------------------------------------------------------------------------");
        System.out.println("before="+ vacancyUIController.getByFilter("java", "за_рубежем"));
        System.out.println("==============================================================================");
*/
//        vacancyService.deleteBeforeDate(reasonToKeepDate);
//        System.out.println("------------------------------------------------------------------------------");
//        System.out.println("after="+ vacancyService.getAll().size());
//        System.out.println("==============================================================================");



        appCtx.close();

    }
}
