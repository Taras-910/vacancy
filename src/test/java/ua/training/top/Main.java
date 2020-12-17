package ua.training.top;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {
    public static void main(String[] args) {

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
/*
        VacancyService vacancyService = appCtx.getBean(VacancyService.class);
        EmployerService employerService = appCtx.getBean(EmployerService.class);
        DoubleString doubleString = new DoubleString("java", "киев");


        System.out.println("==============================================================================");
        vacancyService.deleteBeforeDate(valid_date);
        System.out.println();
        System.out.println("==============================================================================");
*/



        appCtx.close();

    }
}
