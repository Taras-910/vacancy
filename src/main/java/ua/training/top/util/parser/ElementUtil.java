package ua.training.top.util.parser;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.training.top.model.Freshen;
import ua.training.top.to.VacancyTo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.springframework.util.StringUtils.hasText;
import static ua.training.top.aggregator.installation.InstallationUtil.reasonDateToLoad;
import static ua.training.top.aggregator.strategy.NofluffjobsStrategy.validAddress;
import static ua.training.top.aggregator.strategy.NofluffjobsStrategy.validDate;
import static ua.training.top.aggregator.strategy.UAIndeedStrategy.getCorrectUrl;
import static ua.training.top.util.VacancyCheckUtil.getMatchesLanguage;
import static ua.training.top.util.parser.data.CorrectAddress.getCorrectAddress;
import static ua.training.top.util.parser.data.CorrectEmployerName.getCorrectEmployerName;
import static ua.training.top.util.parser.data.CorrectSkills.getCorrectSkills;
import static ua.training.top.util.parser.data.CorrectTitle.getCorrectTitle;
import static ua.training.top.util.parser.date.DateUtil.*;
import static ua.training.top.util.parser.date.ToCorrectDate.getCorrectDate;
import static ua.training.top.util.parser.salary.MinMax.salaryMax;
import static ua.training.top.util.parser.salary.MinMax.salaryMin;
import static ua.training.top.util.parser.salary.SalaryUtil.getCorrectSalary;
import static ua.training.top.util.xss.XssUtil.xssClear;

public class ElementUtil {
    public static final Logger log = LoggerFactory.getLogger(ElementUtil.class);

    public static List<VacancyTo> getVacanciesDjinni(Elements elements, Freshen freshen) {
        List<VacancyTo> list = new ArrayList();
        elements.forEach(element -> {
            try {
                LocalDate localDate = parseCustom(supportDate(xssClear(element.getElementsByClass("inbox-date").text().trim())), element);
                if(localDate.isAfter(reasonDateToLoad)) {
                    String title = getCorrectTitle(xssClear(element.getElementsByClass("list-jobs__title").text().trim()));
                    String skills = getCorrectSkills(xssClear(element.getElementsByClass("list-jobs__description").text().trim()));
                    if (getMatchesLanguage(freshen, title, skills) && skills.length() > 2) {
                        VacancyTo v = new VacancyTo();
                        v.setTitle(title);
                        v.setEmployerName(getCorrectEmployerName(xssClear(element.getElementsByClass("list-jobs__details__info").tagName("a").first().child(1).text().trim())));
                        v.setAddress(freshen.getWorkplace());
                        v.setSalaryMax(1);
                        v.setSalaryMin(1);
                        v.setUrl("https://djinni.co".concat(xssClear(element.getElementsByClass("profile").first().attr("href").trim())));
                        v.setSkills(skills);
                        v.setReleaseDate(localDate);
                        list.add(v);
                    }
                }
            } catch (Exception e) {
                log.error("there is error \ne={}\n for getVacanciesDjinni for parse element \n{}", e.getLocalizedMessage(), element);
            }
        });
        return list;
    }

    public static List<VacancyTo> getVacanciesGrc(Elements elements, Freshen freshen) {
        List<VacancyTo> list = new ArrayList<>();
        elements.forEach(element -> {
            try {
                LocalDate localDate = parseCustom(supportDate(prepare(xssClear(element.getElementsByClass("vacancy-serp-item__publication-date").text().trim()))), element);
                if(localDate.isAfter(reasonDateToLoad)) {
                    String title = getCorrectTitle(xssClear(element.getElementsByTag("a").first().text().trim().toLowerCase()));
                    String skills = getCorrectSkills(xssClear(element.getElementsByAttributeValue("data-qa", "vacancy-serp__vacancy_snippet_requirement").text().trim().toLowerCase()));
                    if (getMatchesLanguage(freshen, title, skills) && skills.length() > 2) {
                        VacancyTo v = new VacancyTo();
                        v.setTitle(title);
                        v.setEmployerName(getCorrectEmployerName(xssClear(element.getElementsByAttributeValue("data-qa", "vacancy-serp__vacancy-employer").text().trim())));
                        v.setAddress(getCorrectAddress(xssClear(element.getElementsByAttributeValue("data-qa", "vacancy-serp__vacancy-address").text().trim())));
                        v.setSalaryMax(salaryMax(getCorrectSalary(xssClear(element.getElementsByAttributeValue("data-qa", "vacancy-serp__vacancy-compensation").text().trim())), element));
                        v.setSalaryMin(salaryMin(getCorrectSalary(xssClear(element.getElementsByAttributeValue("data-qa", "vacancy-serp__vacancy-compensation").text().trim())), element));
                        v.setUrl(xssClear(element.getElementsByTag("a").first().attr("href").trim()));
                        v.setSkills(skills);
                        v.setReleaseDate(localDate);
                        list.add(v);
                    }
                }
            } catch (Exception e) {
                log.error("there is error \ne={}\n for getVacanciesGrc for parse element \n{}", e.getLocalizedMessage(), element);
            }
        });
        return list;
    }

