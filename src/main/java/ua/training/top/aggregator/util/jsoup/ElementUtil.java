package ua.training.top.aggregator.util.jsoup;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ua.training.top.to.DoubleString;
import ua.training.top.to.VacancyTo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static ua.training.top.aggregator.strategy.UAIndeedStrategy.getCorrectUrl;
import static ua.training.top.aggregator.util.DateUtil.*;
import static ua.training.top.aggregator.util.ToCorrectDataUtil.*;
import static ua.training.top.util.DateTimeUtil.clearTime;
import static ua.training.top.util.DateTimeUtil.parse;

public class ElementUtil {

    public static List<VacancyTo> getVacanciesGrc(Elements elements, DoubleString doubleString) {
        List<VacancyTo> list = new ArrayList<>();
        elements.forEach(element -> {
            VacancyTo v = new VacancyTo();
            String title = element.getElementsByTag("a").first().text().trim().toLowerCase();
            String skills = element.getElementsByAttributeValue("data-qa", "vacancy-serp__vacancy_snippet_requirement").text().trim().toLowerCase();
            String address = element.getElementsByAttributeValue("data-qa", "vacancy-serp__vacancy-address").text().trim().toLowerCase();
//            log.info("{} {}", doubleString.getWorkplaceTask(), address);
            if (title.contains(doubleString.getLanguageTask()) || skills.contains(doubleString.getLanguageTask())){
                v.setTitle(element.getElementsByTag("a").first().text().trim());
                v.setEmployerName(getCorrectCompanyName(element.getElementsByAttributeValue("data-qa", "vacancy-serp__vacancy-employer").text().trim()));
                v.setAddress(element.getElementsByAttributeValue("data-qa", "vacancy-serp__vacancy-address").text().trim());
                v.setSalaryMax(salaryMax(getCorrectSalary(element.getElementsByAttributeValue("data-qa", "vacancy-serp__vacancy-compensation").text().trim())));
                v.setSalaryMin(salaryMin(getCorrectSalary(element.getElementsByAttributeValue("data-qa", "vacancy-serp__vacancy-compensation").text().trim())));
                v.setUrl(element.getElementsByTag("a").first().attr("href").trim());
                v.setSkills(element.getElementsByAttributeValue("data-qa", "vacancy-serp__vacancy_snippet_requirement").text().trim());
                v.setReleaseDate(clearTime(parse(getCorrectDate(getDateHH(element.getElementsByAttributeValue("data-qa", "vacancy-serp__vacancy-date").text().trim())), null)));
                v.setSiteName("http://grc.ua");
                list.add(v);
            }
        });
        return list;
    }

    public static List<VacancyTo> getVacanciesHabr(Elements elements, DoubleString doubleString) {
        List<VacancyTo> list = new ArrayList<>();
        for (Element element : elements) {
            log.info("elements {}", elements.size());
            if (element != null) {
                VacancyTo v = new VacancyTo();
                v.setTitle(element.getElementsByClass("vacancy-card__title").tagName("a").text().trim());
                v.setEmployerName(getCorrectCompanyName(element.getElementsByClass("vacancy-card__meta").text().split(",")[0].trim()));
                v.setAddress(element.getElementsByClass("vacancy-card__meta").text().split(",")[1].trim());
                v.setSalaryMax(salaryMax(getCorrectSalary(element.getElementsByClass("basic-salary").text().trim())));
                v.setSalaryMin(salaryMin(getCorrectSalary(element.getElementsByClass("basic-salary").text().trim())));
                v.setUrl("https://career.habr.com".concat(element.getElementsByClass("vacancy-card__icon-link").attr("href").trim()));
                v.setSkills(element.getElementsByClass("vacancy-card__skills").text().trim());
                v.setReleaseDate(parse(element.getElementsByAttribute("datetime").attr("datetime").substring(0, 10), null));
                v.setSiteName("https://career.habr.com");
                list.add(v);
            }
        }
        return list;
    }

