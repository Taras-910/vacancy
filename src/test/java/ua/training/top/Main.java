package ua.training.top;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ua.training.top.web.rest.admin.VacancyRestController;
import ua.training.top.web.rest.profile.ProfileVacancyRestController;
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

        VacancyUIController vacancyUIController = appCtx.getBean(VacancyUIController.class);
        ProfileVacancyRestController profileVacancyRestController = appCtx.getBean(ProfileVacancyRestController.class);
        VacancyRestController vacancyRestController = appCtx.getBean(VacancyRestController.class);

        System.out.println(".............................................................................");

        vacancyUIController.getByFilter("java", "киев");




        appCtx.close();

    }
}