    public static List<VacancyTo> getVacanciesHabr(Elements elements, Freshen freshen) {
        List<VacancyTo> list = new ArrayList<>();
        for (Element element : elements) {
            try {
                LocalDate localDate = parseCustom(xssClear(element.getElementsByAttribute("datetime").attr("datetime").substring(0, 10)), element);
                if(localDate.isAfter(reasonDateToLoad)) {
                    String title = getCorrectTitle(xssClear(element.getElementsByClass("vacancy-card__title").tagName("a").text().trim()));
                    String skills = getCorrectSkills(xssClear(element.getElementsByClass("vacancy-card__skills").text().trim()));
                    if (getMatchesLanguage(freshen, title, skills) && skills.length() > 2) {
                        VacancyTo v = new VacancyTo();
                        v.setTitle(title);
                        v.setEmployerName(getCorrectEmployerName(xssClear(element.getElementsByClass("vacancy-card__company").first().child(0).text())));
                        v.setAddress(getCorrectAddress(xssClear(element.getElementsByClass("vacancy-card__meta").tagName("a").first().text())));
                        v.setSalaryMax(salaryMax(getCorrectSalary(xssClear(element.getElementsByClass("basic-salary").text().trim())), element));
                        v.setSalaryMin(salaryMin(getCorrectSalary(xssClear(element.getElementsByClass("basic-salary").text().trim())), element));
                        v.setUrl("https://career.habr.com".concat(xssClear(element.getElementsByClass("vacancy-card__icon-link").attr("href").trim())));
                        v.setSkills(skills);
                        v.setReleaseDate(localDate);
                        list.add(v);
                    }
                }
            } catch (Exception e) {
                log.error("there is error \ne={}\n for getVacanciesHabr for parse element \n{}", e.getLocalizedMessage(), element);
            }
        }
        return list;
    }

    public static List<VacancyTo> getVacanciesJobs(Elements elements, Freshen freshen) {
        List<VacancyTo> list = new ArrayList();
        elements.forEach(element -> {
            try {
                String title = getCorrectTitle(xssClear(element.getElementsByTag("a").first().text().trim()));
                String skills = getCorrectSkills(xssClear(element.getElementsByClass("sh-info").text().trim()));
                if (getMatchesLanguage(freshen, title, skills) && skills.length() > 2) {
                    VacancyTo v = new VacancyTo();
                    v.setTitle(title);
                    v.setEmployerName(getCorrectEmployerName(xssClear(element.getElementsByTag("a").last().text().trim())));
                    v.setAddress(getCorrectAddress(xssClear(element.getElementsByClass("cities").text().trim())));
                    v.setSalaryMax(salaryMax(getCorrectSalary(xssClear(element.getElementsByClass("salary").text().trim())), element));
                    v.setSalaryMin(salaryMin(getCorrectSalary(xssClear(element.getElementsByClass("salary").text().trim())), element));
                    v.setUrl(xssClear(element.getElementsByTag("a").first().attr("href").trim()));
                    v.setSkills(skills);
                    v.setReleaseDate(LocalDate.now().minusDays(7));
                    list.add(v);
                }
            } catch (Exception e) {
                log.error("there is error \ne={}\n for getVacanciesJobs for parse element \n{}", e.getLocalizedMessage(), element);
            }
        });
        return list;
    }

