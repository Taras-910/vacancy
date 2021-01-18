package ua.training.top.util.refresh;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.training.top.model.Freshen;
import ua.training.top.service.VacancyService;
import ua.training.top.to.VacancyTo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static ua.training.top.aggregator.strategy.NofluffjobsStrategy.validAddress;
import static ua.training.top.aggregator.strategy.NofluffjobsStrategy.validDate;
import static ua.training.top.aggregator.strategy.UAIndeedStrategy.getCorrectUrl;
import static ua.training.top.util.DateTimeUtil.parseLocalDate;
import static ua.training.top.util.refresh.datas.CorrectAddress.getCorrectAddress;
import static ua.training.top.util.refresh.datas.CorrectCompanyName.getCorrectCompanyName;
import static ua.training.top.util.refresh.datas.CorrectTitle.getCorrectTitle;
import static ua.training.top.util.refresh.date.DateUtil.supportDate;
import static ua.training.top.util.refresh.date.ToCorrectDate.getCorrectDate;
import static ua.training.top.util.refresh.salary.MinMax.salaryMax;
import static ua.training.top.util.refresh.salary.MinMax.salaryMin;
import static ua.training.top.util.refresh.salary.SalaryUtil.getCorrectSalary;
import static ua.training.top.util.xss.xssUtil.xssClear;

public class ElementUtil {
    public static final Logger log = LoggerFactory.getLogger(ElementUtil.class);

    public static List<VacancyTo> getVacanciesDjinni(Elements elements, Freshen doubleString) {
        List<VacancyTo> list = new ArrayList();
        elements.forEach(element -> {
            VacancyTo v = new VacancyTo();
//            String[] address = element.getElementsByClass("list-jobs__details__info").text().split("·")[0].trim().split(" ");
            v.setTitle(xssClear(element.getElementsByClass("list-jobs__title").text().trim()));
            v.setEmployerName(xssClear(element.getElementsByClass("list-jobs__details__info").tagName("a").first().child(1).text().trim()));
            v.setAddress(doubleString.getWorkplace());
            v.setSalaryMax(1);
            v.setSalaryMin(1);
            v.setUrl("https://djinni.co".concat(xssClear(element.getElementsByClass("profile").first().attr("href").trim())));
            v.setSkills(xssClear(element.getElementsByClass("list-jobs__description").text().trim()));
            v.setReleaseDate(getCorrectDate(xssClear(element.getElementsByClass("inbox-date").text().trim())));
            v.setSiteName("https://djinni.co");
            list.add(v);
        });
        return list;
    }

    public static List<VacancyTo> getVacanciesGrc(Elements elements, Freshen doubleString) {
        List<VacancyTo> list = new ArrayList<>();
        elements.forEach(element -> {
            VacancyTo v = new VacancyTo();
            String title = element.getElementsByTag("a").first().text().trim().toLowerCase();
            String skills = element.getElementsByAttributeValue("data-qa", "vacancy-serp__vacancy_snippet_requirement").text().trim().toLowerCase();
            if (title.contains(doubleString.getLanguage()) || skills.contains(doubleString.getLanguage())){
                v.setTitle(xssClear(element.getElementsByTag("a").first().text().trim()));
                v.setEmployerName(getCorrectCompanyName(xssClear(element.getElementsByAttributeValue("data-qa", "vacancy-serp__vacancy-employer").text().trim())));
                v.setAddress(xssClear(element.getElementsByAttributeValue("data-qa", "vacancy-serp__vacancy-address").text().trim()));
                v.setSalaryMax(salaryMax(getCorrectSalary(xssClear(element.getElementsByAttributeValue("data-qa", "vacancy-serp__vacancy-compensation").text().trim()))));
                v.setSalaryMin(salaryMin(getCorrectSalary(xssClear(element.getElementsByAttributeValue("data-qa", "vacancy-serp__vacancy-compensation").text().trim()))));
                v.setUrl(xssClear(element.getElementsByTag("a").first().attr("href").trim()));
                v.setSkills(xssClear(element.getElementsByAttributeValue("data-qa", "vacancy-serp__vacancy_snippet_requirement").text().trim()));
                v.setReleaseDate(LocalDate.parse(supportDate(xssClear(element.getElementsByClass("vacancy-serp-item__publication-date").text().trim()))));
                v.setSiteName("http://grc.ua");
                list.add(v);
            }
        });
        return list;
    }

