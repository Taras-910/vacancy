package ua.training.top.util.collect;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.training.top.model.Freshen;
import ua.training.top.to.VacancyTo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static ua.training.top.aggregator.installation.InstallationUtil.reasonDateLoading;
import static ua.training.top.aggregator.strategy.JobCareerStrategy.getCareerUrl;
import static ua.training.top.aggregator.strategy.LinkedinStrategy.getSalaryLinkedin;
import static ua.training.top.aggregator.strategy.LinkedinStrategy.getToLinkedin;
import static ua.training.top.aggregator.strategy.NofluffjobsStrategy.getToNofluffjobs;
import static ua.training.top.aggregator.strategy.WorkStrategy.getAddrWork;
import static ua.training.top.util.collect.data.DataUtil.*;
import static ua.training.top.util.collect.data.DateToUtil.defaultDate;
import static ua.training.top.util.collect.data.DateToUtil.getToLocalDate;
import static ua.training.top.util.collect.data.SalaryUtil.getToSalaries;
import static ua.training.top.util.collect.data.ToUtil.isToValid;
import static ua.training.top.util.collect.data.UrlUtil.getToUrl;
import static ua.training.top.util.collect.xss.XssUtil.xssClear;

public class ElementUtil {
    public static final Logger log = LoggerFactory.getLogger(ElementUtil.class);

    public static List<VacancyTo> getVacanciesDjinni(Elements elements, Freshen freshen) {
        List<VacancyTo> list = new ArrayList();
        for (Element element : elements) {
            try {
                LocalDate localDate = getToLocalDate(xssClear(element.getElementsByAttributeValueStarting("class", "text-date").text()));
                if (localDate.isAfter(reasonDateLoading)) {
                    String skills, salaries, title = getToTitle(xssClear(element.getElementsByClass("profile").tagName("a").text()));
                    skills = getToSkills(xssClear(element.getElementsByClass("list-jobs__description").text()));
                    salaries = xssClear(element.getElementsByClass("public-salary-item").text());
                    salaries = isEmpty(salaries) ? skills : salaries;
                    if (isToValid(freshen, getBuild(title).append(skills).toString())) {
                        VacancyTo v = new VacancyTo();
                        v.setTitle(getLinkIfEmpty(title));
                        v.setEmployerName(getToName(xssClear(element.getElementsByClass("list-jobs__details__info").tagName("a").first().child(1).text())));
                        v.setAddress(getLinkIfEmpty(xssClear(element.getElementsByAttributeValueStarting("class", "location-text").text())));
                        v.setSalaryMin(getToSalaries(salaries)[0]);
                        v.setSalaryMax(getToSalaries(salaries)[1]);
                        v.setUrl(getToUrl(djinni, xssClear(element.getElementsByClass("profile").first().attr("href"))));
                        v.setSkills(skills);
                        v.setReleaseDate(localDate);
                        list.add(v);
                    }
                }
            } catch (Exception e) {
                log.error(error, e.getLocalizedMessage(), element);
            }
        }
        ;
        return list;
    }

    public static List<VacancyTo> getVacanciesGrc(Elements elements, Freshen freshen) {
        List<VacancyTo> list = new ArrayList<>();
        for (Element element : elements) {
            try {
                LocalDate localDate = getToLocalDate(xssClear(element.getElementsByAttributeValueStarting("class", "vacancy-serp-item__publication-date").text()));
                if (localDate.isAfter(reasonDateLoading)) {
                    String skills, salaries, title = getToTitle(xssClear(element.getElementsByClass("resume-search-item__name").text().toLowerCase()));
                    skills = getToSkills(xssClear(element.getElementsByClass("g-user-content").text().toLowerCase()));
                    salaries = xssClear(element.getElementsByClass("vacancy-serp-item__sidebar").text());
                    if (isToValid(freshen, getBuild(title).append(skills).toString())) {
                        VacancyTo v = new VacancyTo();
                        v.setTitle(getLinkIfEmpty(title));
                        v.setEmployerName(getToName(xssClear(element.getElementsByAttributeValue("data-qa", "vacancy-serp__vacancy-employer").text())));
                        v.setAddress(getLinkIfEmpty(xssClear(element.getElementsByAttributeValue("data-qa", "vacancy-serp__vacancy-address").text())));
                        v.setSalaryMin(getToSalaries(salaries)[0]);
                        v.setSalaryMax(getToSalaries(salaries)[1]);
                        v.setUrl(xssClear(element.getElementsByClass("bloko-link").attr("href")));
                        v.setSkills(skills);
                        v.setReleaseDate(localDate);
                        list.add(v);
                    }
                }
            } catch (Exception e) {
                log.error(error, e.getLocalizedMessage(), element);
            }
        }
        ;
        return list;
    }