    public static List<VacancyTo> getVacanciesLinkedin(Elements elements, Freshen freshen) {
        List<VacancyTo> list = new ArrayList();
        elements.forEach(element -> {
            try {
                LocalDate localDate = parseCustom(xssClear(element.getElementsByTag("time").tagName("time").attr("datetime")), element);
                if (localDate.isAfter(reasonDateToLoad)) {
                    String employerName, title = getCorrectTitle(xssClear(element.getElementsByClass("result-card__title job-result-card__title").text().trim()));
                    if (title.toLowerCase().matches(".*\\b" + freshen.getLanguage() + "\\b.*")) {
                        VacancyTo v = new VacancyTo();
                        v.setTitle(title);
                        employerName = element.getElementsByClass("result-card__subtitle-link job-result-card__subtitle-link").text().trim();
                        if(employerName == null || employerName.equals("")) {
                            employerName = element.getElementsByClass("job-result-card__subtitle").text().trim();
                            log.info("other tag employerName={}", employerName);
                        }
                        v.setEmployerName(getCorrectEmployerName(xssClear(employerName)));
//                        v.setEmployerName(getCorrectEmployerName(xssClear(element.getElementsByClass("job-result-card__subtitle").text().trim())));
                        v.setAddress(getCorrectAddress(xssClear(element.getElementsByClass("job-result-card__location").text().trim())));
                        v.setSalaryMax(1);
                        v.setSalaryMin(1);
                        v.setUrl(xssClear(element.getElementsByClass("result-card__full-card-link").first().attr("href").split("&")[0].trim()));
                        v.setSkills("see the card on the link");
                        v.setReleaseDate(localDate);
                        list.add(v);
                    }
                }
            } catch (Exception e) {
                log.error("there is error \ne={}\n for getVacanciesLinkedin for parse element \n{}", e.getLocalizedMessage(), element);
            }
        });
        return list;
    }

    public static Collection<? extends VacancyTo> getNofluffjobsVacancies(Elements elements, Freshen freshen) {
        List<VacancyTo> list = new ArrayList<>();
        log.info("elements {}", elements.size());
        for (Element element : elements) {
            try {
                LocalDate localDate = validDate(element.getElementsByClass("new-label").text());
                if(localDate.isAfter(reasonDateToLoad)) {
                    String title = getCorrectTitle(xssClear(element.getElementsByClass("posting-title__position").text().trim()));
                    if (title.toLowerCase().matches(".*\\b" + freshen.getLanguage() + "\\b.*")) {
                        VacancyTo v = new VacancyTo();
                        v.setTitle(title);
                        v.setEmployerName(xssClear(element.getElementsByClass("posting-title__company").text()).substring(2).trim());
                        v.setAddress(getCorrectAddress(validAddress(xssClear(element.getElementsByTag("nfj-posting-item-city").text()))));
                        v.setSalaryMax(salaryMax(getCorrectSalary(xssClear(element.getElementsByTag("nfj-posting-item-tags").text().trim())), element));
                        v.setSalaryMin(salaryMin(getCorrectSalary(xssClear(element.getElementsByTag("nfj-posting-item-tags").text().trim())), element));
                        v.setUrl("https://nofluffjobs.com".concat(xssClear(element.getElementsByTag("a").attr("href").trim())));
                        v.setSkills("see the card on the link");
                        v.setReleaseDate(localDate);
                        list.add(v);
                    }
                }
            } catch (Exception e) {
                log.error("there is error \ne={}\n for getNofluffjobsVacancies for parse element \n{}", e.getLocalizedMessage(), element);
            }
        }
        return list;
    }

    public static List<VacancyTo> getVacanciesRabota(Elements elements, Freshen freshen) {
        List<VacancyTo> list = new ArrayList<>();
        for (Element element : elements) {
            try {
                LocalDate localDate = getCorrectDate(xssClear(element.getElementsByClass("publication-time").text().trim()));
                if(localDate.isAfter(reasonDateToLoad)) {
                    String title = getCorrectTitle(xssClear(element.getElementsByClass("card-title").text().trim()));
                    String skills = getCorrectSkills(xssClear(element.getElementsByClass("card-description").text().trim()));
                    if (getMatchesLanguage(freshen, title, skills) && skills.length() > 2) {
                        VacancyTo v = new VacancyTo();
                        v.setTitle(title);
                        v.setEmployerName(getCorrectEmployerName(xssClear(element.getElementsByClass("company-name").text().trim())));
                        v.setAddress(getCorrectAddress(xssClear(element.getElementsByClass("location").text().trim())));
                        v.setSalaryMax(salaryMax(getCorrectSalary(xssClear(element.getElementsByClass("salary").text().trim())), element));
                        v.setSalaryMin(salaryMin(getCorrectSalary(xssClear(element.getElementsByClass("salary").text().trim())), element));
                        v.setUrl("https://rabota.ua".concat("/company").concat(xssClear(element.getElementsByClass("card").attr("data-company-id").trim()))
                                .concat("/vacancy").concat(element.getElementsByClass("card").attr("data-vacancy-id").trim()));
                        v.setSkills(skills);
                        v.setReleaseDate(localDate);
                        list.add(v);
                    }
                }
            } catch (Exception e) {
                log.error("there is error \ne={}\n for getVacanciesRabota for parse element \n{}", e.getLocalizedMessage(), element);
            }
        }
        return list;
    }