    public static List<VacancyTo> getVacanciesHabr(Elements elements, Freshen doubleString) {
        List<VacancyTo> list = new ArrayList<>();
        for (Element element : elements) {
            log.info("elements {}", elements.size());
            if (element != null) {
                VacancyTo v = new VacancyTo();
                v.setTitle(xssClear(element.getElementsByClass("vacancy-card__title").tagName("a").text().trim()));
                v.setEmployerName(getCorrectCompanyName(xssClear(element.getElementsByClass("vacancy-card__company").first().child(0).text())));
                v.setAddress(xssClear(element.getElementsByClass("vacancy-card__meta").tagName("a").first().text()));
                v.setSalaryMax(salaryMax(getCorrectSalary(xssClear(element.getElementsByClass("basic-salary").text().trim()))));
                v.setSalaryMin(salaryMin(getCorrectSalary(xssClear(element.getElementsByClass("basic-salary").text().trim()))));
                v.setUrl("https://career.habr.com".concat(xssClear(element.getElementsByClass("vacancy-card__icon-link").attr("href").trim())));
                v.setSkills(xssClear(element.getElementsByClass("vacancy-card__skills").text().trim()));
                v.setReleaseDate(LocalDate.parse(xssClear(element.getElementsByAttribute("datetime").attr("datetime").substring(0, 10)), null));
                v.setSiteName("https://career.habr.com");
                list.add(v);
            }
        }
        return list;
    }

    public static List<VacancyTo> getVacanciesJobs(Elements elements, Freshen doubleString) {
        List<VacancyTo> list = new ArrayList();
        elements.forEach(element -> {
            VacancyTo v = new VacancyTo();
            v.setTitle(xssClear(element.getElementsByTag("a").first().text().trim()));
            v.setEmployerName(getCorrectCompanyName(xssClear(element.getElementsByTag("a").last().text().trim())));
            v.setAddress(xssClear(element.getElementsByClass("cities").text().trim()));
            v.setSalaryMax(salaryMax(getCorrectSalary(xssClear(element.getElementsByClass("salary").text().trim()))));
            v.setSalaryMin(salaryMin(getCorrectSalary(xssClear(element.getElementsByClass("salary").text().trim()))));
            v.setUrl(xssClear(element.getElementsByTag("a").first().attr("href").trim()));
            v.setSkills(xssClear(element.getElementsByClass("sh-info").text().trim()));
            v.setReleaseDate(LocalDate.now().minusDays(1).minusDays((int) (5 + list.size() * 1.5) / 12));
            v.setSiteName("https://jobs.dou.ua");
            list.add(v);
        });
        return list;
    }

    public static List<VacancyTo> getVacanciesLinkedin(Elements elements, Freshen doubleString) {
        List<VacancyTo> list = new ArrayList();
        elements.forEach(element -> {
            VacancyTo v = new VacancyTo();
            v.setTitle(xssClear(element.getElementsByClass("result-card__title job-result-card__title").text().trim()));
            v.setEmployerName(getCorrectCompanyName(xssClear(element.getElementsByClass("result-card__subtitle-link job-result-card__subtitle-link").text().trim())));
            v.setAddress(xssClear(element.getElementsByClass("job-result-card__location").text().trim()));
            v.setSalaryMax(1);
            v.setSalaryMin(1);
            v.setUrl(xssClear(element.getElementsByClass("result-card__full-card-link").first().attr("href").split("&")[0].trim()));
            v.setSkills("see the card on the link");
            v.setReleaseDate(parseLocalDate(xssClear(element.getElementsByTag("time").tagName("time").attr("datetime"))));
            v.setSiteName("https://www.linkedin.com");
            list.add(v);
        });
        return list;
    }

