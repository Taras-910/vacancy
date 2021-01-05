package ua.training.top;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ua.training.top.repository.VacancyRepository;
import ua.training.top.service.VacancyService;
import ua.training.top.web.ui.VacancyUIController;

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
        VacancyRepository vacancyRepository = appCtx.getBean(VacancyRepository.class);
        VacancyService vacancyService = appCtx.getBean(VacancyService.class);
        VacancyUIController vacancyUIController = appCtx.getBean(VacancyUIController.class);
//        EmployerService employerService = appCtx.getBean(EmployerService.class);
//        DoubleString doubleString = new DoubleString("java", "киев");
//        EmployerRepository employerRepository = appCtx.getBean(EmployerRepository.class);

//        System.out.println(Optional.ofNullable(employerRepository.getByName("Brat1")).orElse(null));
//        System.out.println(Optional.ofNullable(vacancyService.get(10)).orElse(null));
//        System.out.println(Optional.ofNullable(employerRepository.getByName("10")).orElse(null));



//        System.out.println("------------------------------------------------------------------------------");
        System.out.println("before="+ vacancyUIController.getByFilter("php", "киев"));
//        System.out.println("==============================================================================");
//        vacancyService.deleteBeforeDate(reasonToKeepDate);
//        System.out.println("------------------------------------------------------------------------------");
//        System.out.println("after="+ vacancyService.getAll().size());
//        System.out.println("==============================================================================");



        appCtx.close();

    }
}
