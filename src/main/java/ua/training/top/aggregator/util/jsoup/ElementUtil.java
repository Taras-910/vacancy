package ua.training.top.aggregator.util.jsoup;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ua.training.top.to.VacancyNet;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static ua.training.top.aggregator.strategy.UAIndeedStrategy.getCorrectUrl;
import static ua.training.top.aggregator.util.DateUtil.*;
import static ua.training.top.aggregator.util.ToCorrectDataUtil.*;

public class ElementUtil {

    public static List<VacancyNet> getVacanciesGrc(Elements elements){
        List<VacancyNet> list = new ArrayList<>();
        elements.forEach(element -> {
            if (element != null){
                VacancyNet vacancy = new VacancyNet();
                vacancy.setSiteName("http://grc.ua");
                vacancy.setUrl(element.getElementsByTag("a").first().attr("href").trim());
                vacancy.setTitle(element.getElementsByTag("a").first().text().trim());
                vacancy.setDate(getDateHH(element.getElementsByAttributeValue("data-qa", "vacancy-serp__vacancy-date").text().trim()));
                vacancy.setCompanyName(getCorrectCompanyName(element.getElementsByAttributeValue("data-qa", "vacancy-serp__vacancy-employer").text().trim()));
                vacancy.setCity(element.getElementsByAttributeValue("data-qa", "vacancy-serp__vacancy-address").text().trim());
                vacancy.setSalary(getCorrectSalary(element.getElementsByAttributeValue("data-qa", "vacancy-serp__vacancy-compensation").text().trim()));
                vacancy.setSkills(element.getElementsByAttributeValue("data-qa", "vacancy-serp__vacancy_snippet_requirement").text().trim());
                list.add(vacancy);
            }
        });
        return list;
    }

    public static List<VacancyNet> getVacanciesHabr(Elements elements) {
        List<VacancyNet> list = new ArrayList<>();
        for (Element element : elements) {
            if (element != null) {
                VacancyNet vacancy = new VacancyNet();
                vacancy.setSiteName("https://career.habr.com");
                vacancy.setUrl(vacancy.getSiteName().concat(element.getElementsByClass("vacancy-card__icon-link").attr("href").trim()));
                vacancy.setTitle(element.getElementsByClass("vacancy-card__title").tagName("a").text().trim());
                vacancy.setDate(getDateHH(element.getElementsByClass("basic-date").text().trim()));
                vacancy.setCompanyName(getCorrectCompanyName(element.getElementsByClass("vacancy-card__meta").text().split(",")[0].trim()));
                vacancy.setCity(element.getElementsByClass("vacancy-card__meta").text().split(",")[1].trim());
                vacancy.setSalary(getCorrectSalary(element.getElementsByClass("basic-salary").text().trim()));
                vacancy.setSkills(element.getElementsByClass("vacancy-card__skills").text().trim());
                list.add(vacancy);
            }
        }
        return list;
    }

    public static List<VacancyNet> getVacanciesJabs(Elements elements) {
        List<VacancyNet> list = new ArrayList();
        elements.forEach(element -> {
            VacancyNet vacancy = new VacancyNet();
            vacancy.setSiteName("https://jobs.dou.ua");
            vacancy.setDate(printDefault(LocalDate.now().minusDays((int) (5 + list.size()*1.5)/12)));
            vacancy.setUrl(element.getElementsByTag("a").first().attr("href").trim());
            vacancy.setTitle(element.getElementsByTag("a").first().text().trim());
            vacancy.setCompanyName(getCorrectCompanyName(element.getElementsByTag("a").last().text().trim()));
            vacancy.setCity(element.getElementsByClass("cities").text().trim());
            vacancy.setSalary(getCorrectSalary(element.getElementsByClass("salary").text().trim()));
            vacancy.setSkills(element.getElementsByClass("sh-info").text().trim());
            list.add(vacancy);
        });
        return list;
    }

