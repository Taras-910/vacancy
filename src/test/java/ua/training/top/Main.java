package ua.training.top;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ua.training.top.repository.VacancyRepository;

import static ua.training.top.testData.VacancyTestData.VACANCY2;

public class Main {
    public static void main(String[] args) {

        ConfigurableApplicationContext appCtx =
                new ClassPathXmlApplicationContext(new String[]{"spring/spring-app.xml", "spring/spring-db.xml"}, false);
        appCtx.refresh();
/*
        System.out.println("Bean definition names: ");
        for(String s : appCtx.getBeanDefinitionNames()) {
            System.out.println(s);
        }
*/

        VacancyRepository vacancyRepository = appCtx.getBean(VacancyRepository.class);

        System.out.println(vacancyRepository.getAll());
        System.out.println("========================================");
        System.out.println(VACANCY2);





        appCtx.close();    }
}