    public static List<VacancyTo> getVacanciesHabr(Elements elements, Freshen freshen) {
        List<VacancyTo> list = new ArrayList<>();
        for (Element element : elements) {
            try {
                LocalDate localDate = getToLocalDate(xssClear(element.getElementsByAttribute("datetime").attr("datetime")));
                if (localDate.isAfter(reasonDateLoading)) {
                    String skills, salaries, title = getToTitle(xssClear(element.getElementsByClass("vacancy-card__title").tagName("a").text()));
                    skills = getToSkills(xssClear(element.getElementsByClass("vacancy-card__skills").text()));
                    salaries = xssClear(element.getElementsByClass("basic-salary").text());
                    if (isToValid(freshen, getBuild(title).append(skills).toString())) {
                        VacancyTo v = new VacancyTo();
                        v.setTitle(getLinkIfEmpty(title));
                        v.setEmployerName(getToName(xssClear(element.getElementsByClass("vacancy-card__company").first().child(0).text())));
                        v.setAddress(getLinkIfEmpty(xssClear(element.getElementsByClass("vacancy-card__meta").tagName("a").first().text())));
                        v.setSalaryMin(getToSalaries(salaries)[0]);
                        v.setSalaryMax(getToSalaries(salaries)[1]);
                        v.setUrl(getToUrl(habr, xssClear(element.getElementsByClass("vacancy-card__icon-link").attr("href"))));
                        v.setSkills(skills);
                        v.setReleaseDate(localDate);
                        list.add(v);
                    }
                }
            } catch (Exception e) {
                log.error(error, e.getLocalizedMessage(), element);
            }
        }
        return list;
    }

    public static List<VacancyTo> getVacanciesJobCareer(Elements elements, Freshen freshen) {
        List<VacancyTo> list = new ArrayList<>();
        for (Element element : elements) {
            try {
                LocalDate localDate = getToLocalDate(xssClear(element.getElementsByTag("ul").tagName("li").text()));
                if (localDate.isAfter(reasonDateLoading)) {
                    String skills, salaries, title = getToTitle(xssClear(element.getElementsByTag("a").text()));
                    skills = getToSkills(xssClear(element.getElementsByTag("p").text()));
                    salaries = xssClear(element.getElementsByClass("vacancy-salary").text());
                    if (isToValid(freshen, getBuild(title).append(skills).toString())) {
                        VacancyTo v = new VacancyTo();
                        v.setTitle(getLinkIfEmpty(title));
                        v.setEmployerName(xssClear(element.getElementsByAttributeValueEnding("class", "vacancy-company").text()));
                        v.setAddress(getLinkIfEmpty(xssClear(element.getElementsByClass("vacancy-location").text())));
                        v.setSalaryMin(getToSalaries(salaries)[0]);
                        v.setSalaryMax(getToSalaries(salaries)[1]);
                        v.setUrl(getCareerUrl(xssClear(element.getElementsByTag("a").attr("href")), freshen));
                        v.setSkills(skills);
                        v.setReleaseDate(localDate);
                        list.add(v);
                    }
                }
            } catch (Exception e) {
                log.error(error, e.getLocalizedMessage(), element);
            }
        }
        return list;
    }