    public static List<VacancyNet> getVacanciesRabota(Elements elements) {
        List<VacancyNet> list = new ArrayList<>();
        for(Element element : elements) {
            if (element != null) {
                VacancyNet vacancy = new VacancyNet();
                vacancy.setDate(getCorrectDate(element.getElementsByClass("publication-time").text().trim()));
                vacancy.setSiteName("https://rabota.ua");
                vacancy.setUrl(vacancy.getSiteName().concat("/company")
                        .concat(element.getElementsByClass("card").attr("data-company-id").trim())
                        .concat("/vacancy").concat(element.getElementsByClass("card").attr("data-vacancy-id").trim()));
                vacancy.setTitle(getCorrectTitle(element.getElementsByClass("card-title").text().trim()));
                vacancy.setCity(element.getElementsByClass("location").text().trim());
                vacancy.setCompanyName(getCorrectCompanyName(element.getElementsByClass("company-name").text().trim()));
                vacancy.setSalary(getCorrectSalary(element.getElementsByClass("salary").text().trim()));
                vacancy.setSkills(element.getElementsByClass("card-description").text().trim());
                list.add(vacancy);
            }
        }
        return list;
    }
    public static List<VacancyNet> getVacanciesIndeed(Elements elements) {
        List<VacancyNet> list = new ArrayList<>();
        for (Element element : elements) {
            if (element != null) {
                VacancyNet vacancy = new VacancyNet();
                vacancy.setSiteName("https://ua.indeed.com");
                vacancy.setTitle(element.getElementsByAttributeValue("data-tn-element","jobTitle").text().trim());
                vacancy.setSkills(element.getElementsByClass("summary").text().trim());
                vacancy.setCity(element.getElementsByClass("location accessible-contrast-color-location").text().trim());
                vacancy.setCompanyName(getCorrectCompanyName(element.getElementsByClass("company").text().trim()));
                vacancy.setSalary("1");
                vacancy.setUrl(getCorrectUrl(element.getElementsByClass("jobsearch-SerpJobCard").attr("data-jk").trim()));
                vacancy.setDate(getCorrectDate(element.getElementsByClass("date ").text().trim()));
                list.add(vacancy);
            }
        }
        return list;
    }

    public static List<VacancyNet> getVacanciesJooble(Elements elements){
        List<VacancyNet> list = new ArrayList();
        for (Element element : elements) {
            if (element != null) {
                VacancyNet vacancy = new VacancyNet();
                vacancy.setDate(getCorrectDate(element.getElementsByClass("date_from_creation").tagName("span").text().trim()));
                vacancy.setSiteName("https://ua.jooble.org");
                vacancy.setUrl(element.getElementsByClass("link-position job-marker-js").attr("href").trim());
                vacancy.setTitle(element.getElementsByClass("position").tagName("span").text().trim());
                vacancy.setCompanyName(getCorrectCompanyName(element.getElementsByClass("employer-widget_company").text().trim()));
                vacancy.setCity(element.getElementsByClass("serp_location__region").text().trim());
                vacancy.setSalary(getCorrectSalary(element.getElementsByClass("description").text()));
                vacancy.setSkills(element.getElementsByClass("description").text().trim());
                list.add(vacancy);
            }
        }
        return list;
    }

//https://ua.jooble.org/desc/
//skills include salary -> need to divide :
// Salary: 110-125 (B2B) PLN / hour. Requirements: Java, Spring, JPA, REST, Java EE. Tools: Jira, Agile, Scrum. Additionally: Sport subscription, Private healthcare, International projects. Java, Spring, JPA, REST, Java EE, JUnit, Angular, SQL, OOP, JavaScript, English,...

    public static List<VacancyNet> getVacanciesWork(Elements elements) {
        List<VacancyNet> list = new ArrayList<>();
        for (Element element : elements) {
            if (element != null) {
                VacancyNet vacancy = new VacancyNet();
                vacancy.setSiteName("https://www.work.ua");
                vacancy.setUrl(vacancy.getSiteName().concat(element.getElementsByTag("a").attr("href")));
                vacancy.setTitle(getCorrectTitle(element.getElementsByTag("a").first().text()));
                vacancy.setCompanyName(getCorrectCompanyName(element.getElementsByTag("img").attr("alt")));
                vacancy.setSalary(getCorrectSalary(element.getElementsByTag("h2").next().tagName("b").text()));
                vacancy.setSkills(element.getElementsByClass("overflow").text().trim());
                vacancy.setDate(getCorrectDate(element.getElementsByClass("pull-right").text().trim()));
                vacancy.setCity(getCorrectCity(element.getElementsByClass("add-top-xs").first().children().next().next().text()));
                list.add(vacancy);
            }
        }
        return list;
    }
}
