package ua.training.top.util.aggregatorUtil;

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
import java.util.stream.Collectors;

import static java.util.List.of;
import static ua.training.top.aggregator.InstallationUtil.reasonDateLoading;
import static ua.training.top.aggregator.strategies.JobsBGStrategy.getAddress;
import static ua.training.top.aggregator.strategies.JobsBGStrategy.getDateJobsBG;
import static ua.training.top.aggregator.strategies.JobsMarketStrategy.getDateJobsMarket;
import static ua.training.top.aggregator.strategies.NofluffjobsStrategy.getToNofluffAddress;
import static ua.training.top.aggregator.strategies.UAJoobleStrategy.getJoobleDate;
import static ua.training.top.util.MessageUtil.error;
import static ua.training.top.util.aggregatorUtil.data.CommonUtil.*;
import static ua.training.top.util.aggregatorUtil.data.ConstantsUtil.*;
import static ua.training.top.util.aggregatorUtil.data.DateToUtil.defaultDate;
import static ua.training.top.util.aggregatorUtil.data.DateToUtil.getToLocalDate;
import static ua.training.top.util.aggregatorUtil.data.PatternUtil.pattern_is_money_value;
import static ua.training.top.util.aggregatorUtil.data.SalaryToUtil.getToSalaries;
import static ua.training.top.util.aggregatorUtil.data.ToUtil.*;
import static ua.training.top.util.aggregatorUtil.xss.XssUtil.xssClear;

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
                    skills = xssClear(element.getElementsByClass("job-snippet").text());
                    if (isToValid(freshen, getJoin(title, skills))) {
                        VacancyTo v = new VacancyTo();
                        v.setTitle(getLinkIfEmpty(title));
                        v.setEmployerName(getToName(xssClear(element.getElementsByClass("companyName").text())));
                        v.setAddress(getLinkIfEmpty(xssClear(element.getElementsByClass("companyLocation").text())));
                        v.setSalaryMin(1);
                        v.setSalaryMax(1);
                        v.setUrl(getToUrl(indeed, xssClear(element.getElementsByTag("a").tagName("a").attr("data-jk"))));
                        v.setSkills(getToSkills(skills, freshen));
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
                        v.setSkills(getToSkills(skills, freshen));
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
                String date = xssClear(element.getElementsByAttributeValueStarting("class", "text-date").text());
                LocalDate localDate = getToLocalDate(date);
                if (localDate.isAfter(reasonDateLoading)) {
                    String skills, salary, title = getToTitle(xssClear(element.getElementsByClass("profile").tagName("a").text()));
                    skills = xssClear(element.getElementsByClass("list-jobs__description").text());
                    salary = xssClear(element.getElementsByClass("public-salary-item").text());
                    /*salary = isEmpty(salary) ? skills : salary;*/
                    Integer[] salaries = getToSalaries(salary.replaceAll(date, ""));
                    if (isToValid(freshen, getJoin(title, skills))) {
                        VacancyTo v = new VacancyTo();
                        v.setTitle(getLinkIfEmpty(title));
                        v.setEmployerName(getToName(xssClear(element.getElementsByClass("list-jobs__details__info").tagName("a").first().child(1).text())));
                        v.setAddress(getLinkIfEmpty(xssClear(element.getElementsByAttributeValueStarting("class", "location-text").text())));
                        v.setSalaryMin(salaries[0]);
                        v.setSalaryMax(salaries[1]);
                        v.setUrl(getToUrl(djinni, xssClear(element.getElementsByClass("profile").first().attr("href"))));
                        v.setSkills(getToSkills(skills, freshen));
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

    public static List<VacancyTo> getITJob(Elements elements, Freshen freshen) {
        List<VacancyTo> list = new ArrayList();
        for (Element element : elements) {
            LocalDate localDate = getToLocalDate(xssClear(element.getElementsByClass("date").text()));
            if (localDate.isAfter(reasonDateLoading)) {
                try {
                    Elements elementsByTag = element.getElementsByTag("a");
                    String skills, address = xssClear(element.getElementsByClass("location").text());
                    skills = xssClear(element.getElementsByClass("offer-description").text());
                    Integer[] salaries = getToSalaries(skills);
                    if (elementsByTag.size() >= 4 && xssClear(elementsByTag.get(3).ownText()).equals(address)) {
                        VacancyTo v = new VacancyTo();
                        v.setTitle(getLinkIfEmpty(xssClear(elementsByTag.get(1).ownText())));
                        v.setEmployerName(xssClear(elementsByTag.get(2).ownText()));
                        v.setAddress(xssClear(elementsByTag.get(3).ownText()));
                        v.setSalaryMin(salaries[0]);
                        v.setSalaryMax(salaries[1]);
                        v.setUrl(xssClear(element.getElementsByTag("a").attr("href")));
                        v.setSkills(getToSkills(skills, freshen));
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

    public static List<VacancyTo> getITJobsWatch(Elements elements, Freshen freshen) {
        List<VacancyTo> list = new ArrayList();
        for (Element element : elements) {
            Elements listData = element.select("span, div");
            String url, dateString = xssClear(element.getElementsByTag("time").text());
            LocalDate localDate = isContains(dateString, "30+") ? reasonDateLoading.plusDays(1) : getToLocalDate(dateString);
            url = xssClear(element.getElementsByTag("a").attr("href"));
            if (localDate.isAfter(reasonDateLoading) && listData.size() >= 7 && !url.contains("?jr=")) {
                try {
                    String salary, skills, title = getToTitle(xssClear(element.getElementsByClass("jobTitle").first().text()));
                    skills = xssClear(listData.get(4).ownText());
                    salary = xssClear(listData.get(7).ownText());
                    Integer[] salaries = getToSalaries(isMatch(of("Daily", "Annual"), salary) ? salary : getJoin(salary, " year"));
                    if (isToValid(freshen, getJoin(title, skills))) {
                        VacancyTo v = new VacancyTo();
                        v.setTitle(getLinkIfEmpty(title));
                        v.setEmployerName(xssClear(listData.get(3).ownText()));
                        v.setAddress(xssClear(listData.get(2).ownText()).split(" -")[0]);
                        v.setSalaryMin(salaries[0]);
                        v.setSalaryMax(salaries[1]);
                        v.setUrl(getToUrl(itJobsWatch, url));
                        v.setSkills(getToSkills(skills, freshen));
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

    public static List<VacancyTo> getJobBank(Elements elements, Freshen freshen) {
        List<VacancyTo> list = new ArrayList();
        for (Element element : elements) {
            LocalDate localDate = getToLocalDate(xssClear(element.getElementsByClass("date").text()));
            if (localDate.isAfter(reasonDateLoading)) {
                try {
                    String url, title = getToTitle(xssClear(element.getElementsByClass("noctitle").text()));
                    url = xssClear(element.getElementsByTag("a").attr("href"));
                    Integer[] salaries = getToSalaries(getJoin(xssClear(element.getElementsByClass("salary").text()), " cad"));
                    if (isToValid(freshen, title)) {
                        VacancyTo v = new VacancyTo();
                        v.setTitle(getLinkIfEmpty(isContains(title, "Verified") ? title.split("Verified")[0] : title));
                        v.setEmployerName(getToName(xssClear(element.getElementsByClass("business").text())));
                        v.setAddress(getLinkIfEmpty(xssClear(element.getElementsByClass("location").text().replaceFirst("Location ", ""))));
                        v.setSalaryMin(salaries[0]);
                        v.setSalaryMax(salaries[1]);
                        v.setUrl(getToUrl(jobBank, isContains(url, ";") ? url.split(";")[0] : url));
                        v.setSkills(getToSkills("", freshen));
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

    public static List<VacancyTo> getJobsBG(Elements elements, Freshen freshen) {
        List<VacancyTo> list = new ArrayList();
        int i = 1;
        for (Element element : elements) {
            String dateString = xssClear(element.getElementsByClass("card-date").text());
            LocalDate localDate = isEmpty(dateString) ? reasonDateLoading.plusDays(1) : getDateJobsBG(dateString);
            if (localDate.isAfter(reasonDateLoading)) {
                try {
                    String title, skills, employerName, salary, address = xssClear(element.getElementsByClass("card-info").first().text());
                    employerName = getToName(xssClear(element.getElementsByClass("secondary-text").text()));
                    title = getToTitle(xssClear(element.getElementsByTag("a").attr("title")));
                    skills = element.getElementsByAttribute("alt").eachAttr("alt").stream()
                            .filter(e -> !xssClear(e).equals(employerName))
                            .collect(Collectors.joining(", "));
                    try {
                        salary = xssClear(element.getElementsByTag("b").first().text());
                    } catch (Exception e) {
                        salary = xssClear(element.getElementsByTag("b").text());
                    }
                    Integer[] salaries = getToSalaries(salary);
                    if (isToValid(freshen, getJoin(title, skills))) {
                        VacancyTo v = new VacancyTo();
                        v.setTitle(getLinkIfEmpty(title));
                        v.setEmployerName(employerName);
                        v.setAddress(getAddress(address));
                        v.setSalaryMin(salaries[0]);
                        v.setSalaryMax(salaries[1]);
                        v.setUrl(xssClear(element.getElementsByTag("a").attr("href")));
                        v.setSkills(getToSkills(skills, freshen));
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

    public static List<VacancyTo> getJobsDou(Elements elements, Freshen freshen) {
        List<VacancyTo> list = new ArrayList();
        for (Element element : elements) {
            String date = xssClear(element.getElementsByClass("date").text());
            LocalDate localDate = isEmpty(date) ? LocalDate.now() : getToLocalDate(date);
            if (localDate.isAfter(reasonDateLoading)) {
                try {
                    String skills, title = getToTitle(xssClear(element.getElementsByTag("a").first().text()));
                    skills = xssClear(element.getElementsByClass("sh-info").text());
                    Integer[] salaries = getToSalaries(xssClear(element.getElementsByClass("salary").text()));
                    if (isToValid(freshen, getJoin(title, skills))) {
                        VacancyTo v = new VacancyTo();
                        v.setTitle(getLinkIfEmpty(title));
                        v.setEmployerName(getToName(xssClear(element.getElementsByTag("a").last().text())));
                        v.setAddress(getLinkIfEmpty(xssClear(element.getElementsByClass("cities").text())));
                        v.setSalaryMin(salaries[0]);
                        v.setSalaryMax(salaries[1]);
                        v.setUrl(xssClear(element.getElementsByTag("a").first().attr("href")));
                        v.setSkills(getToSkills(skills, freshen));
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

    public static List<VacancyTo> getJobsMarket(Elements elements, Freshen freshen) {
        List<VacancyTo> list = new ArrayList<>();
        for (Element element : elements) {
            try {
                LocalDate localDate = getToLocalDate(getDateJobsMarket(xssClear(element.getElementsByTag("time").text())));
                if (localDate.isAfter(reasonDateLoading)) {
                    String title, salary, skills = getToSkills(xssClear(element.getElementsByClass("card-body").text()), freshen);
                    title = getToTitle(xssClear(element.getElementsByClass("link").text()));
                    salary = xssClear(element.getElementsByClass("text-muted").tagName("strong").text());
                    Integer[] salaries = getToSalaries(salary);
                    if (isToValid(freshen, getJoin(title, skills))) {
                        VacancyTo v = new VacancyTo();
                        v.setTitle(getLinkIfEmpty(title));
                        v.setEmployerName(getToName(xssClear(element.getElementsByClass("cursor-pointer").text())));
                        v.setAddress(getLinkIfEmpty(xssClear(element.getElementsByClass("fa-map-marker-alt").next().text())));
                        v.setSalaryMin(salaries[0]);
                        v.setSalaryMax(salaries[1]);
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

    public static List<VacancyTo> getJobsMarketUA(Elements elements, Freshen freshen) {
        List<VacancyTo> list = new ArrayList<>();
        for (Element element : elements) {
            try {
                LocalDate localDate = getToLocalDate(xssClear(element.getElementsByAttributeValueStarting("class", "badge").text().split(" ")[0]));
                if (localDate.isAfter(reasonDateLoading)) {
                    String title, skills = xssClear(element.getElementsByClass("mt-1").text().trim());
                    title = element.select("a,strong").get(0).ownText();
                    Integer[] salaries = getToSalaries(xssClear(element.getElementsByClass("text-success").tagName("strong").text()));
                    if (isToValid(freshen, getJoin(title, skills))) {
                        VacancyTo v = new VacancyTo();
                        v.setTitle(getLinkIfEmpty(title));
                        v.setEmployerName(element.select("a,strong").get(1).ownText());
                        v.setAddress(element.select("a,strong").get(2).ownText());
                        v.setSalaryMin(salaries[0]);
                        v.setSalaryMax(salaries[1]);
                        v.setUrl(xssClear(element.getElementsByTag("a").attr("href")));
                        v.setSkills(getToSkills(skills, freshen));
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

    public static List<VacancyTo> getVacanciesLinkedin(Elements elements, Freshen freshen) {
        List<VacancyTo> list = new ArrayList();
        for (Element element : elements) {
            try {
                LocalDate localDate = getToLocalDate(xssClear(element.getElementsByTag("time").tagName("time").attr("datetime")));
                if (localDate.isAfter(reasonDateLoading)) {
                    String title = xssClear(element.getElementsByClass("base-search-card__title").text());
                    Integer[] salaries = pattern_is_money_value.matcher(title).find() ? getToSalaries(title) : new Integer[]{1, 1};
                    if (true) {
                        VacancyTo v = new VacancyTo();
                        v.setTitle(title);
                        v.setEmployerName(getToName(xssClear(element.getElementsByClass("base-search-card__subtitle").tagName("a").text())));
                        v.setAddress(xssClear(element.getElementsByClass("job-search-card__location").text()));
                        v.setSalaryMin(salaries[0]);
                        v.setSalaryMax(salaries[1]);
                        v.setUrl(xssClear(element.getElementsByTag("a").attr("href")));
                        v.setSkills(getToSkills("", freshen));
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
                    String skills, title = getToTitle(xssClear(element.getElementsByClass("posting-title__position").text()));
                    skills = xssClear(element.getElementsByTag("common-posting-item-tag").text());
                    Integer[] salaries = getToSalaries(xssClear(element.getElementsByAttributeValueContaining("class", "salary").text()));
                    if (isToValid(freshen, getJoin(title, skills))) {
                        VacancyTo v = new VacancyTo();
                        v.setTitle(getLinkIfEmpty(title));
                        v.setEmployerName(xssClear(element.getElementsByClass("posting-title__company").text()).substring(2).trim());
                        v.setAddress(getToNofluffAddress(xssClear(element.getElementsByTag("nfj-posting-item-city").text())));
                        v.setSalaryMin(salaries[0]);
                        v.setSalaryMax(salaries[1]);
                        v.setUrl(getToUrl(nofluff, xssClear(element.getElementsByTag("a").attr("href"))));
                        v.setSkills(getToSkills(skills, freshen));
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
                    skills = xssClear(element.getElementsByClass("card-description").text());
                    salaries = xssClear(element.getElementsByClass("salary").text());
                    salaries = isEmpty(salaries) ? title : skills;
                    if (isToValid(freshen, getJoin(title, skills))) {
                        VacancyTo v = new VacancyTo();
                        v.setTitle(getLinkIfEmpty(title));
                        v.setEmployerName(getToName(xssClear(element.getElementsByClass("company-name").text())));
                        v.setAddress(getLinkIfEmpty(xssClear(element.getElementsByClass("location").text())));
                        v.setSalaryMin(getToSalaries(salaries)[0]);
                        v.setSalaryMax(getToSalaries(salaries)[1]);
                        v.setUrl(getToUrl(rabota, xssClear(element.getElementsByTag("a").attr("href"))));
                        v.setSkills(getToSkills(skills, freshen));
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
                        v.setSkills(getToSkills("", freshen));
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
                    skills = xssClear(element.getElementsByClass("job-snippet").text());
                    if (isToValid(freshen, getJoin(title, skills))) {
                        VacancyTo v = new VacancyTo();
                        v.setTitle(getLinkIfEmpty(title));
                        v.setEmployerName(getToName(xssClear(xssClear(element.getElementsByClass("companyName").text()))));
                        v.setAddress(getLinkIfEmpty(xssClear(element.getElementsByClass("companyLocation").text())));
                        v.setSalaryMin(1);
                        v.setSalaryMax(1);
                        v.setUrl(getToUrl(indeed, xssClear(element.getElementsByTag("a").tagName("a").attr("data-jk"))));
                        v.setSkills(getToSkills(skills, freshen));
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

    public static List<VacancyTo> getVacanciesJooble(Elements elements, Freshen freshen, String codeISO) {
        List<VacancyTo> list = new ArrayList();
        for (Element element : elements) {
            try {
                LocalDate localDate;
                try {
                    String dateString = xssClear(element.getElementsByClass("caption e0VAhp"/*"caption _04443"*/).text());
                    localDate = getToLocalDate(getJoobleDate(codeISO, dateString));
                } catch (Exception e) {
                    log.error(error, "LocalDate", e.getMessage());
                    localDate = defaultDate;
                }
                if (localDate.isAfter(reasonDateLoading)) {
                    String skills, salary, title = getToTitle(xssClear(element.getElementsByTag("header").tagName("span").text()));
                    skills = xssClear(element.getElementsByClass("_9jGwm1"/*"_10840"*/).text());
                    salary = xssClear(element.getElementsByClass("jNebTl"/*"a7943"*/).text());
                    Integer[] salaries = getToSalaries(!isEmpty(salary) ? getJoin(skills, "month") : salary);
                    if (isToValid(freshen, getJoin(title, skills))) {
                        VacancyTo v = new VacancyTo();
                        v.setTitle(getLinkIfEmpty(title));
                        v.setEmployerName(getToName(xssClear(element.getElementsByClass("Ya0gV9"/*"efaa8"*/).text())));
                        v.setAddress(getLinkIfEmpty(xssClear(Optional.of(element.getElementsByClass("caption _2_Ab4T"/*"caption d7cb2"*/).text()).orElse(""))));
                        v.setSalaryMin(salaries[0]);
                        v.setSalaryMax(salaries[1]);
                        v.setUrl(getJoin("https://", codeISO.equals("all") ? "" : getJoin(codeISO, "."), getToUrl(jooble, xssClear(element.getElementsByTag("article").attr("id")))));
                        v.setSkills(getToSkills(skills, freshen));
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

    public static List<VacancyTo> getWork(Elements elements, Freshen freshen) {
        List<VacancyTo> list = new ArrayList<>();
        for (Element element : elements) {
            try {
                Elements dataName = element.select("b"), dataAddress = element.getElementsByClass("middot").next();
                LocalDate localDate = getToLocalDate(xssClear(element.getElementsByTag("a").first().attr("title")));
                if (localDate.isAfter(reasonDateLoading)) {
                    String address, skills, title = getToTitle(xssClear(element.getElementsByTag("a").first().text()));
                    skills = xssClear(element.getElementsByClass("overflow").text());
                    Integer[] salaries = getToSalaries(xssClear(element.getElementsByTag("b").tagName("b").first().text()));
                    address = xssClear(dataAddress.get(dataAddress.size() - 1).ownText());
                    if (isToValid(freshen, getJoin(title, skills))) {
                        VacancyTo v = new VacancyTo();
                        v.setTitle(getLinkIfEmpty(title));
                        v.setEmployerName(xssClear(dataName.get(dataName.size() - 1).ownText()));
                        v.setAddress(isEmpty(address) ? xssClear(dataAddress.get(dataAddress.size() - 2).ownText()) : address);
                        v.setSalaryMin(salaries[0]);
                        v.setSalaryMax(salaries[1]);
                        v.setUrl(getToUrl(work, xssClear(element.getElementsByTag("a").attr("href"))));
                        v.setSkills(getToSkills(skills, freshen));
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
        int i = 1;
        for (Element element : elements) {
            try {
                Elements listDateAdr = element.select("span");
                String[] dateAddress = xssClear(listDateAdr.get(listDateAdr.size() - 1).ownText()).split(", ");
                LocalDate localDate = getToLocalDate(dateAddress[0]);
                if (localDate.isAfter(reasonDateLoading)) {
                    String employerName, salary, title = getToTitle(xssClear(element.getElementsByTag("a").first().text()));
                    employerName = xssClear(element.getElementsByClass("c4").text());
                    Elements listSalary = element.select("strong");
                    Integer[] salaries = getToSalaries(listSalary.size() <= 1 ? "" : getJoin(listSalary.get(0).ownText(), "-", listSalary.get(1).ownText()));
                    if (true) {
                        VacancyTo v = new VacancyTo();
                        v.setTitle(getLinkIfEmpty(title));
                        v.setEmployerName(getToName(employerName));
                        v.setAddress(dateAddress[1]);
                        v.setSalaryMin(salaries[0]);
                        v.setSalaryMax(salaries[1]);
                        v.setUrl(xssClear(element.getElementsByTag("a").attr("href")));
                        v.setSkills(getToSkills("", freshen));
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

        System.out.println(i++ + "-".repeat(150));
        System.out.println("dateString     =" + xssClear(element.getElementsByClass("date").text()));
        System.out.println("date           =" + v.getReleaseDate());

        Elements elementList = element.getElementsByTag("a");
        System.out.print("elementList: ");
        System.out.print(elementList.size()+" ");
        elementList.forEach(e -> System.out.print(e.ownText() + " ::: "));
        System.out.println();

        System.out.println("titleString2   =" + getLinkIfEmpty(elementsByTag.get(1).ownText()));
        System.out.println("title          =" + v.getTitle());

        System.out.println("emploNameString=" + elementsByTag.get(2).ownText());
        System.out.println("employerName   =" + v.getEmployerName());

        System.out.println("addressString  =" + elementsByTag.get(3).ownText());
        System.out.println("address        =" + v.getAddress());

        System.out.println("salaryString1  =" + Arrays.toString(salaries));
        System.out.println("salaryMin      =" + v.getSalaryMin());
        System.out.println("salaryMan      =" + v.getSalaryMax());

        System.out.println("urlString      =" + xssClear(element.getElementsByTag("a").attr("href")));
        System.out.println("url            =" + v.getUrl());

        System.out.println("skillsString   =" + xssClear(element.getElementsByClass("offer-description").text()));
        System.out.println("skills         =" + v.getSkills());


*/
// itJobsWatch
/*    itJobsWatch
                        Elements salaryNameList = element.getElementsByTag("span");
                        Elements addressSkills = element.select("div");
                        skills = xssClear(element.getElementsByClass("jobDescription").text());
                        Integer[] salaries = getToSalaries(getJoin(xssClear(salaryNameList.get(1).ownText()), " year"));

                        System.out.println(i++ + "-".repeat(150));
                        System.out.print("salaryNameList: " + salaryNameList.size()+" \n");
                        salaryNameList.forEach(e -> System.out.println(" ::: "+ e.ownText() + " ::: "));
                        System.out.print("addressSkills: " + addressSkills.size()+" \n");
                        addressSkills.forEach(e -> System.out.println(" ::: "+ e.ownText() + " ::: "));
                        System.out.print("list3: " + listData.size()+" \n");
                        listData.forEach(e -> System.out.println(" ::: "+ e.ownText() + " ::: "));
                        System.out.println(".  ".repeat(50));

                        System.out.println("dateString    =" + xssClear(element.getElementsByTag("time").text()));
                        System.out.println("date           =" + v.getReleaseDate());
                        System.out.println("titleString    =" + xssClear(element.getElementsByClass("jobTitle").first().text()));
                        System.out.println("title          =" + v.getTitle());

                        System.out.println("emploNameString=" + xssClear(xssClear(salaryNameList.get(0).ownText())));
                        System.out.println("employerName   =" + v.getEmployerName());

                        System.out.println("addressString  =" + xssClear(element.getElementsByClass("jobDetails jobDetailsTop").text().split("-")[0]));
                        System.out.println("address        =" + v.getAddress());


                        System.out.println("salaryString   =" + xssClear(salaryNameList.get(1).ownText()));
                        System.out.println("salaryMin      =" + v.getSalaryMin());
                        System.out.println("salaryMan      =" + v.getSalaryMax());

                        System.out.println("urlString      =" + xssClear(element.getElementsByTag("a").attr("href")));
                        System.out.println("url            =" + v.getUrl());

                        System.out.println("skillsString  =" + xssClear(element.getElementsByClass("jobDescription").toString()));
                        System.out.println("skills         =" + v.getSkills());
*/
// jobBank
/* jobBank
                        System.out.println(i++ + "-".repeat(150));
                        Elements elementList = element.getElementsByTag("span");
                        System.out.print("elementList: ");
                        System.out.print(elementList.size()+" \n");
                        elementList.forEach(e -> System.out.println(" ::: "+ e.ownText() + " ::: "));
                        System.out.println();
                        System.out.print("elementsByTag: ");
                        System.out.println(element.getElementsByTag("span").text());

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
                        System.out.println(i++ + "-".repeat(150));
                        System.out.println("dateString     =" + xssClear(element.getElementsByClass("card-date").text()));
                        System.out.println("date           =" + v.getReleaseDate());
                        System.out.println("titleString    =" + xssClear(element.getElementsByTag("a").attr("title")));
                        System.out.println("title          =" + v.getTitle());

                        System.out.println("emploNameString=" + xssClear(element.getElementsByClass("secondary-text").text()));
                        System.out.println("employerName   =" + v.getEmployerName());

                        System.out.println("addressString  =" + xssClear(element.getElementsByClass("card-info card__subtitle").text()));
                        System.out.println("address        =" + v.getAddress());

                        System.out.println("salaryString   =" + salary);
                        System.out.println("salaryMin      =" + v.getSalaryMin());
                        System.out.println("salaryMan      =" + v.getSalaryMax());

                        System.out.println("urlString      =" + xssClear(element.getElementsByTag("a").attr("href")));
                        System.out.println("url            =" + v.getUrl());

                        System.out.println("skillsString  =" + xssClear(element.getElementsByAttribute("alt").eachAttr("alt").toString()));
                        System.out.println("skills         =" + v.getSkills());
*/
// jobsMarket
/*
                        System.out.println(i++ + "-".repeat(150));
                        Elements elementList = element.select("span");
                        System.out.print("elementList: "+ elementList.size()+" \n");
                        elementList.forEach(e -> System.out.println(" ::: "+ e.ownText() + " ::: "));
                        System.out.println();

                        System.out.println(". ".repeat(75));
//                        System.out.println("element=\n" + element);
                        System.out.println("dateString     =" + xssClear(xssClear(element.getElementsByTag("time").text())));
                        System.out.println("date           =" + v.getReleaseDate());
                        System.out.println("titleString    =" + xssClear(element.getElementsByClass("link").text()));
                        System.out.println("title          =" + v.getTitle());

                        System.out.println("emploNameString=" + xssClear(element.getElementsByClass("cursor-pointer").text()));
                        System.out.println("employerName   =" + v.getEmployerName());

                        System.out.println("addressString  =" + xssClear(element.getElementsByClass("fa-map-marker-alt").next().text()));
                        System.out.println("address        =" + v.getAddress());

                        System.out.println("salaryString   =" + xssClear(element.getElementsByClass("text-muted").tagName("strong").text()));
                        System.out.println("salaryMin      =" + v.getSalaryMin());
                        System.out.println("salaryMan      =" + v.getSalaryMax());

                        System.out.println("urlString      =" + xssClear(element.getElementsByTag("a").first().attr("href")));
                        System.out.println("url            =" + v.getUrl());
                        System.out.println("skillsString   =" + xssClear(element.getElementsByClass("card-body").text().trim()));
                        System.out.println("skills         =" + v.getSkills());
*/
// jobsMarketUA
/*
                    System.out.println(i++ + "-".repeat(150));
                    Elements elementList = element.select("a,strong");
                    System.out.print("elementList: "+ elementList.size()+" \n");
                    elementList.forEach(e -> System.out.println(" ::: "+ e.ownText() + " ::: "));
                    System.out.println();

                        System.out.println(". ".repeat(75));
//                        System.out.println("element=\n" + element);
                        System.out.println("dateString     =" + xssClear(xssClear(element.getElementsByAttributeValueStarting("class", "badge").text())));
                        System.out.println("date           =" + v.getReleaseDate());
                        System.out.println("titleString    =" + xssClear(element.getElementsByClass("a").text()));
                        System.out.println("title          =" + v.getTitle());

                        System.out.println("emploNameString=" + xssClear(element.getElementsByTag("strong").first().text()));
                        System.out.println("employerName   =" + v.getEmployerName());

                        System.out.println("addressString  =" + xssClear(element.getElementsByClass("text-secondary").tagName("a").text()));
                        System.out.println("address        =" + v.getAddress());

                        System.out.println("salaryString   =" + xssClear(element.getElementsByClass("text-success").tagName("strong").text().replaceAll(",", "")));
                        System.out.println("salaryMin      =" + v.getSalaryMin());
                        System.out.println("salaryMan      =" + v.getSalaryMax());

                        System.out.println("urlString      =" + xssClear(element.getElementsByTag("a").first().attr("href")));
                        System.out.println("url            =" + v.getUrl());
                        System.out.println("skillsString   =" + xssClear(element.getElementsByClass("mt-1").text().trim()));
                        System.out.println("skills         =" + v.getSkills());





* */
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

                    Elements elementList = element.getElementsByAttributeStarting("h");

                        System.out.println(i++ + "-".repeat(150));
                        System.out.print("elementList: "+elementList.size()+" \n");
                        elementList.forEach(e -> System.out.println(" ::: "+ e.ownText() + " ::: "));
                        System.out.println();

                        System.out.println("dateString     =" + xssClear(element.getElementsByTag("time").tagName("time").attr("datetime")));
                        System.out.println("date           ="+ v.getReleaseDate());
                        System.out.println("titleString    ="+ xssClear(element.getElementsByClass("base-search-card__title").text()));
                        System.out.println("title          ="+v.getTitle());

                        System.out.println("emploNameString="+ xssClear(element.getElementsByClass("base-search-card__subtitle").tagName("a").text()));
                        System.out.println("employerName   ="+v.getEmployerName());

                        System.out.println("addressString  ="+xssClear(element.getElementsByClass("job-search-card__location").text()));
                        System.out.println("address        ="+v.getAddress());

                        System.out.println("salaryString   ="+ title);
                        System.out.println("salaryMin      ="+v.getSalaryMin());
                        System.out.println("salaryMan      ="+v.getSalaryMax());

                        System.out.println("urlString      ="+ xssClear(element.getElementsByTag("a").attr("href")));
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
// work
/*
                        System.out.println(i++ + "-".repeat(150));
                        System.out.print("dataList1: "+dataName.size()+" \n");
                        dataName.forEach(e -> System.out.print(" ::: "+ xssClear(e.ownText()) + " ::: "));
                        System.out.println();

                        System.out.println("dateString     =" + xssClear(element.getElementsByTag("a").attr("title")));
                        System.out.println("date           ="+ v.getReleaseDate());
                        System.out.println("titleString    ="+ xssClear(element.getElementsByTag("a").first().text()));
                        System.out.println("title          ="+v.getTitle());

                        System.out.println("emploNameString="+ xssClear(dataName.get(dataName.size() - 1).ownText()));
                        System.out.println("employerName   ="+v.getEmployerName());

                        System.out.print("dataList="+dataList.size()+" ");
                        dataList.forEach(l -> System.out.print(xssClear(l.ownText()) + " ::: "));
                        System.out.println("\naddressString1 ="+address);
                        System.out.println("addressString2 ="+(isEmpty(address) ? xssClear(dataList.get(dataList.size() - 2).text()) : address));
                        System.out.println("\n\naddress        ="+v.getAddress());

                        System.out.println("salaryString   ="+ xssClear(element.getElementsByTag("b").tagName("b").first().text()));
                        System.out.println("salaryMin      ="+v.getSalaryMin());
                        System.out.println("salaryMan      ="+v.getSalaryMax());

                        System.out.println("urlString       ="+ xssClear(element.getElementsByTag("a").attr("href")));
                        System.out.println("url            ="+v.getUrl());

                        System.out.println("skillsString   ="+xssClear(element.getElementsByClass("overflow").text()));
                        System.out.println("skills         ="+v.getSkills());
*/
// zaplata
/* zaplata
                        System.out.println(i++ + "-".repeat(150));

                        System.out.print("listDateAdr: "+ listDateAdr.size()+" \n");
                        listDateAdr.forEach(e -> System.out.println(" ::: "+ e.ownText() + " ::: "));
                        System.out.println(".  ".repeat(50));
                        System.out.print("listSalary: "+ listSalary.size()+" \n");
                        listSalary.forEach(e -> System.out.println(" ::: "+ e.ownText() + " ::: "));
                        System.out.println();


                        System.out.println("dateString     =" + dateAddress[0]);
                        System.out.println("date           =" + v.getReleaseDate());
                        System.out.println("titleString    =" + xssClear(element.getElementsByTag("a").first().text()));
                        System.out.println("title          =" + v.getTitle());

                        System.out.println("emploNameString=" + xssClear(element.getElementsByClass("c4").text()));
                        System.out.println("employerName   =" + v.getEmployerName());

                        System.out.println("addressString  =" + dateAddress[1]);
                        System.out.println("address        =" + v.getAddress());

                        System.out.println("salaryString   =" + salary);
                        System.out.println("salaryMin      =" + v.getSalaryMin());
                        System.out.println("salaryMan      =" + v.getSalaryMax());

                        System.out.println("urlString      =" + xssClear(element.getElementsByTag("a").attr("href")));
                        System.out.println("url            =" + v.getUrl());

                        System.out.println("skillsString  =" + xssClear(element.getElementsByAttribute("alt").eachAttr("alt").toString()));
                        System.out.println("skills         =" + v.getSkills());

*/