    public static List<VacancyTo> getVacanciesIndeed(Elements elements, Freshen freshen) {
        List<VacancyTo> list = new ArrayList<>();
        for (Element element : elements) {
            try {
                LocalDate localDate = getCorrectDate(xssClear(element.getElementsByClass("date ").text().trim()));
                if(localDate.isAfter(reasonDateToLoad)) {
                    String title = getCorrectTitle(xssClear(element.getElementsByAttributeValue("data-tn-element", "jobTitle").text().trim()));
                    String skills = getCorrectSkills(xssClear(element.getElementsByClass("summary").text().trim()));
                    if (getMatchesLanguage(freshen, title, skills) && skills.length() > 2 && skills.length() < 1000) {
                        VacancyTo v = new VacancyTo();
                        v.setTitle(title);
                        v.setEmployerName(getCorrectEmployerName(xssClear(element.getElementsByClass("company").text().trim())));
                        v.setAddress(getCorrectAddress(xssClear(element.getElementsByClass("location accessible-contrast-color-location").text().trim())));
                        v.setSalaryMax(1);
                        v.setSalaryMin(1);
                        v.setUrl(getCorrectUrl(xssClear(element.getElementsByClass("jobsearch-SerpJobCard").attr("data-jk").trim())));
                        v.setSkills(skills);
                        v.setReleaseDate(localDate);
                        list.add(v);
                    }
                }
            } catch (Exception e) {
                log.error("there is error \ne={}\n for getVacanciesIndeed for parse element \n{}", e.getLocalizedMessage(), element);
            }
        }
        return list;
    }

