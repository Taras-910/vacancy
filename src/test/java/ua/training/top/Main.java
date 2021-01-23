package ua.training.top;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ua.training.top.model.Role;
import ua.training.top.model.User;
import ua.training.top.to.VacancyTo;
import ua.training.top.web.rest.admin.VacancyRestController;
import ua.training.top.web.rest.profile.ProfileVacancyRestController;
import ua.training.top.web.ui.VacancyUIController;

import java.util.List;

import static ua.training.top.SecurityUtil.setTestAuthorizedUser;

public class Main {
    public static void main(String[] args) {

        // safe from xss
        /*String unsafe =
                "<p><a href='http://example.com/' onclick='stealCookies()'>Link</a></p>";
        String safe = Jsoup.clean(unsafe, Whitelist.basic());
        // now: <p><a href="http://example.com/" rel="nofollow">Link</a></p>
        System.out.println(safe);*/
//        try (GenericXmlApplicationContext appCtx = new ClassPathXmlApplicationContext(new String[]{"spring/spring-app.xml", "spring/spring-db.xml"}, false)) {
//            appCtx.getEnvironment().setActiveProfiles(Profiles.getActiveDbProfile());
//            appCtx.load("spring/inmemory.xml");
//            appCtx.refresh();
//            appCtx.load("spring/spring-app.xml");
//            appCtx.refresh();

        ConfigurableApplicationContext appCtx =
                new ClassPathXmlApplicationContext(new String[]{"spring/spring-app.xml", "spring/spring-db.xml", "spring/spring-mvc.xml"}, false);
        appCtx.getEnvironment().setActiveProfiles(Profiles.getActiveDbProfile());
        appCtx.refresh();

        User admin = new User(100000, "Admin", "admin@gmail.com", "admin", Role.ADMIN);
        setTestAuthorizedUser(admin);

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

//        List<VacancyTo> vacancyTos = vacancyUIController.getByFilter("php", "киев");
//        List<VacancyTo> vacancyTos2 = profileVacancyRestController.getByFilter("php", "киев");
        List<VacancyTo> vacancyTos3 = vacancyRestController.getByFilter("php", "киев");

        System.out.println(".............................................................................");
        System.out.println(vacancyTos3);




        appCtx.close();

    }
}