    public static Collection<? extends VacancyTo> getNofluffjobsVacancies(Elements elements) {
        List<VacancyTo> list = new ArrayList<>();
        log.info("elements {}", elements.size());
        for (Element element : elements) {
            if (element != null) {
                VacancyTo v = new VacancyTo();
                v.setTitle(xssClear(element.getElementsByClass("posting-title__position").text().trim()));
                v.setEmployerName(xssClear(element.getElementsByClass("posting-title__company").text()).substring(2).trim());
                v.setAddress(validAddress(xssClear(element.getElementsByTag("nfj-posting-item-city").text())));
                v.setSalaryMax(salaryMax(getCorrectSalary(xssClear(element.getElementsByTag("nfj-posting-item-tags").text().trim()))));
                v.setSalaryMin(salaryMin(getCorrectSalary(xssClear(element.getElementsByTag("nfj-posting-item-tags").text().trim()))));
                v.setUrl("https://nofluffjobs.com".concat(xssClear(element.getElementsByTag("a").attr("href").trim())));
                v.setSkills("see the card on the link");
                v.setReleaseDate(validDate(element.getElementsByClass("new-label").text()));
                v.setSiteName("https://nofluffjobs.com/");
                list.add(v);
            }
        }
        return list;
    }

    public static List<VacancyTo> getVacanciesRabota(Elements elements, Freshen doubleString) {
        List<VacancyTo> list = new ArrayList<>();
        for (Element element : elements) {
            VacancyTo v = new VacancyTo();
            String title = element.getElementsByClass("card-title").text().trim().toLowerCase();
            String skills = element.getElementsByClass("card-description").text().trim().toLowerCase();
            if (title.contains(doubleString.getLanguage()) || skills.contains(doubleString.getLanguage())) {
                v.setTitle(getCorrectTitle(xssClear(element.getElementsByClass("card-title").text().trim())));
                v.setEmployerName(getCorrectCompanyName(xssClear(element.getElementsByClass("company-name").text().trim())));
                v.setAddress(xssClear(element.getElementsByClass("location").text().trim()));
                v.setSalaryMax(salaryMax(getCorrectSalary(xssClear(element.getElementsByClass("salary").text().trim()))));
                v.setSalaryMin(salaryMin(getCorrectSalary(xssClear(element.getElementsByClass("salary").text().trim()))));
                v.setUrl("https://rabota.ua".concat("/company").concat(xssClear(element.getElementsByClass("card").attr("data-company-id").trim()))
                        .concat("/vacancy").concat(element.getElementsByClass("card").attr("data-vacancy-id").trim()));
                v.setSkills(xssClear(element.getElementsByClass("card-description").text().trim()));
                v.setReleaseDate(getCorrectDate(xssClear(element.getElementsByClass("publication-time").text().trim())));
                v.setSiteName("https://rabota.ua");
                list.add(v);
            }
        }
        return list;
    }

    public static List<VacancyTo> getVacanciesIndeed(Elements elements, Freshen doubleString) {
        List<VacancyTo> list = new ArrayList<>();
        for (Element element : elements) {
            if (element != null) {
                VacancyTo v = new VacancyTo();
                v.setTitle(xssClear(element.getElementsByAttributeValue("data-tn-element", "jobTitle").text().trim()));
                v.setEmployerName(getCorrectCompanyName(xssClear(element.getElementsByClass("company").text().trim())));
                v.setAddress(xssClear(element.getElementsByClass("location accessible-contrast-color-location").text().trim()));
                v.setSalaryMax(1);
                v.setSalaryMin(1);
                v.setUrl(getCorrectUrl(xssClear(element.getElementsByClass("jobsearch-SerpJobCard").attr("data-jk").trim())));
                v.setSkills(xssClear(element.getElementsByClass("summary").text().trim()));
                v.setReleaseDate(getCorrectDate(xssClear(element.getElementsByClass("date ").text().trim())));
                v.setSiteName("https://ua.indeed.com");
                list.add(v);
            }
        }
        return list;
    }