    public static List<VacancyTo> getVacanciesJobsMarket(Elements elements, Freshen freshen) {
        List<VacancyTo> list = new ArrayList<>();
        for (Element element : elements) {
            try {
                LocalDate localDate = getToLocalDate(xssClear(element.getElementsByTag("time").text()));
                if (localDate.isAfter(reasonDateLoading)) {
                    String title, salaries, skills = getToSkills(xssClear(element.getElementsByClass("card-body").text()));
                    title = getToTitle(xssClear(element.getElementsByClass("link").text()));
                    salaries = xssClear(element.getElementsByClass("text-muted clearfix d-block").tagName("strong").text().replaceAll(",", ""));
                    if (isToValid(freshen, getBuild(title).append(skills).toString())) {
                        VacancyTo v = new VacancyTo();
                        v.setTitle(getLinkIfEmpty(title));
                        v.setEmployerName(getToName(xssClear(element.getElementsByClass("cursor-pointer").text())));
                        v.setAddress(getLinkIfEmpty(xssClear(element.getElementsByClass("fa-map-marker-alt").next().text())));
                        v.setSalaryMin(getToSalaries(salaries)[0]);
                        v.setSalaryMax(getToSalaries(salaries)[1]);
                        v.setUrl(xssClear(element.getElementsByTag("a").attr("href")));
                        v.setSkills(skills);
                        v.setReleaseDate(localDate);
                        list.add(v);
                    }
                }
            } catch (Exception e) {
                log.error(error, e.getLocalizedMessage(), element);
            }
        }
        return list;
    }

    public static List<VacancyTo> getVacanciesJobs(Elements elements, Freshen freshen) {
        List<VacancyTo> list = new ArrayList();
        for (Element element : elements) {
            try {
                String skills, salaries, title = getToTitle(xssClear(element.getElementsByTag("a").first().text()));
                skills = getLinkIfEmpty(xssClear(element.getElementsByClass("sh-info").text()));
                salaries = xssClear(element.getElementsByClass("salary").text());
                if (isToValid(freshen, getBuild(title).append(skills).toString())) {
                    VacancyTo v = new VacancyTo();
                    v.setTitle(getLinkIfEmpty(title));
                    v.setEmployerName(getToName(xssClear(element.getElementsByTag("a").last().text())));
                    v.setAddress(getLinkIfEmpty(xssClear(element.getElementsByClass("cities").text())));
                    v.setSalaryMin(getToSalaries(salaries)[0]);
                    v.setSalaryMax(getToSalaries(salaries)[1]);
                    v.setUrl(xssClear(element.getElementsByTag("a").first().attr("href")));
                    v.setSkills(skills);
                    v.setReleaseDate(defaultDate);
                    list.add(v);
                }
            } catch (Exception e) {
                log.error(error, e.getLocalizedMessage(), element);
            }
        }
        return list;
    }

    public static List<VacancyTo> getVacanciesLinkedin(Elements elements, Freshen freshen) {
        List<VacancyTo> list = new ArrayList();
        for (Element element : elements) {
            try {
                LocalDate localDate = getToLocalDate(xssClear(element.getElementsByTag("time").tagName("time").attr("datetime")));
                if (localDate.isAfter(reasonDateLoading)) {
                    String title = getToTitle(xssClear(element.getElementsByClass("base-search-card__title").text()));

                    if (isToValid(freshen, getBuild(title).toString())) {
                        VacancyTo v = new VacancyTo();
                        v.setTitle(getLinkIfEmpty(title));
                        v.setEmployerName(getToName(xssClear(element.getElementsByClass("base-search-card__subtitle").tagName("a").text())));
                        v.setAddress(getToLinkedin(xssClear(element.getElementsByClass("job-search-card__location").text())));
                        v.setSalaryMin(getToSalaries(getSalaryLinkedin(title))[0]);
                        v.setSalaryMax(getToSalaries(getSalaryLinkedin(title))[1]);
                        v.setUrl(xssClear(element.getElementsByTag("a").first().attr("href")));
                        v.setSkills(link);
                        v.setReleaseDate(localDate);
                        list.add(v);
                    }
                }
            } catch (Exception e) {
                log.error(error, e.getLocalizedMessage(), element);
            }
        }
        return list;
    }

    public static List<VacancyTo> getNofluffjobsVacancies(Elements elements, Freshen freshen) {
        List<VacancyTo> list = new ArrayList<>();
        for (Element element : elements) {
            try {
                LocalDate localDate = getToLocalDate(element.getElementsByClass("new-label").text());
                if (localDate.isAfter(reasonDateLoading)) {
                    String skills, salaries, title = getToTitle(xssClear(element.getElementsByClass("posting-title__position").text()));
                    salaries = xssClear(element.getElementsByAttributeValueContaining("class", "salary").text());
                    skills = getToSkills(xssClear(element.getElementsByTag("common-posting-item-tag").text()));
                    if (isToValid(freshen, getBuild(title).append(skills).toString())) {
                        VacancyTo v = new VacancyTo();
                        v.setTitle(getLinkIfEmpty(title));
                        v.setEmployerName(xssClear(element.getElementsByClass("posting-title__company").text()).substring(2).trim());
                        v.setAddress(getToNofluffjobs(xssClear(element.getElementsByTag("nfj-posting-item-city").text())));
                        v.setSalaryMin(getToSalaries(salaries)[0]);
                        v.setSalaryMax(getToSalaries(salaries)[1]);
                        v.setUrl(getToUrl(nofluff, xssClear(element.getElementsByTag("a").attr("href"))));
                        v.setSkills(skills);
                        v.setReleaseDate(localDate);
                        list.add(v);
                    }
                }
            } catch (Exception e) {
                log.error(error, e.getLocalizedMessage(), element);
            }
        }
        return list;
    }