    public static List<VacancyTo> getVacanciesJobs(Elements elements, DoubleString doubleString) {
        List<VacancyTo> list = new ArrayList();
        elements.forEach(element -> {
            VacancyTo v = new VacancyTo();
            v.setTitle(element.getElementsByTag("a").first().text().trim());
            v.setEmployerName(getCorrectCompanyName(element.getElementsByTag("a").last().text().trim()));
            v.setAddress(element.getElementsByClass("cities").text().trim());
            v.setSalaryMax(salaryMax(getCorrectSalary(element.getElementsByClass("salary").text().trim())));
            v.setSalaryMin(salaryMin(getCorrectSalary(element.getElementsByClass("salary").text().trim())));
            v.setUrl(element.getElementsByTag("a").first().attr("href").trim());
            v.setSkills(element.getElementsByClass("sh-info").text().trim());
            v.setReleaseDate(parse(printDefault(LocalDate.now().minusDays(1).minusDays((int) (5 + list.size() * 1.5) / 12)), null));
            v.setSiteName("https://jobs.dou.ua");
            list.add(v);
        });
        return list;
    }

    public static List<VacancyTo> getVacanciesRabota(Elements elements, DoubleString doubleString) {
        List<VacancyTo> list = new ArrayList<>();
        for (Element element : elements) {
            VacancyTo v = new VacancyTo();
            String title = element.getElementsByClass("card-title").text().trim().toLowerCase();
            String skills = element.getElementsByClass("card-description").text().trim().toLowerCase();
            if (title.contains(doubleString.getLanguageTask()) || skills.contains(doubleString.getLanguageTask())) {
                v.setTitle(getCorrectTitle(element.getElementsByClass("card-title").text().trim()));
                v.setEmployerName(getCorrectCompanyName(element.getElementsByClass("company-name").text().trim()));
                v.setAddress(element.getElementsByClass("location").text().trim());
                v.setSalaryMax(salaryMax(getCorrectSalary(element.getElementsByClass("salary").text().trim())));
                v.setSalaryMin(salaryMin(getCorrectSalary(element.getElementsByClass("salary").text().trim())));
                v.setUrl("https://rabota.ua".concat("/company").concat(element.getElementsByClass("card").attr("data-company-id").trim())
                        .concat("/vacancy").concat(element.getElementsByClass("card").attr("data-vacancy-id").trim()));
                v.setSkills(element.getElementsByClass("card-description").text().trim());
                v.setReleaseDate(parse(getCorrectDate(element.getElementsByClass("publication-time").text().trim()), null));
                v.setSiteName("https://rabota.ua");
                list.add(v);
            }
        }
        return list;
    }

    public static List<VacancyTo> getVacanciesIndeed(Elements elements, DoubleString doubleString) {
        List<VacancyTo> list = new ArrayList<>();
        for (Element element : elements) {
            if (element != null) {
                VacancyTo v = new VacancyTo();
                v.setTitle(element.getElementsByAttributeValue("data-tn-element", "jobTitle").text().trim());
                v.setEmployerName(getCorrectCompanyName(element.getElementsByClass("company").text().trim()));
                v.setAddress(element.getElementsByClass("location accessible-contrast-color-location").text().trim());
                v.setSalaryMax(1);
                v.setSalaryMin(1);
                v.setUrl(getCorrectUrl(element.getElementsByClass("jobsearch-SerpJobCard").attr("data-jk").trim()));
                v.setSkills(element.getElementsByClass("summary").text().trim());
                v.setReleaseDate(clearTime(parse(getCorrectDate(element.getElementsByClass("date ").text().trim()), null)));
                v.setSiteName("https://ua.indeed.com");
                list.add(v);
            }
        }
        return list;
    }