    public static List<VacancyTo> getVacanciesJooble(Elements elements, Freshen doubleString) {
//        elements.stream().peek(e -> log.info("\nelement={}\n", e)).collect(Collectors.toList());
        List<VacancyTo> list = new ArrayList();
        for (Element element : elements) {
            try {
                VacancyTo v = new VacancyTo();
                String title = element.getElementsByClass("_84af9").text().trim().toLowerCase();
                String skills = element.getElementsByClass("_0b1c1").text().trim().toLowerCase();
                if (title.contains(doubleString.getLanguage()) || skills.contains(doubleString.getLanguage())) {
                    v.setTitle(xssClear(element.getElementsByClass("_84af9").tagName("span").text().trim()));
                    v.setEmployerName(getCorrectCompanyName(xssClear(element.getElementsByClass("_786d5").text().trim())));
                    v.setAddress(xssClear(element.getElementsByClass("caption _8d375").first().text().trim()));
                    v.setSalaryMax(salaryMax(getCorrectSalary(xssClear(element.getElementsByClass("_0b1c1").tagName("span").first().text()))));
                    v.setSalaryMin(salaryMin(getCorrectSalary(xssClear(element.getElementsByClass("_0b1c1").tagName("span").first().text()))));
                    v.setUrl("https://ua.jooble.org/desc/".concat(xssClear(element.getElementsByClass("_31572 _07ebc").attr("id"))));
                    v.setSkills(xssClear(element.getElementsByTag("b").tagName("span").nextAll().text()));
                    v.setReleaseDate(getCorrectDate(xssClear(element.getElementsByClass("_8d375").last().text().trim())));
                    v.setSiteName("https://ua.jooble.org");
                    list.add(v);
                }
            } catch (Exception e) {
                log.info("there is error UAJoobleStrategy for parse element {}", element);
            }
        }
        return list;
    }