    public static List<VacancyTo> getVacanciesRabota(Elements elements, Freshen freshen) {
        List<VacancyTo> list = new ArrayList<>();
        for (Element element : elements) {
            try {
                LocalDate localDate = getToLocalDate(xssClear(element.getElementsByClass("publication-time").text()));
                if (localDate.isAfter(reasonDateLoading)) {
                    String skills, salaries, title = getToTitle(xssClear(element.getElementsByClass("card-title").text()));
                    skills = getToSkills(xssClear(element.getElementsByClass("card-description").text()));
                    salaries = xssClear(element.getElementsByClass("salary").text());
                    salaries = isEmpty(salaries) ? title : skills;
                    if (isToValid(freshen, getBuild(title).append(skills).toString())) {
                        VacancyTo v = new VacancyTo();
                        v.setTitle(getLinkIfEmpty(title));
                        v.setEmployerName(getToName(xssClear(element.getElementsByClass("company-name").text())));
                        v.setAddress(getLinkIfEmpty(xssClear(element.getElementsByClass("location").text())));
                        v.setSalaryMin(getToSalaries(salaries)[0]);
                        v.setSalaryMax(getToSalaries(salaries)[1]);
                        v.setUrl(getToUrl(rabota, xssClear(element.getElementsByTag("a").attr("href"))));
                        v.setSkills(skills);
                        v.setReleaseDate(localDate);
                        list.add(v);
                    }
                }
            } catch (Exception e) {
                log.error(error, e.getLocalizedMessage(), element);
            }
        }
        return list;
    }

    public static List<VacancyTo> getVacanciesIndeed(Elements elements, Freshen freshen) {
        List<VacancyTo> list = new ArrayList<>();
        for (Element element : elements) {
            try {
                LocalDate localDate = getToLocalDate(xssClear(element.getElementsByClass("date").text()));
                if (localDate.isAfter(reasonDateLoading)) {
                    String skills, title = getToTitle(xssClear(element.getElementsByAttribute("title").first().text()));
                    skills = getToSkills(xssClear(element.getElementsByClass("job-snippet").text()));
                    if (isToValid(freshen, getBuild(title).append(skills).toString())) {
                        VacancyTo v = new VacancyTo();
                        v.setTitle(getLinkIfEmpty(title));
                        v.setEmployerName(getToName(xssClear(xssClear(element.getElementsByClass("companyName").text()))));
                        v.setAddress(getLinkIfEmpty(xssClear(element.getElementsByClass("companyLocation").text())));
                        v.setSalaryMin(1);
                        v.setSalaryMax(1);
                        v.setUrl(getToUrl(indeed, xssClear(element.getElementsByTag("a").tagName("a").attr("data-jk"))));
                        v.setSkills(skills);
                        v.setReleaseDate(localDate);
                        list.add(v);
                    }
                }
            } catch (Exception e) {
                log.error(error, e.getLocalizedMessage(), element);
            }
        }
        return list;
    }