    public static List<VacancyTo> getVacanciesJooble(Elements elements, Freshen freshen) {
        List<VacancyTo> list = new ArrayList();
        int i = 0;
        for (Element element : elements) {
//            System.out.println("----------------------------------------------------------------------------------------");
            String salary = "1", date, skills, title, url = xssClear(element.getElementsByClass("_31572 _07ebc").attr("id"));
            if(url != ""){
                date = Optional.of(xssClear(element.getElementsByClass("_8d375").last().text().trim())).orElse("");
            }
            else {
                date = Optional.of(xssClear(element.getElementsByClass("_77a3a d3fee _356ae _108aa ab7d6").text().trim())).orElse("");
            }
            String address = getCorrectAddress(xssClear(Optional.of(element.getElementsByClass("_36dc5 d6b7e _4f6da a4850 _2128e _8e9e1").next().first().text()).orElse("").trim()));
            LocalDate localDate = null;
            try {
                localDate = getCorrectDate(date.equalsIgnoreCase(address) ? null : date);
                if (localDate.isAfter(reasonDateToLoad)) {
                    //check salary
                    /*try {
                        String salary1 = getCorrectSalary(Optional.of(xssClear(element.getElementsByClass("_0b1c1").tagName("span").first().text())).orElse("1"));
                        System.out.println("salary1:" + salary1);
                    } catch (Exception e) {
                        System.out.println("salary1: error");
                    }
                    String salary2 = !salary.equals("1") && hasText(salary) ? salary : getCorrectSalary(xssClear(Optional.of(element.getElementsByClass("_0010f").text()).orElse("1")));
                    System.out.println("salary2:" + salary2);
                    try {
                        String salary3 = getCorrectSalary(xssClear(Optional.of(element.getElementsByClass("_44ec3").text()).orElse("1")));
                        System.out.println("salary3:" + salary3);
                    } catch (Exception e) {
                        System.out.println("salary3:error");
                    }
                    String salary4 = getCorrectSalary(xssClear(Optional.of(element.getElementsByClass("b2a33").text()).orElse("1")));
                    System.out.println("salary4:" + salary4);*/
                    if (url != "") {
                        title = getCorrectTitle(xssClear(element.getElementsByClass("_84af9").tagName("span").text().trim()));
                        salary = getCorrectSalary(Optional.of(xssClear(element.getElementsByClass("_0b1c1").tagName("span").first().text())).orElse("1"));
                        salary = !salary.equals("1") && hasText(salary) ? salary : getCorrectSalary(xssClear(Optional.of(element.getElementsByClass("_0010f").text()).orElse("1")));
                    } else {
                        url = element.getElementsByClass("_77074 _8b8c3").attr("id");
                        title = getCorrectTitle(xssClear(element.getElementsByClass("_045f1").tagName("span").text().trim()));
                        salary = getCorrectSalary(xssClear(Optional.of(element.getElementsByClass("_44ec3").text()).orElse("1")));
                    }
                    skills = getCorrectSkills(xssClear(element.getElementsByTag("b").tagName("span").nextAll().text()));
                    skills = skills == null ? "see the card on the link" : skills;
                    if (getMatchesLanguage(freshen, title, skills) && skills.length() > 2) {
                        VacancyTo v = new VacancyTo();
                        v.setTitle(title);
                        v.setEmployerName(getCorrectEmployerName(xssClear(element.getElementsByClass("_786d5").text().trim())));
                        v.setAddress(address);
                        v.setSalaryMax(salaryMax(salary, element));
                        v.setSalaryMin(salaryMin(salary, element));
                        v.setUrl("https://ua.jooble.org/desc/".concat(url));
                        v.setSkills(skills);
                        v.setReleaseDate(localDate);
                        list.add(v);
                    }
                }
            } catch (Exception e) {
                log.error("there is error \ne={}\n for UAJoobleStrategy for parse element \n{}", e.getLocalizedMessage(), element);
            }
        }
        return list;
    }

    public static List<VacancyTo> getVacanciesWork(Elements elements, Freshen freshen) {
        List<VacancyTo> list = new ArrayList<>();
        for (Element element : elements) {
            try {
                LocalDate localDate = parseCustom(supportDate(xssClear(element.getElementsByTag("a").first().attr("title").split("вакансия от ")[1].trim())), element);
                if(localDate.isAfter(reasonDateToLoad)) {
                    String employerName, skills, title = getCorrectTitle(xssClear(element.getElementsByTag("a").first().text()));
                    skills = getCorrectSkills(xssClear(element.getElementsByClass("overflow").text().trim()));
                    if (getMatchesLanguage(freshen, title, skills) && skills.length() > 2) {
                        VacancyTo v = new VacancyTo();
                        v.setTitle(title);
                        employerName = xssClear(element.getElementsByTag("img").attr("alt"));
                        if(employerName == null || employerName.equals("")) {
                            employerName = xssClear(element.getElementsByClass("add-top-xs").tagName("span").tagName("b").eachText().get(0));
                            log.info("other tag employerName={}", employerName);
                        }
                        v.setEmployerName(getCorrectEmployerName(employerName));
                        v.setAddress(getCorrectAddress(xssClear(element.getElementsByClass("add-top-xs").first().children().next().next().text())));
                        v.setSalaryMax(salaryMax(getCorrectSalary(xssClear(element.getElementsByTag("b").tagName("b").first().text().trim())), element));
                        v.setSalaryMin(salaryMin(getCorrectSalary(xssClear(element.getElementsByTag("b").tagName("b").first().text().trim())), element));
                        v.setUrl("https://www.work.ua".concat(xssClear(element.getElementsByTag("a").attr("href"))));
                        v.setSkills(skills);
                        v.setReleaseDate(localDate);
                        list.add(v);
                    }
                }
            } catch (Exception e) {
                log.error("there is error \ne={}\n for getVacanciesWork for parse element \n{}", e.getLocalizedMessage(), element);
            }
        }
        return list;
    }