    public static List<VacancyTo> getVacanciesWork(Elements elements, Freshen doubleString) {
        List<VacancyTo> list = new ArrayList<>();
        for (Element element : elements) {
            try {
                VacancyTo v = new VacancyTo();
                String title = element.getElementsByTag("a").first().text().trim().toLowerCase();
                String skills = element.getElementsByClass("overflow").text().trim().toLowerCase();
                if (title.contains(doubleString.getLanguage()) || skills.contains(doubleString.getLanguage())) {
                    v.setTitle(getCorrectTitle(xssClear(element.getElementsByTag("a").first().text())));
                    v.setEmployerName(getCorrectCompanyName(xssClear(element.getElementsByTag("img").attr("alt"))));
                    v.setAddress(getCorrectAddress(xssClear(element.getElementsByClass("add-top-xs").first().children().next().next().text())));
                    v.setSalaryMax(salaryMax(getCorrectSalary(xssClear(element.getElementsByTag("b").tagName("b").first().text().trim()))));
                    v.setSalaryMin(salaryMin(getCorrectSalary(xssClear(element.getElementsByTag("b").tagName("b").first().text().trim()))));
                    v.setUrl("https://www.work.ua".concat(xssClear(element.getElementsByTag("a").attr("href"))));
                    v.setSkills(xssClear(element.getElementsByClass("overflow").text().trim()));
                    v.setReleaseDate(parseLocalDate(supportDate(xssClear(element.getElementsByTag("a").first().attr("title").split("вакансия от ")[1].trim()))));
                    v.setSiteName("https://www.work.ua");
                    list.add(v);
                }
            } catch (Exception e) {
                log.info("there is error on ElementUtil {} for parse element {}", VacancyService.class.getSimpleName(), element);
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

                String line = element.getElementsByClass("_29fd5").text();
                System.out.println("____________________________________________________________________________________");
                System.out.println("element:\n" + element);
                System.out.println("\nline=" + line);
                System.out.println("getCorrectSalary=" + getCorrectSalary(line.toString()));
                System.out.println("salaryMax=" + salaryMax(getCorrectSalary(line.toString())));
                System.out.println("salaryMin=" + salaryMin(getCorrectSalary((line.toString()))));




                String line = element.getElementsByClass("_0b1c1").tagName("span").first().child(0).text();
//                String line2 = element.getElementsByClass("_1e0a8").text();

                System.out.println("____________________________________________________________________________________");
                System.out.println("element:\n" + element);
                System.out.println("\nline=" + line);
                System.out.println("getCorrectSalary=" + getCorrectSalary(line.toString()));
                System.out.println("salaryMax=" + salaryMax(getCorrectSalary(line.toString())));
                System.out.println("salaryMin=" + salaryMin(getCorrectSalary((line.toString()))));




                String line = element.getElementsByClass("vacancy-card__salary").text().trim();

                System.out.println("____________________________________________________________________________________");
                System.out.println("element:\n" + element);
                System.out.println("\nline=" + line);
                System.out.println("getCorrectSalary=" + getCorrectSalary(line.toString()));
                System.out.println("salaryMax=" + salaryMax(getCorrectSalary(line.toString())));
                System.out.println("salaryMin=" + salaryMin(getCorrectSalary((line.toString()))));



*/
/*
//                String line = element.getElementsByTag("h2").next().tagName("b").first().text().trim();
                String line = element.getElementsByTag("b").tagName("b").first().text().trim();

                System.out.println("____________________________________________________________________________________");
                System.out.println("element:\n" + element);
                System.out.println("\nline=" + line);
                System.out.println("getXssCleaned=" + getXssCleaned(line.toString()));
                System.out.println("getCorrectSalary=" + getCorrectSalary(getXssCleaned(line.toString())));
                System.out.println("salaryMax=" + salaryMax(getCorrectSalary(getXssCleaned(line.toString()))));
                System.out.println("salaryMin=" + salaryMin(getCorrectSalary(getXssCleaned(line.toString()))));



            String line = element.getElementsByTag("time").tagName("time").attr("datetime");

            System.out.println("____________________________________________________________________________________");
            System.out.println("element:\n" + element);
            System.out.println("\nline=" + line);
            System.out.println("getXssCleaned=" + getXssCleaned(line.toString()));
//            System.out.println("getCorrectDate=" + getCorrectDate(getXssCleaned(line.toString())));
            System.out.println("parse=" + parse(getXssCleaned(line.toString()), null));



                    String line = element.getElementsByClass("_0b1c1").tagName("span").first().text();
//                    String line = element.getElementsByClass("_0b1c1").tagName("span").first().child(0).text();
//                String line2 = element.getElementsByClass("_1e0a8").text();

                    System.out.println("____________________________________________________________________________________");
                    System.out.println("element:\n" + element);
                    System.out.println("\nline=" + line);
                    System.out.println("getCorrectSalary=" + getCorrectSalary(line.toString()));
                    System.out.println("salaryMax=" + salaryMax(getCorrectSalary(line.toString())));
                    System.out.println("salaryMin=" + salaryMin(getCorrectSalary((line.toString()))));



                    String line = element.getElementsByClass("_8d375").last().text().trim();

                    System.out.println("____________________________________________________________________________________");
                    System.out.println("element:\n" + element);
                    System.out.println("\nline=" + line);
                    System.out.println("getXssCleaned=" + getXssCleaned(line.toString()));
                    System.out.println("getCorrectDate=" + getCorrectDate(getXssCleaned(line.toString())));
                    System.out.println("parse=" + parse(getCorrectDate(getXssCleaned(line.toString())), null));



            String line = element.getElementsByTag("time").tagName("time").attr("datetime");

            System.out.println("____________________________________________________________________________________");
            System.out.println("element:\n" + element);
            System.out.println("\nline=" + line);
            System.out.println("getXssCleaned=" + getXssCleaned(line.toString()));
            System.out.println("getCorrectDate=" + LocalDate.parse(getXssCleaned(line.toString())));



                    String line = element.getElementsByClass("_0b1c1").tagName("span").first().text();

                    System.out.println("____________________________________________________________________________________");
                    System.out.println("element:\n" + element);
                    System.out.println("\nline=" + line);
                    System.out.println("getCorrectSalary=" + getCorrectSalary(line));
                    System.out.println("salaryMax=" + salaryMax(getCorrectSalary(line)));



*/