    public static List<VacancyTo> getVacanciesJooble(Elements elements, Freshen freshen) {
        List<VacancyTo> list = new ArrayList();
        for (Element element : elements) {
            try {
                LocalDate localDate;
                try {
                    localDate = getToLocalDate(xssClear(element.getElementsByClass("caption _04443").text()));
                } catch (Exception e) {
                    log.error(error, "LocalDate", e.getMessage());
                    localDate = defaultDate;
                }
                if (localDate.isAfter(reasonDateLoading)) {
                    String skills, salaries, title = getToTitle(xssClear(element.getElementsByTag("header").tagName("span").text()));
                    skills = getToSkills(xssClear(element.getElementsByClass("_10840").text()));
                    salaries = xssClear(element.getElementsByClass("a7943").text());
                    salaries = isEmpty(salaries) ? skills : salaries;
                    if (isToValid(freshen, getBuild(title).append(skills).toString())) {
                        VacancyTo v = new VacancyTo();
                        v.setTitle(getLinkIfEmpty(title));
                        v.setEmployerName(getToName(xssClear(element.getElementsByClass("efaa8").text())));
                        v.setAddress(getLinkIfEmpty(xssClear(Optional.of(element.getElementsByClass("caption d7cb2").text()).orElse(""))));
                        v.setSalaryMin(getToSalaries(salaries)[0]);
                        v.setSalaryMax(getToSalaries(salaries)[1]);
                        v.setUrl(getToUrl(jooble, xssClear(element.getElementsByTag("article").attr("id"))));
                        v.setSkills(skills);
                        v.setReleaseDate(localDate);
                        list.add(v);
                    }
                }
            } catch (Exception e) {
                log.error(error, e.getLocalizedMessage(), element);
            }
        }
        return list;
    }

    public static List<VacancyTo> getVacanciesWork(Elements elements, Freshen freshen) {
        List<VacancyTo> list = new ArrayList<>();
        for (Element element : elements) {
            try {
                LocalDate localDate = getToLocalDate(xssClear(element.getElementsByTag("a").first().attr("title")));
                if (localDate.isAfter(reasonDateLoading)) {
                    String employerName, skills, salaries, title = getToTitle(xssClear(element.getElementsByTag("a").first().text()));
                    skills = getToSkills(xssClear(element.getElementsByClass("overflow").text()));
                    salaries = xssClear(element.getElementsByTag("b").tagName("b").first().text());
                    employerName = xssClear(element.getElementsByTag("img").attr("alt"));
                    employerName = !isEmpty(employerName) ? employerName : xssClear(element.getElementsByClass("add-top-xs").tagName("span").tagName("b").eachText().get(0));
                    if (isToValid(freshen, getBuild(title).append(skills).toString())) {
                        VacancyTo v = new VacancyTo();
                        v.setTitle(getLinkIfEmpty(title));
                        v.setEmployerName(getToName(employerName));
                        v.setAddress(getLinkIfEmpty(getAddrWork(xssClear(element.getElementsByClass("add-top-xs").first().children().next().next().text()))));
                        v.setSalaryMin(getToSalaries(salaries)[0]);
                        v.setSalaryMax(getToSalaries(salaries)[1]);
                        v.setUrl(getToUrl(work, xssClear(element.getElementsByTag("a").attr("href"))));
                        v.setSkills(skills);
                        v.setReleaseDate(localDate);
                        list.add(v);
                    }
                }
            } catch (Exception e) {
                log.error(error, e.getLocalizedMessage(), element);
            }
        }
        return list;
    }
}
/*
jobs

                    System.out.println(i++ + "-".repeat(150));
                    System.out.println("element=\n"+element);
                    System.out.println("dateString     =" +defaultDate);
                    System.out.println("date           ="+ v.getReleaseDate());
                    System.out.println("titleString    ="+xssClear(element.getElementsByTag("a").first().text().trim()));
                    System.out.println("title          ="+v.getTitle());

                    System.out.println("emploNameString="+ xssClear(element.getElementsByTag("a").last().text().trim()));
                    System.out.println("employerName   ="+v.getEmployerName());

                    System.out.println("addressString  ="+xssClear(element.getElementsByClass("cities").text().trim()));
                    System.out.println("address        ="+v.getAddress());

                    System.out.println("salaryString   ="+ xssClear(element.getElementsByClass("salary").text()));
                    System.out.println("salaryMin      ="+v.getSalaryMin());
                    System.out.println("salaryMan      ="+v.getSalaryMax());

                    System.out.println("urlString      ="+ xssClear(element.getElementsByTag("a").first().attr("href")));
                    System.out.println("url            ="+v.getUrl());
                    System.out.println("skillsString   ="+ xssClear(element.getElementsByClass("sh-info").text().trim()));
                    System.out.println("skills         ="+v.getSkills());


                        System.out.println(i++ + "-".repeat(150));
                        System.out.println("salaryString   ="+ salaries);
                        System.out.println("salaryMin      ="+v.getSalaryMin());
                        System.out.println("salaryMan      ="+v.getSalaryMax());

*/
