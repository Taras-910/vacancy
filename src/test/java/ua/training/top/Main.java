package ua.training.top;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ua.training.top.aggregator.AggregatorController;

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
        EmployerController employerController = appCtx.getBean(EmployerController.class);
        System.out.println("========================================");
        System.out.println(employerController.getAllWithVacancies());
*/
        AggregatorController aggregatorController = appCtx.getBean(AggregatorController.class);

        aggregatorController.refreshDB("за_рубежем", "java");

        appCtx.close();    }
}