    public static List<VacancyTo> getVacanciesJooble(Elements elements, DoubleString doubleString) {
        List<VacancyTo> list = new ArrayList();
        for (Element element : elements) {
            VacancyTo v = new VacancyTo();
            String title = element.getElementsByClass("position").tagName("span").text().trim().toLowerCase();
            String skills = element.getElementsByClass("description").text().trim().toLowerCase();
            if (title.contains(doubleString.getLanguageTask()) || skills.contains(doubleString.getLanguageTask())) {
                v.setTitle(element.getElementsByClass("position").tagName("span").text().trim());
                v.setEmployerName(getCorrectCompanyName(element.getElementsByClass("employer-widget_company").text().trim()));
                v.setAddress(element.getElementsByClass("serp_location__region").text().trim());
                v.setSalaryMax(salaryMax(getCorrectSalary(element.getElementsByClass("description").text())));
                v.setSalaryMin(salaryMin(getCorrectSalary(element.getElementsByClass("description").text())));
                v.setUrl(element.getElementsByClass("link-position job-marker-js").attr("href").trim());
                v.setSkills(element.getElementsByClass("description").text().trim());
                v.setReleaseDate(clearTime(parse(getCorrectDate(element.getElementsByClass("date_from_creation").tagName("span").text().trim()), null)));
                v.setSiteName("https://ua.jooble.org");
                list.add(v);
            }
        }
        return list;
    }

    public static List<VacancyTo> getVacanciesWork(Elements elements, DoubleString doubleString) {
        List<VacancyTo> list = new ArrayList<>();
        for (Element element : elements) {
            VacancyTo v = new VacancyTo();
            String title = element.getElementsByTag("a").first().text().trim().toLowerCase();
            String skills = element.getElementsByClass("overflow").text().trim().toLowerCase();
            if (title.contains(doubleString.getLanguageTask()) || skills.contains(doubleString.getLanguageTask())) {
                v.setTitle(getCorrectTitle(element.getElementsByTag("a").first().text()));
                v.setEmployerName(getCorrectCompanyName(element.getElementsByTag("img").attr("alt")));
                v.setAddress(getCorrectCity(element.getElementsByClass("add-top-xs").first().children().next().next().text()));
                v.setSalaryMax(salaryMax(getCorrectSalary(element.getElementsByTag("h2").next().tagName("b").first().text().trim())));
                v.setSalaryMin(salaryMin(getCorrectSalary(element.getElementsByTag("h2").next().tagName("b").first().text().trim())));
                v.setUrl("https://www.work.ua".concat(element.getElementsByTag("a").attr("href")));
                v.setSkills(element.getElementsByClass("overflow").text().trim());
                v.setReleaseDate(clearTime(parse(getDateHH(element.getElementsByTag("a").first().attr("title").split("вакансия от")[1].trim()), null)));
                v.setSiteName("https://www.work.ua");
                list.add(v);
            }
        }
        return list;
    }
}




//                String line = (element.getElementsByTag("h2").next().tagName("b").first().text().trim());
//                System.out.println("____________________________________________________________________________________");
//                System.out.println("element:\n" + element);
//                System.out.println("\ndate=" + line);
//                System.out.println("getCorrectSalary=" + getCorrectSalary(line.toString()));
//                System.out.println("salaryMax=" + salaryMax(getCorrectSalary(line.toString())));
//                System.out.println("salaryMin=" + salaryMin(getCorrectSalary((line.toString()))));
//
//                System.out.println(".......................................................");
//                System.out.println(element.getElementsByTag("a").first().attr("title").split("вакансия от")[1]);
//                System.out.println(getDateHH(element.getElementsByTag("a").first().attr("title").split("вакансия от")[1]));
//                System.out.println(".......................................................");


/*
                String line = (element.getElementsByClass("description").text());
                System.out.println("____________________________________________________________________________________");
                System.out.println("element:\n" + element);
                System.out.println("\ndate=" + line);
                System.out.println("getCorrectSalary=" + getCorrectSalary(line.toString()));
                System.out.println("salaryMax=" + salaryMax(getCorrectSalary(line.toString())));
                System.out.println("salaryMin=" + salaryMin(getCorrectSalary((line.toString()))));

*/
