package ua.training.top;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ua.training.top.repository.VacancyRepository;
import ua.training.top.service.UserService;

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

        VacancyRepository vacancyRepository = appCtx.getBean(VacancyRepository.class);
        UserService service = appCtx.getBean(UserService.class);
        System.out.println(service.get(100016));

        System.out.println("==============================================================================");



        appCtx.close();

    }
}
