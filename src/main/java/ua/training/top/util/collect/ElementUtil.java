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
import static ua.training.top.aggregator.strategy.JobsBGStrategy.*;
import static ua.training.top.aggregator.strategy.LinkedinStrategy.getToLinkedin;
import static ua.training.top.aggregator.strategy.NofluffjobsStrategy.getToNofluffAddress;
import static ua.training.top.aggregator.strategy.WorkStrategy.getAddrWork;
import static ua.training.top.util.collect.data.DataUtil.*;
import static ua.training.top.util.collect.data.DateToUtil.defaultDate;
import static ua.training.top.util.collect.data.DateToUtil.getToLocalDate;
import static ua.training.top.util.collect.data.PatternUtil.pattern_is_money_value;
import static ua.training.top.util.collect.data.SalaryUtil.getSalaryFromText;
import static ua.training.top.util.collect.data.SalaryUtil.getToSalaries;
import static ua.training.top.util.collect.data.ToUtil.isToValid;
import static ua.training.top.util.collect.data.UrlUtil.getToUrl;
import static ua.training.top.util.collect.xss.XssUtil.xssClear;

public class ElementUtil {
    public static final Logger log = LoggerFactory.getLogger(ElementUtil.class);

    public static List<VacancyTo> getVacanciesCaIndeed(Elements elements, Freshen freshen) {
        List<VacancyTo> list = new ArrayList<>();
        int i = 1;
        for (Element element : elements) {
            try {
                LocalDate localDate = getToLocalDate(xssClear(element.getElementsByClass("date").text()));
                if (localDate.isAfter(reasonDateLoading)) {
                    String skills, title = getToTitle(xssClear(element.getElementsByAttribute("title").first().text()));
                    skills = getToSkills(xssClear(element.getElementsByClass("job-snippet").text()));
                    if (isToValid(freshen, getJoin(title,skills))) {
                        VacancyTo v = new VacancyTo();
                        v.setTitle(getLinkIfEmpty(title));
                        v.setEmployerName(getToName(xssClear(element.getElementsByClass("companyName").text())));
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

    public static List<VacancyTo> getVacanciesCwJobs(Elements elements, Freshen freshen) {
        List<VacancyTo> list = new ArrayList<>();
        for (Element element : elements) {
            try {
                LocalDate localDate = getToLocalDate(xssClear(element.getElementsByAttributeValue("data-at", "job-item-timeago").text()));
                if (/*localDate.isAfter(reasonDateLoading)*/true) {
                    String salaries, skills, title = getToTitle(xssClear(element.getElementsByAttributeValue("data-at", "job-item-title").text()));
                    salaries = xssClear(element.getElementsByAttributeValue("data-at", "job-item-salary-info").text());
                    skills = xssClear(element.getElementsByAttribute("data-offer-meta-text-snippet").text());
                    if (true) {
                        VacancyTo v = new VacancyTo();
                        v.setTitle(getLinkIfEmpty(title));
                        v.setEmployerName(getToName(xssClear(element.getElementsByAttributeValue("data-at", "job-item-company-name").text())));
                        v.setAddress(getLinkIfEmpty(xssClear(element.getElementsByAttributeValue("jdata-at", "job-item-location").text())));
                        v.setSalaryMin(getToSalaries(salaries)[0]);
                        v.setSalaryMax(getToSalaries(salaries)[1]);
                        v.setUrl(getToUrl(cwjobs, xssClear(element.getElementsByTag("a").attr("href"))));
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
                    if (isToValid(freshen, getJoin(title,skills))) {
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
        return list;
    }

    public static List<VacancyTo> getVacanciesITJob(Elements elements, Freshen freshen) {
        List<VacancyTo> list = new ArrayList();
        for (Element element : elements) {
            LocalDate localDate = getToLocalDate(xssClear(element.getElementsByClass("date").text()));
            if (localDate.isAfter(reasonDateLoading)) {
                try {
                    String skills, title = getToTitle(xssClear(element.getElementsByClass("offer-name").first().text()));
                    skills = xssClear(element.getElementsByClass("offer-description").text());
                    title = isContains(skills, " ") ? title.substring(0, title.indexOf(skills.split(" ")[0])) : title;
                    Integer[] salaries = pattern_is_money_value.matcher(title).find() ? getToSalaries(getSalaryFromText(title)) : new Integer[]{1,1};
                    if (true) {
                        VacancyTo v = new VacancyTo();
                        v.setTitle(getLinkIfEmpty(title));
                        v.setEmployerName(getToName(xssClear(element.getElementsByClass("company").text())));
                        v.setAddress(getLinkIfEmpty(xssClear(element.getElementsByClass("location").text().replaceFirst("Location ", ""))));
                        v.setSalaryMin(salaries[0]);
                        v.setSalaryMax(salaries[1]);
                        v.setUrl(xssClear(element.getElementsByTag("a").attr("href")));
                        v.setSkills(getToSkills(skills));
                        v.setReleaseDate(localDate);
                        list.add(v);
                    }
                } catch (Exception e) {
                    log.error(error, e.getLocalizedMessage(), element);
                }
            }
        }
        return list;
    }

    public static List<VacancyTo> getVacanciesITJobsWatch(Elements elements, Freshen freshen) {
        List<VacancyTo> list = new ArrayList();
        int i = 1;
        for (Element element : elements) {
            String dateString = xssClear(element.getElementsByTag("time").text());
            LocalDate localDate = dateString. equals("30+") ? reasonDateLoading.plusDays(1) : getToLocalDate(dateString);
            if (localDate.isAfter(reasonDateLoading)) {
                try {
                    String address, salary, skills, title = getToTitle(xssClear(element.getElementsByClass("jobTitle").first().text()));
                    skills = xssClear(element.getElementsByClass("jobDescription").text());
                    salary = xssClear(element.getElementsByClass("jobDetails").tagName("span").text());
                    Integer[] salaries = getToSalaries(salary);
                    address = isContains(salary, "-") ? salary.split("-")[0] : salary;
                    if (true) {
                        VacancyTo v = new VacancyTo();
                        v.setTitle(getLinkIfEmpty(title));
                        v.setEmployerName(getToName(xssClear(element.getElementsByClass("company").text())));
                        v.setAddress(address);
                        v.setSalaryMin(salaries[0]);
                        v.setSalaryMax(salaries[1]);
                        v.setUrl(getToUrl(itJobsWatch, xssClear(element.getElementsByTag("a").attr("href"))));
                        v.setSkills(getToSkills(skills));
                        v.setReleaseDate(localDate);
                        list.add(v);
                    }
                } catch (Exception e) {
                    log.error(error, e.getLocalizedMessage(), element);
                }
            }
        }
        return list;
    }

    public static List<VacancyTo> getVacanciesJobBank(Elements elements, Freshen freshen) {
        List<VacancyTo> list = new ArrayList();
        for (Element element : elements) {
            LocalDate localDate = getToLocalDate(xssClear(element.getElementsByClass("date").text()));
            if (localDate.isAfter(reasonDateLoading)) {
                try {
                    String salaries, url, title = getToTitle(xssClear(element.getElementsByClass("noctitle").text()));
                    salaries = xssClear(element.getElementsByClass("salary").text());
                    url = xssClear(element.getElementsByTag("a").attr("href"));
                    if (true) {
                        VacancyTo v = new VacancyTo();
                        v.setTitle(getLinkIfEmpty(isContains(title, "Verified") ? title.split("Verified")[0] : title));
                        v.setEmployerName(getToName(xssClear(element.getElementsByClass("business").text())));
                        v.setAddress(getLinkIfEmpty(xssClear(element.getElementsByClass("location").text().replaceFirst("Location ", ""))));
                        v.setSalaryMin(getToSalaries(getJoin(salaries, " cad"))[0]);
                        v.setSalaryMax(getToSalaries(getJoin(salaries, " cad"))[1]);
                        v.setUrl(getToUrl(jobBank, isContains(url, ";") ? url.split(";")[0] : url));
                        v.setSkills(link);
                        v.setReleaseDate(localDate);
                        list.add(v);
                    }
                } catch (Exception e) {
                    log.error(error, e.getLocalizedMessage(), element);
                }
            }
        }
        return list;
    }

    public static List<VacancyTo> getVacanciesJobsBG(Elements elements, Freshen freshen) {
        List<VacancyTo> list = new ArrayList();
        for (Element element : elements) {
            String dateString = xssClear(element.getElementsByClass("card-date").text());
            LocalDate localDate = isEmpty(dateString) ? reasonDateLoading.plusDays(1) : getDateJobsBG(dateString);
            if (localDate.isAfter(reasonDateLoading)) {
                try {
                    String address, employerName, skills = xssClear(element.getElementsByAttribute("alt").eachAttr("alt").toString());
                    address = getLinkIfEmpty(xssClear(element.getElementsByClass("card-info card__subtitle").text()));
                    employerName = getToName(xssClear(element.getElementsByClass("secondary-text").text()));
                    Integer [] salaries = getToSalaries(xssClear(element.getElementsByTag("b").text()));
                    address = getAddress(address);
                    if (true) {
                        VacancyTo v = new VacancyTo();
                        v.setTitle(getLinkIfEmpty(getToTitle(xssClear(element.getElementsByTag("a").attr("title")))));
                        v.setEmployerName(employerName);
                        v.setAddress(getAddress(address));
                        v.setSalaryMin(salaries[0]);
                        v.setSalaryMax(salaries[1]);
                        v.setUrl(xssClear(element.getElementsByTag("a").attr("href")));
                        v.setSkills(skills.length() != 0 ? getSkills(employerName, skills) : skills);
                        v.setReleaseDate(localDate);
                        list.add(v);
                    }
                } catch (Exception e) {
                    log.error(error, e.getLocalizedMessage(), element);
                }
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
                    if (isToValid(freshen, getJoin(title,skills))) {
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
            LocalDate localDate = getToLocalDate(xssClear(element.getElementsByClass("date").text()));
            if (localDate.isAfter(reasonDateLoading)) {
                try {
                    String skills, salaries, title = getToTitle(xssClear(element.getElementsByTag("a").first().text()));
                    skills = getLinkIfEmpty(xssClear(element.getElementsByClass("sh-info").text()));
                    salaries = xssClear(element.getElementsByClass("salary").text());
                    if (isToValid(freshen, getJoin(title, skills))) {
                        VacancyTo v = new VacancyTo();
                        v.setTitle(getLinkIfEmpty(title));
                        v.setEmployerName(getToName(xssClear(element.getElementsByTag("a").last().text())));
                        v.setAddress(getLinkIfEmpty(xssClear(element.getElementsByClass("cities").text())));
                        v.setSalaryMin(getToSalaries(salaries)[0]);
                        v.setSalaryMax(getToSalaries(salaries)[1]);
                        v.setUrl(xssClear(element.getElementsByTag("a").first().attr("href")));
                        v.setSkills(skills);
                        v.setReleaseDate(localDate);
                        list.add(v);
                    }
                } catch (Exception e) {
                    log.error(error, e.getLocalizedMessage(), element);
                }
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
                    if (isToValid(freshen, title)) {
                        VacancyTo v = new VacancyTo();
                        v.setTitle(getLinkIfEmpty(title));
                        v.setEmployerName(getToName(xssClear(element.getElementsByClass("base-search-card__subtitle").tagName("a").text())));
                        v.setAddress(getToLinkedin(xssClear(element.getElementsByClass("job-search-card__location").text())));
                        v.setSalaryMin(pattern_is_money_value.matcher(title).find() ? getToSalaries(getSalaryFromText(title))[0] : 1);
                        v.setSalaryMax(pattern_is_money_value.matcher(title).find() ? getToSalaries(getSalaryFromText(title))[1] : 1);
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
                    if (isToValid(freshen, getJoin(title,skills))) {
                        VacancyTo v = new VacancyTo();
                        v.setTitle(getLinkIfEmpty(title));
                        v.setEmployerName(xssClear(element.getElementsByClass("posting-title__company").text()).substring(2).trim());
                        v.setAddress(getToNofluffAddress(xssClear(element.getElementsByTag("nfj-posting-item-city").text())));
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
                    if (isToValid(freshen, getJoin(title,skills))) {
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

    public static List<VacancyTo> getVacanciesReed(Elements elements, Freshen freshen) {
        List<VacancyTo> list = new ArrayList<>();
        for (Element element : elements) {
            try {
                LocalDate localDate = getToLocalDate(xssClear(element.getElementsByClass("job-result-heading__posted-by").text()));
                if (/*localDate.isAfter(reasonDateLoading)*/true) {
                    String salaries, title = getToTitle(xssClear(element.getElementsByTag("a").attr("title")));
                    salaries = xssClear(element.getElementsByClass("job-metadata__item--salary").text());
                    if (true) {
                        VacancyTo v = new VacancyTo();
                        v.setTitle(getLinkIfEmpty(title));
                        v.setEmployerName(getToName(xssClear(element.getElementsByClass("company-name").text())));
                        v.setAddress(getLinkIfEmpty(xssClear(element.getElementsByClass("job-metadata__item--location").text())));
                        v.setSalaryMin(getToSalaries(salaries)[0]);
                        v.setSalaryMax(getToSalaries(salaries)[1]);
                        v.setUrl(getToUrl(reed, xssClear(element.getElementsByTag("a").attr("href"))));
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

    public static List<VacancyTo> getVacanciesIndeed(Elements elements, Freshen freshen) {
        List<VacancyTo> list = new ArrayList<>();
        for (Element element : elements) {
            try {
                LocalDate localDate = getToLocalDate(xssClear(element.getElementsByClass("date").text()));
                if (localDate.isAfter(reasonDateLoading)) {
                    String skills, title = getToTitle(xssClear(element.getElementsByAttribute("title").first().text()));
                    skills = getToSkills(xssClear(element.getElementsByClass("job-snippet").text()));
                    if (isToValid(freshen, getJoin(title,skills))) {
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

    public static List<VacancyTo> getVacanciesJooble(Elements elements, Freshen freshen, String country) {
        List<VacancyTo> list = new ArrayList();
        for (Element element : elements) {
            try {
                LocalDate localDate;
                try {
                    localDate = getToLocalDate(xssClear(element.getElementsByClass("caption e0VAhp"/*"caption _04443"*/).text()));
                } catch (Exception e) {
                    log.error(error, "LocalDate", e.getMessage());
                    localDate = defaultDate;
                }
                if (localDate.isAfter(reasonDateLoading)) {
                    String skills, salaries, title = getToTitle(xssClear(element.getElementsByTag("header").tagName("span").text()));
                    skills = getToSkills(xssClear(element.getElementsByClass("_9jGwm1"/*"_10840"*/).text()));
                    salaries = xssClear(element.getElementsByClass("jNebTl"/*"a7943"*/).text());
                    salaries = isEmpty(salaries) ? getJoin(skills, "month") : salaries;
                    if (isToValid(freshen, getJoin(title,skills))) {
                        VacancyTo v = new VacancyTo();
                        v.setTitle(getLinkIfEmpty(title));
                        v.setEmployerName(getToName(xssClear(element.getElementsByClass("Ya0gV9"/*"efaa8"*/).text())));
                        v.setAddress(getLinkIfEmpty(xssClear(Optional.of(element.getElementsByClass("caption _2_Ab4T"/*"caption d7cb2"*/).text()).orElse(""))));
                        v.setSalaryMin(getToSalaries(salaries)[0]);
                        v.setSalaryMax(getToSalaries(salaries)[1]);
                        v.setUrl(getJoin("https://", country, getToUrl(jooble, xssClear(element.getElementsByTag("article").attr("id")))));
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
                    if (isToValid(freshen, getJoin(title, skills))) {
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

    public static List<VacancyTo> getVacanciesZaplata(Elements elements, Freshen freshen) {
        List<VacancyTo> list = new ArrayList<>();
        for (Element element : elements) {
            try {
                String location = xssClear(element.getElementsByClass("location").text());
                String[] dateAndAddress = isContains(location, ", ") ? location.split(", ") : new String[]{"",""};
                LocalDate localDate = getToLocalDate(dateAndAddress[0]);
                if (localDate.isAfter(reasonDateLoading)) {
                    String employerName, salaries, title = getToTitle(xssClear(element.getElementsByTag("a").first().text()));
                    salaries = xssClear(element.getElementsByClass("is_visibility_salary").text()).replaceAll("Зарплата с: ", "");
                    employerName = xssClear(element.getElementsByClass("c4").text());
                    if (true) {
                        VacancyTo v = new VacancyTo();
                        v.setTitle(getLinkIfEmpty(title));
                        v.setEmployerName(getToName(employerName));
                        v.setAddress(dateAndAddress[1]);
                        v.setSalaryMin(getToSalaries(salaries)[0]);
                        v.setSalaryMax(getToSalaries(salaries)[1]);
                        v.setUrl(xssClear(element.getElementsByTag("a").attr("href")));
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
// cwJobs
/* cwJobs
*                         System.out.println(i++ + "-".repeat(150));
                        System.out.println("dateString     =" + xssClear(element.getElementsByAttributeValue("data-at", "job-item-timeago").text()));
                        System.out.println("date           =" + v.getReleaseDate());
                        System.out.println("titleString    =" + xssClear(element.getElementsByAttributeValue("data-at", "job-item-title").text()));
                        System.out.println("title          =" + v.getTitle());

                        System.out.println("emploNameString=" + xssClear(element.getElementsByAttributeValue("data-at", "job-item-company-name").text()));
                        System.out.println("employerName   =" + v.getEmployerName());

                        System.out.println("addressString  =" + xssClear(element.getElementsByAttributeValue("jdata-at", "job-item-location").text()));
                        System.out.println("address        =" + v.getAddress());

                        System.out.println("salaryString   =" + xssClear(element.getElementsByAttributeValue("data-at", "job-item-salary-info").text()));
                        System.out.println("salaryMin      =" + v.getSalaryMin());
                        System.out.println("salaryMan      =" + v.getSalaryMax());

                        System.out.println("urlString      =" + xssClear(element.getElementsByTag("a").attr("href")));
                        System.out.println("url            =" + v.getUrl());
                        System.out.println("skillsString   =" + xssClear(element.getElementsByAttribute("data-offer-meta-text-snippet").text()));
                        System.out.println("skills         =" + v.getSkills());

*/
// djinni
/* djinni
                        System.out.println(i++ + "-".repeat(150));
//                        System.out.println("element=\n"+element);
                        System.out.println("dateString     =" + xssClear(element.getElementsByAttributeValueStarting("class", "text-date").text()));
                        System.out.println("date           ="+ v.getReleaseDate());
                        System.out.println("titleString    ="+ xssClear(element.getElementsByClass("profile").tagName("a").text()));
                        System.out.println("title          ="+v.getTitle());

                        System.out.println("emploNameString="+ xssClear(element.getElementsByClass("list-jobs__details__info").tagName("a").first().child(1).text()));
                        System.out.println("employerName   ="+v.getEmployerName());

                        System.out.println("addressString  ="+xssClear(element.getElementsByAttributeValueStarting("class", "location-text").text()));
                        System.out.println("address        ="+v.getAddress());

                        System.out.println("salaryString   ="+ xssClear(element.getElementsByClass("public-salary-item").text()));
                        System.out.println("salaryMin      ="+v.getSalaryMin());
                        System.out.println("salaryMan      ="+v.getSalaryMax());

                        System.out.println("urlString      ="+ xssClear(element.getElementsByClass("profile").first().attr("href")));
                        System.out.println("url            ="+v.getUrl());
                        System.out.println("skillsString   ="+ xssClear(element.getElementsByClass("list-jobs__description").text()));
                        System.out.println("skills         ="+v.getSkills());
 */
// itJob
/* itJob
                        System.out.println(i++ + "+".repeat(150));
                        if (v.getTitle().contains("Verified"))System.out.println("element=\n" + element);
                        System.out.println("-".repeat(150+String.valueOf(i).length()));
                        System.out.println("dateString     =" + xssClear(element.getElementsByClass("date").text()));
                        System.out.println("date           =" + v.getReleaseDate());
                        System.out.println("titleString    =" + title);
                        System.out.println("title          =" + v.getTitle());

                        System.out.println("emploNameString=" + xssClear(element.getElementsByClass("company").text()));
                        System.out.println("employerName   =" + v.getEmployerName());

                        System.out.println("addressString  =" + xssClear(element.getElementsByClass("location").text()));
                        System.out.println("address        =" + v.getAddress());

                        System.out.println("salaryString1  =" + xssClear(element.getElementsByClass("salary").text()));
                        System.out.println("salaryMin      =" + v.getSalaryMin());
                        System.out.println("salaryMan      =" + v.getSalaryMax());

                        System.out.println("urlString      =" + xssClear(element.getElementsByTag("a").attr("href")));
                        System.out.println("url            =" + v.getUrl());
                        System.out.println("skillsString   =" + xssClear(element.getElementsByClass("skills").text()));
                        System.out.println("skills         =" + v.getSkills());
*/
// itJobsWatch
/*    itJobsWatch
                        System.out.println(i++ + "-".repeat(150));

                        System.out.println("dateString    =" + xssClear(element.getElementsByTag("time").text()));
                        System.out.println("date           =" + v.getReleaseDate());
                        System.out.println("titleString    =" + xssClear(element.getElementsByClass("jobTitle").first().text()));
                        System.out.println("title          =" + v.getTitle());

                        System.out.println("emploNameString=" + xssClear(element.getElementsByClass("company").text()));
                        System.out.println("employerName   =" + v.getEmployerName());

                        System.out.println("addressString  =" + xssClear(element.getElementsByClass("jobDetails").tagName("span").text().split("-")[0]));
                        System.out.println("address        =" + v.getAddress());

                        System.out.println("salaryString   =" + xssClear(element.getElementsByClass("jobDetails").tagName("span").text()));
                        System.out.println("salaryMin      =" + v.getSalaryMin());
                        System.out.println("salaryMan      =" + v.getSalaryMax());

                        System.out.println("urlString      =" + xssClear(element.getElementsByTag("a").attr("href")));
                        System.out.println("url            =" + v.getUrl());

                        System.out.println("skillsString  =" + xssClear(element.getElementsByClass("jobDescription").toString()));
                        System.out.println("skills         =" + v.getSkills());

*/
// jobBank
/* jobBank
                        System.out.println(i++ + "+".repeat(150));
                        if (v.getTitle().contains("Verified"))System.out.println("element=\n" + element);
                        System.out.println("-".repeat(150+String.valueOf(i).length()));
                        System.out.println("dateString     =" + xssClear(element.getElementsByClass("date").text()));
                        System.out.println("date           =" + v.getReleaseDate());
                        System.out.println("titleString    =" + xssClear(element.getElementsByClass("noctitle").text()));
                        System.out.println("title          =" + v.getTitle());

                        System.out.println("emploNameString=" + xssClear(element.getElementsByClass("business").text()));
                        System.out.println("employerName   =" + v.getEmployerName());

                        System.out.println("addressString  =" + xssClear(element.getElementsByClass("location").text()));
                        System.out.println("address        =" + v.getAddress());

                        System.out.println("salaryString1  =" + xssClear(element.getElementsByClass("salary").text()));
                        System.out.println("salaryMin      =" + v.getSalaryMin());
                        System.out.println("salaryMan      =" + v.getSalaryMax());

                        System.out.println("urlString      =" + xssClear(element.getElementsByTag("a").attr("href").split(";")[0]));
                        System.out.println("url            =" + v.getUrl());
                        System.out.println("skillsString   =" + link);
                        System.out.println("skills         =" + v.getSkills());
* */
// jobsBG
/* jobsBG
                        System.out.println(i++ + "+".repeat(150));
                        if (v.getTitle().contains("Verified"))System.out.println("element=\n" + element);
                        System.out.println("-".repeat(150+String.valueOf(i).length()));
                        System.out.println("dateString     =" + xssClear(element.getElementsByClass("card-date").text()));
                        System.out.println("date           =" + v.getReleaseDate());
                        System.out.println("titleString    =" + xssClear(element.getElementsByTag("a").attr("title")));
                        System.out.println("title          =" + v.getTitle());

                        System.out.println("emploNameString=" + xssClear(element.getElementsByClass("secondary-text").text()));
                        System.out.println("employerName   =" + v.getEmployerName());

                        System.out.println("addressString  =" + xssClear(element.getElementsByClass("card-info card__subtitle").text()));
                        System.out.println("address        =" + v.getAddress());

                        System.out.println("salaryString1  =" + xssClear(element.getElementsByTag("b").text()));
                        System.out.println("salaryMin      =" + v.getSalaryMin());
                        System.out.println("salaryMan      =" + v.getSalaryMax());

                        System.out.println("urlString      =" + xssClear(element.getElementsByTag("a").attr("href")));
                        System.out.println("url            =" + v.getUrl());

                        System.out.println("skillsString  =" + xssClear(element.getElementsByAttribute("alt").eachAttr("alt").toString()));
                        System.out.println("skills         =" + v.getSkills());
*/
// jobsDuo
/*   jobsDuo
                        System.out.println(i++ + "-".repeat(150));
                        System.out.println("element=\n" + element);
                        System.out.println("dateString     =" + xssClear(element.getElementsByClass("date").text()));
                        System.out.println("date           =" + v.getReleaseDate());
                        System.out.println("titleString    =" + xssClear(element.getElementsByTag("a").first().text().trim()));
                        System.out.println("title          =" + v.getTitle());

                        System.out.println("emploNameString=" + xssClear(element.getElementsByTag("a").last().text().trim()));
                        System.out.println("employerName   =" + v.getEmployerName());

                        System.out.println("addressString  =" + xssClear(element.getElementsByClass("cities").text().trim()));
                        System.out.println("address        =" + v.getAddress());

                        System.out.println("salaryString   =" + xssClear(element.getElementsByClass("salary").text()));
                        System.out.println("salaryMin      =" + v.getSalaryMin());
                        System.out.println("salaryMan      =" + v.getSalaryMax());

                        System.out.println("urlString      =" + xssClear(element.getElementsByTag("a").first().attr("href")));
                        System.out.println("url            =" + v.getUrl());
                        System.out.println("skillsString   =" + xssClear(element.getElementsByClass("sh-info").text().trim()));
                        System.out.println("skills         =" + v.getSkills());
*/
// indeed
/* indeed


                        System.out.println(i++ + "-".repeat(150));
                        System.out.println("element=\n"+element);
                        System.out.println("dateString     =" +xssClear(element.getElementsByClass("date").text()));
                        System.out.println("date           ="+ v.getReleaseDate());
                        System.out.println("titleString    ="+xssClear(xssClear(element.getElementsByAttribute("title").first().text())));
                        System.out.println("title          ="+v.getTitle());

                        System.out.println("emploNameString="+ xssClear(element.getElementsByClass("companyName").text()));
                        System.out.println("employerName   ="+v.getEmployerName());

                        System.out.println("addressString  ="+xssClear(element.getElementsByClass("companyLocation").text().trim()));
                        System.out.println("address        ="+v.getAddress());

                        System.out.println("salaryString   ="+ "0");
                        System.out.println("salaryMin      ="+v.getSalaryMin());
                        System.out.println("salaryMan      ="+v.getSalaryMax());

                        System.out.println("urlString      ="+ xssClear(element.getElementsByTag("a").tagName("a").attr("data-jk")));
                        System.out.println("url            ="+v.getUrl());
                        System.out.println("skillsString   ="+ xssClear(element.getElementsByClass("job-snippet").text().trim()));
                        System.out.println("skills         ="+v.getSkills());
                        i++;

*/
// linkedin
/* linkedin

                        System.out.println(i++ + "-".repeat(150));
                        System.out.println("dateString     =" + xssClear(element.getElementsByTag("time").tagName("time").attr("datetime")));
                        System.out.println("date           ="+ v.getReleaseDate());
                        System.out.println("titleString    ="+ xssClear(element.getElementsByClass("base-search-card__title").text()));
                        System.out.println("title          ="+v.getTitle());

                        System.out.println("emploNameString="+ xssClear(element.getElementsByClass("base-search-card__subtitle").tagName("a").text()));
                        System.out.println("employerName   ="+v.getEmployerName());

                        System.out.println("addressString  ="+xssClear(element.getElementsByClass("job-search-card__location").text()));
                        System.out.println("address        ="+v.getAddress());

                        System.out.println("salaryString   ="+ getSalaryFromText(title));
                        System.out.println("salaryMin      ="+v.getSalaryMin());
                        System.out.println("salaryMan      ="+v.getSalaryMax());

                        System.out.println("urlString      ="+ xssClear(element.getElementsByTag("a").first().attr("href")));
                        System.out.println("url            ="+v.getUrl());
                        System.out.println("skillsString   ="+xssClear(element.getElementsByClass("sh-info").text()));
                        System.out.println("skills         ="+v.getSkills());

*/
// rabota
/* rabota страница - js-функция
                        System.out.println(i++ + "-".repeat(150));
                        System.out.println("element=\n" + element);
                        System.out.println("dateString     =" + xssClear(element.getElementsByClass("publication-time").text()));
                        System.out.println("date           =" + v.getReleaseDate());
                        System.out.println("titleString    =" + xssClear(element.getElementsByClass("card-title").text()));
                        System.out.println("title          =" + v.getTitle());

                        System.out.println("emploNameString=" + xssClear(element.getElementsByClass("company-name").text()));
                        System.out.println("employerName   =" + v.getEmployerName());

                        System.out.println("addressString  =" + xssClear(element.getElementsByClass("location").text()));
                        System.out.println("address        =" + v.getAddress());

                        System.out.println("salaryString1  =" + xssClear(element.getElementsByClass("salary").text()));
                        System.out.println("salaryString2  =" + salaries);
                        System.out.println("salaryMin      =" + v.getSalaryMin());
                        System.out.println("salaryMan      =" + v.getSalaryMax());

                        System.out.println("urlString      =" + xssClear(element.getElementsByTag("a").attr("href")));
                        System.out.println("url            =" + v.getUrl());
                        System.out.println("skillsString   =" + xssClear(element.getElementsByClass("card-description").text()));
                        System.out.println("skills         =" + v.getSkills());
 */
// reed
/*   reed
                        System.out.println(i++ + "-".repeat(150));
                        System.out.println("dateString     =" + xssClear(element.getElementsByClass("job-result-heading__posted-by").text()));
                        System.out.println("date           =" + v.getReleaseDate());
                        System.out.println("titleString    =" + xssClear(element.getElementsByTag("a").attr("title")));
                        System.out.println("title          =" + v.getTitle());

                        System.out.println("emploNameString=" + xssClear(element.getElementsByClass("company-name").text()));
                        System.out.println("employerName   =" + v.getEmployerName());

                        System.out.println("addressString  =" + xssClear(element.getElementsByClass("job-metadata__item--location").text()));
                        System.out.println("address        =" + v.getAddress());

                        System.out.println("salaryString1  =" + xssClear(element.getElementsByClass("job-metadata__item--salary").text()));
                        System.out.println("salaryMin      =" + v.getSalaryMin());
                        System.out.println("salaryMan      =" + v.getSalaryMax());

                        System.out.println("urlString      =" + xssClear(element.getElementsByTag("a").attr("href")));
                        System.out.println("url            =" + v.getUrl());
                        System.out.println("skillsString   =" + xssClear(element.getElementsByClass("skills").text()));
                        System.out.println("skills         =" + v.getSkills());

*/
// jooble
/*jooble
                        System.out.println(i++ + "+".repeat(150));
                        if (v.getSalaryMin() != 1 || v.getSalaryMax() != 1)System.out.println("element=\n" + element);
                        System.out.println(i++ + "-".repeat(150));
                        System.out.println("dateString     =" + xssClear(element.getElementsByClass("caption e0VAhp").text()));
                        System.out.println("date           =" + v.getReleaseDate());
                        System.out.println("titleString    =" + xssClear(element.getElementsByTag("header").tagName("span").text()));
                        System.out.println("title          =" + v.getTitle());

                        System.out.println("emploNameString=" + xssClear(element.getElementsByClass("Ya0gV9").text()));
                        System.out.println("employerName   =" + v.getEmployerName());

                        System.out.println("addressString  =" + xssClear(element.getElementsByClass("caption _2_Ab4T").text()));
                        System.out.println("address        =" + v.getAddress());

                        System.out.println("salaryString1  =" + xssClear(element.getElementsByClass("jNebTl").text()));
                        System.out.println("salaryString2  =" + salaries);
                        System.out.println("salaryMin      =" + v.getSalaryMin());
                        System.out.println("salaryMan      =" + v.getSalaryMax());

                        System.out.println("urlString      =" + xssClear(element.getElementsByTag("article").attr("id")));
                        System.out.println("url            =" + v.getUrl());
                        System.out.println("skillsString   =" + xssClear(element.getElementsByClass("_9jGwm1").text()));
                        System.out.println("skills         =" + v.getSkills());
*/
// zaplata
/* zaplata
                        System.out.println(i++ + "+".repeat(150));
                        if (v.getTitle().contains("Verified"))System.out.println("element=\n" + element);
                        System.out.println("-".repeat(150+String.valueOf(i).length()));
                        System.out.println("dateAndAddress =" + Arrays.toString(dateAndAddress));

                        System.out.println("dateString     =" + dateAndAddress[0]);
                        System.out.println("date           =" + v.getReleaseDate());
                        System.out.println("titleString    =" + xssClear(element.getElementsByTag("a").first().text()));
                        System.out.println("title          =" + v.getTitle());

                        System.out.println("emploNameString=" + xssClear(element.getElementsByClass("c4").text()));
                        System.out.println("employerName   =" + v.getEmployerName());

                        System.out.println("addressString  =" + dateAndAddress[1]);
                        System.out.println("address        =" + v.getAddress());

                        System.out.println("salaryString1  =" + xssClear(element.getElementsByClass("is_visibility_salary").text()));
                        System.out.println("salaryMin      =" + v.getSalaryMin());
                        System.out.println("salaryMan      =" + v.getSalaryMax());

                        System.out.println("urlString      =" + xssClear(element.getElementsByTag("a").attr("href")));
                        System.out.println("url            =" + v.getUrl());

                        System.out.println("skillsString  =" + xssClear(element.getElementsByAttribute("alt").eachAttr("alt").toString()));
                        System.out.println("skills         =" + v.getSkills());

*/
}