    public static List<VacancyTo> getVacanciesYandex(Elements elements, Freshen freshen) {
        List<VacancyTo> list = new ArrayList<>();
        for (Element element : elements) {
            try {
                LocalDate localDate = getCorrectDate(xssClear(element.getElementsByClass("serp-vacancy__date").text().trim()));
                if(localDate.isAfter(reasonDateToLoad)) {
                    String title = getCorrectTitle(xssClear(element.getElementsByClass("heading heading_level_3").text().trim()));
                    String skills = getCorrectSkills(xssClear(element.getElementsByClass("serp-vacancy__requirements").text().trim()));
                    if (getMatchesLanguage(freshen, title, skills) && skills.length() > 2) {
                        VacancyTo v = new VacancyTo();
                        v.setTitle(title);

                        System.out.println("\n-----------------------------------------------------------------------------------");
                        String employerName0 = xssClear(element.getElementsByClass("address").text());
                        System.out.println("employerName0="+employerName0);
                        String employerName1 = xssClear(element.getElementsByClass("serp-vacancy__company").text());
                        System.out.println("employerName1="+employerName1);
                        String employerName2 = xssClear(element.getElementsByClass("serp-vacancy__contacts-wrapper").text());
                        System.out.println("employerName2="+employerName2);

                        String salary = xssClear(element.getElementsByClass("serp-vacancy__salary").text().trim());
                        System.out.println("salary="+salary);
                        System.out.println("getCorrectSalary="+getCorrectSalary(salary));
                        System.out.println("salaryMax="+salaryMax(getCorrectSalary(salary),element));
                        System.out.println("salaryMin="+salaryMin(getCorrectSalary(salary),element));


                        v.setEmployerName(getCorrectEmployerName(xssClear(element.getElementsByClass("serp-vacancy__company").text())));
                        v.setAddress(getCorrectAddress(xssClear(element.getElementsByClass("serp-vacancy__contacts").text().trim())));
                        v.setSalaryMax(salaryMax(getCorrectSalary(xssClear(element.getElementsByClass("serp-vacancy__salary").text().trim())), element));
                        v.setSalaryMin(salaryMin(getCorrectSalary(xssClear(element.getElementsByClass("serp-vacancy__salary").text().trim())), element));
                        v.setUrl(xssClear(element.getElementsByTag("a").attr("href")));
                        v.setSkills(skills);
                        v.setReleaseDate(localDate);
                        list.add(v);
                    }
                }
            } catch (Exception e) {
                log.error("there is error \ne={}\n for getVacanciesYandex for parse element \n{}", e.getLocalizedMessage(), element);
            }
        }
        return list;
    }

    public static List<VacancyTo> getVacanciesJobsMarket(Elements elements) {
        List<VacancyTo> list = new ArrayList<>();
        for (Element element : elements) {
            try {
                LocalDate localDate = parseCustom(supportDate(xssClear(element.getElementsByTag("time").text().replaceAll("Posted on: ", "").replaceAll(",", "").trim())), element);
                if(localDate.isAfter(reasonDateToLoad)) {
                    String skills = getCorrectSkills(xssClear(element.getElementsByClass("card-body").text().trim()));
                    if (skills.length() > 2) {
                        VacancyTo v = new VacancyTo();
                        String salary = xssClear(element.getElementsByClass("text-muted clearfix d-block").tagName("strong").text().replaceAll(",","").trim());
                        v.setTitle(getCorrectTitle(xssClear(element.getElementsByClass("link").text().trim())));
                        v.setEmployerName(getCorrectEmployerName(xssClear(element.getElementsByClass("cursor-pointer").text())));
                        v.setAddress(getCorrectAddress(xssClear(element.getElementsByClass("fa-map-marker-alt").next().text().trim())));
                        v.setSalaryMax(salaryMax(getCorrectSalary(salary), element));
                        v.setSalaryMin(salaryMin(getCorrectSalary(salary), element));
                        v.setUrl(xssClear(element.getElementsByTag("a").attr("href")));
                        v.setSkills(skills);
                        v.setReleaseDate(localDate);
                        list.add(v);
                    }
                }
            } catch (Exception e) {
                log.error("there is error \ne={}\n for getVacanciesJobsMarket for parse element \n{}", e.getLocalizedMessage(), element);
            }
        }
        return list;
    }

}
/*
                    System.out.println("------------------------------------------------------------------------------------------");
                    String line = element.getElementsByTag("a").last().text().trim();
                    System.out.println("element:\n" + element);
                    System.out.println("getCorrectEmployerName=" + getCorrectEmployerName(xssClear(line)));
*/
