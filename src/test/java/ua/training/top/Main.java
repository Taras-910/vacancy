package ua.training.top;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ua.training.top.model.Role;
import ua.training.top.model.User;
import ua.training.top.model.Vacancy;
import ua.training.top.service.*;

import java.util.List;
import java.util.stream.Collectors;

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

        String[] beans = appCtx.getBeanDefinitionNames();
        System.out.println("Bean definition names: "+ beans.length);
        System.out.println("profiles="+Profiles.getActiveDbProfile());
        System.out.println("=".repeat(120));
        for(String s : beans) {
            System.out.println(s);
        }
        System.out.println("=".repeat(120));

//        VacancyUIController vacancyUIController = appCtx.getBean(VacancyUIController.class);
//        ProfileVacancyRestController profileVacancyRestController = appCtx.getBean(ProfileVacancyRestController.class);
//        VacancyRestController vacancyRestController = appCtx.getBean(VacancyRestController.class);
        UserService userService = appCtx.getBean(UserService.class);
//        FreshenService freshenService = appCtx.getBean(FreshenService.class);
//        AggregatorService aggregatorService = appCtx.getBean(AggregatorService.class);
        RateService rateService = appCtx.getBean(RateService.class);
        EmployerService employerService = appCtx.getBean(EmployerService.class);
        VacancyService vacancyService = appCtx.getBean(VacancyService.class);
        VoteService voteService = appCtx.getBean(VoteService.class);
//        RateRepository repository = appCtx.getBean(RateRepository.class);

        delim();

        List<Vacancy> list1 = vacancyService.getAll();
        List<Vacancy> list2 = list1.stream()
                .filter(v -> !v.getUrl().contains("linkedin"))
                .filter(v -> !v.getUrl().contains("jobs.dou"))
                .filter(v -> !v.getUrl().contains("grc.ua"))
                .filter(v -> v.getUrl().contains("?"))
                .peek(System.out::println)
                .collect(Collectors.toList());
        System.out.println("size="+list1.size());
        System.out.println("size="+list2.size());

//        vacancyService.deleteList(list2);

        delim();
        appCtx.close();
    }

    private static void delim() {
        System.out.println(".".repeat(120));
    }
}
