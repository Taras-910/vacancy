package ua.training.top;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ua.training.top.model.Role;
import ua.training.top.model.User;
import ua.training.top.service.AggregatorService;
import ua.training.top.service.FreshenService;

import static ua.training.top.SecurityUtil.setTestAuthorizedUser;

public class Main {
    public static void main(String[] args) {
        ConfigurableApplicationContext appCtx =
                new ClassPathXmlApplicationContext(new String[]{"spring/spring-app.xml", "spring/spring-db.xml"}, false);
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

//        VacancyUIController vacancyUIController = appCtx.getBean(VacancyUIController.class);
//        ProfileVacancyRestController profileVacancyRestController = appCtx.getBean(ProfileVacancyRestController.class);
//        VacancyRestController vacancyRestController = appCtx.getBean(VacancyRestController.class);
        FreshenService freshenService = appCtx.getBean(FreshenService.class);
        AggregatorService aggregatorService = appCtx.getBean(AggregatorService.class);
//        EmployerService employerService = appCtx.getBean(EmployerService.class);
//        VacancyService vacancyService = appCtx.getBean(VacancyService.class);
        System.out.println("========================================");

/*
        List<Freshen> freshenDb = freshenService.getAll();
        System.out.println("list before = " + freshenDb.size());

        aggregatorService.deleteFreshensOutLimitedHeroku(freshenDb, 12);

        List<Freshen> list2 = freshenService.getAll();
        System.out.println("list after = " + list2.size());
*/

        System.out.println(".............................................................................");





        appCtx.close();

    }
}
