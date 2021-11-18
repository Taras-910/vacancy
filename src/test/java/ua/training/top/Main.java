package ua.training.top;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ua.training.top.model.Role;
import ua.training.top.model.User;
import ua.training.top.service.VacancyService;

import static ua.training.top.SecurityUtil.setTestAuthorizedUser;
import static ua.training.top.testData.UserTestData.USER_ID;

public class Main {
    public static void main(String[] args) {
        ConfigurableApplicationContext appCtx =
                new ClassPathXmlApplicationContext(new String[]{"spring/spring-app.xml", "spring/spring-db.xml"}, false);
        appCtx.getEnvironment().setActiveProfiles(Profiles.getActiveDbProfile());
        appCtx.refresh();

        User user = new User(USER_ID, "User", "user@yandex.ru", "password", Role.USER);
        User admin = new User(100000, "Admin", "admin@gmail.com", "admin", Role.ADMIN);
        setTestAuthorizedUser(admin);
//        setTestAuthorizedUser(user);

/*
        System.out.println("Bean definition names: ");
        System.out.println("========================================");
        for(String s : appCtx.getBeanDefinitionNames()) {
            System.out.println(s);
        }
        System.out.println("========================================");
*/

//        VacancyUIController vacancyUIController = appCtx.getBean(VacancyUIController.class);
//        ProfileVacancyRestController profileVacancyRestController = appCtx.getBean(ProfileVacancyRestController.class);
//        VacancyRestController vacancyRestController = appCtx.getBean(VacancyRestController.class);
//        FreshenService freshenService = appCtx.getBean(FreshenService.class);
//        AggregatorService aggregatorService = appCtx.getBean(AggregatorService.class);
//        EmployerService employerService = appCtx.getBean(EmployerService.class);
        VacancyService vacancyService = appCtx.getBean(VacancyService.class);
//        VoteService voteService = appCtx.getBean(VoteService.class);

        System.out.println(".".repeat(120));



        System.out.println(".".repeat(120));
        appCtx.close();
    }
}
