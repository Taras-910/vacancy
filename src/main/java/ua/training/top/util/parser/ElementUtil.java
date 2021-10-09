package ua.training.top.util.parser;

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
import java.util.concurrent.atomic.AtomicInteger;

import static org.springframework.util.StringUtils.hasText;
import static ua.training.top.aggregator.installation.InstallationUtil.reasonDateToLoad;
import static ua.training.top.util.VacancyCheckUtil.getMatchesLanguage;
import static ua.training.top.util.parser.data.CorrectAddress.*;
import static ua.training.top.util.parser.data.CorrectEmployerName.getCorrectEmployerName;
import static ua.training.top.util.parser.data.CorrectSkills.getCorrectSkills;
import static ua.training.top.util.parser.data.CorrectTitle.addLabelToTitle;
import static ua.training.top.util.parser.data.CorrectTitle.getCorrectTitle;
import static ua.training.top.util.parser.data.CorrectUrl.getCorrectUrl;
import static ua.training.top.util.parser.data.CorrectUrl.getCorrectUrlYandex;
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
                LocalDate localDate = parseCustom(supportDate(xssClear(element.getElementsByAttributeValueStarting("class", "text-date").text().trim())), element);
                if(localDate.isAfter(reasonDateToLoad)) {
                    String skills, title = getCorrectTitle(xssClear(element.getElementsByClass("profile").tagName("a").text().trim()));
                    skills = getCorrectSkills(xssClear(element.getElementsByClass("list-jobs__description").text().trim()));
                    if (/*getMatchesLanguage(freshen, title, skills) && skills.length() > 2*/ true) {
                        VacancyTo v = new VacancyTo();
                        v.setTitle(addLabelToTitle(title, freshen));
                        v.setEmployerName(getCorrectEmployerName(xssClear(element.getElementsByClass("list-jobs__details__info").tagName("a").first().child(1).text().trim())));
                        v.setAddress(getCorrectAddress(xssClear(element.getElementsByAttributeValueStarting("class","location-text").text())));
                        v.setSalaryMax(salaryMax(getCorrectSalary(xssClear(element.getElementsByClass("public-salary-item").text().trim())), element));
                        v.setSalaryMin(salaryMin(getCorrectSalary(xssClear(element.getElementsByClass("public-salary-item").text().trim())), element));
                        v.setUrl(getCorrectUrl("djinni", xssClear(element.getElementsByClass("profile").first().attr("href").trim())));
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
                LocalDate localDate = parseCustom(supportDate(prepare(xssClear(element.getElementsByAttributeValueStarting("class","vacancy-serp-item__publication-date").text().trim()))), element);
                if(localDate.isAfter(reasonDateToLoad)) {
                    String skills, title = getCorrectTitle(xssClear(element.getElementsByClass("resume-search-item__name").text().trim().toLowerCase()));
                    skills = getCorrectSkills(xssClear(element.getElementsByClass("g-user-content").text().trim().toLowerCase()));
                    if (/*getMatchesLanguage(freshen, title, skills) &&*/ skills.length() > 2) {
                        VacancyTo v = new VacancyTo();
                        v.setTitle(addLabelToTitle(title, freshen));
                        v.setEmployerName(getCorrectEmployerName(xssClear(element.getElementsByAttributeValue("data-qa","vacancy-serp__vacancy-employer").text().trim())));
                        v.setAddress(getCorrectAddress(xssClear(element.getElementsByAttributeValue("data-qa", "vacancy-serp__vacancy-address").text().trim())));
                        v.setSalaryMax(salaryMax(getCorrectSalary(xssClear(element.getElementsByClass("vacancy-serp-item__sidebar").text().trim())), element));
                        v.setSalaryMin(salaryMin(getCorrectSalary(xssClear(element.getElementsByClass("vacancy-serp-item__sidebar").text().trim())), element));
                        v.setUrl(xssClear(element.getElementsByClass("bloko-link").attr("href").trim()));
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
                    String skills, title = getCorrectTitle(xssClear(element.getElementsByClass("vacancy-card__title").tagName("a").text().trim()));
                    skills = getCorrectSkills(xssClear(element.getElementsByClass("vacancy-card__skills").text().trim()));
                    if (getMatchesLanguage(freshen, title, skills) && skills.length() > 2) {
                        VacancyTo v = new VacancyTo();
                        v.setTitle(addLabelToTitle(title, freshen));
                        v.setEmployerName(getCorrectEmployerName(xssClear(element.getElementsByClass("vacancy-card__company").first().child(0).text())));
                        v.setAddress(getCorrectAddress(xssClear(element.getElementsByClass("vacancy-card__meta").tagName("a").first().text())));
                        v.setSalaryMax(salaryMax(getCorrectSalary(xssClear(element.getElementsByClass("basic-salary").text().trim())), element));
                        v.setSalaryMin(salaryMin(getCorrectSalary(xssClear(element.getElementsByClass("basic-salary").text().trim())), element));
                        v.setUrl(getCorrectUrl("habr", xssClear(element.getElementsByClass("vacancy-card__icon-link").attr("href").trim())));
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

    public static List<VacancyTo> getVacanciesJobsMarket(Elements elements, Freshen freshen) {
        List<VacancyTo> list = new ArrayList<>();
        for (Element element : elements) {
            try {
                LocalDate localDate = parseCustom(supportDate(xssClear(element.getElementsByTag("time").text().replaceAll("Posted on: ", "").replaceAll(",", "").trim())), element);
                if(localDate.isAfter(reasonDateToLoad)) {
                    String title, salary, skills = getCorrectSkills(xssClear(element.getElementsByClass("card-body").text().trim()));
                    title = getCorrectTitle(xssClear(element.getElementsByClass("link").text().trim()));
                    if (getMatchesLanguage(freshen, title, skills) && skills.length() > 2) {
                        VacancyTo v = new VacancyTo();
                        salary = xssClear(element.getElementsByClass("text-muted clearfix d-block").tagName("strong").text().replaceAll(",","").trim());
                        v.setTitle(title);
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

    public static List<VacancyTo> getVacanciesJobs(Elements elements, Freshen freshen) {
        List<VacancyTo> list = new ArrayList();
        AtomicInteger i = new AtomicInteger();
        elements.forEach(element -> {
            try {
                String skills, title = getCorrectTitle(xssClear(element.getElementsByTag("a").first().text().trim()));
                skills = getCorrectSkills(xssClear(element.getElementsByClass("sh-info").text().trim()));
                if (getMatchesLanguage(freshen, title, skills) && skills.length() > 2) {
                    VacancyTo v = new VacancyTo();
                    v.setTitle(addLabelToTitle(title, freshen));
                    v.setEmployerName(getCorrectEmployerName(xssClear(element.getElementsByTag("a").last().text().trim())));
                    v.setAddress(getCorrectAddress(xssClear(element.getElementsByClass("cities").text().trim())));
                    v.setSalaryMax(salaryMax(getCorrectSalary(xssClear(element.getElementsByClass("salary").text().trim())), element));
                    v.setSalaryMin(salaryMin(getCorrectSalary(xssClear(element.getElementsByClass("salary").text().trim())), element));
                    v.setUrl(xssClear(element.getElementsByTag("a").first().attr("href").trim()));
                    v.setSkills(skills);
                    v.setReleaseDate(LocalDate.now().minusDays(10));
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
                    String title = getCorrectTitle(xssClear(element.getElementsByClass("base-search-card__title").text().trim()));
                    if (/*title.toLowerCase().matches(".*\\b" + freshen.getLanguage() + "\\b.*")*/ true) {
                        VacancyTo v = new VacancyTo();
                        v.setTitle(addLabelToTitle(title, freshen));
                        v.setEmployerName(getCorrectEmployerName(xssClear(element.getElementsByClass("base-search-card__subtitle").tagName("a").text().trim())));
                        v.setAddress(getCorrectAddress(getToLinkedin(xssClear(element.getElementsByClass("job-search-card__location").text().trim()))));
                        v.setSalaryMax(1);
                        v.setSalaryMin(1);
                        v.setUrl(xssClear(element.getElementsByTag("a").first().attr("href")));
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

    public static List<VacancyTo> getNofluffjobsVacancies(Elements elements, Freshen freshen) {
        List<VacancyTo> list = new ArrayList<>();
        log.info("elements {}", elements.size());
        for (Element element : elements) {
            try {
                LocalDate localDate = getDateNofluffjobs(element.getElementsByClass("new-label").text());
                if(localDate.isAfter(reasonDateToLoad)) {
                    String title = getCorrectTitle(xssClear(element.getElementsByClass("posting-title__position").text().trim()));
                    if (/*getMatchesLanguage(freshen, title, "null")*/ true) {
                        VacancyTo v = new VacancyTo();
                        v.setTitle(addLabelToTitle(title, freshen));
                        v.setEmployerName(xssClear(element.getElementsByClass("posting-title__company").text()).substring(2).trim());
                        v.setAddress(getToNofluffjobs(xssClear(element.getElementsByTag("nfj-posting-item-city").text())));
                        v.setSalaryMax(salaryMax(getCorrectSalary(xssClear(element.getElementsByAttributeValueStarting("class", "text-truncate badgy salary").text().trim())), element));
                        v.setSalaryMin(salaryMin(getCorrectSalary(xssClear(element.getElementsByAttributeValueStarting("class", "text-truncate badgy salary").text().trim())), element));
                        v.setUrl(getCorrectUrl("nofluffjobs", xssClear(element.getElementsByTag("a").attr("href").trim())));
                        String skills = getCorrectSkills(xssClear(element.getElementsByTag("common-posting-item-tag").text().trim()));
                        v.setSkills(hasText(skills) ? skills : "see the card on the link");
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
                    String skills, title = getCorrectTitle(xssClear(element.getElementsByClass("card-title").text().trim()));
                    skills = getCorrectSkills(xssClear(element.getElementsByClass("card-description").text().trim()));
                    if (getMatchesLanguage(freshen, title, skills) && skills.length() > 2) {
                        VacancyTo v = new VacancyTo();
                        v.setTitle(addLabelToTitle(title, freshen));
                        v.setEmployerName(getCorrectEmployerName(xssClear(element.getElementsByClass("company-name").text().trim())));
                        v.setAddress(getCorrectAddress(xssClear(element.getElementsByClass("location").text().trim())));
                        v.setSalaryMax(salaryMax(getCorrectSalary(xssClear(element.getElementsByClass("salary").text().trim())), element));
                        v.setSalaryMin(salaryMin(getCorrectSalary(xssClear(element.getElementsByClass("salary").text().trim())), element));
                        v.setUrl(getCorrectUrl("rabota", xssClear(element.getElementsByTag("a").attr("href").trim())));
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
                LocalDate localDate = getCorrectDate(xssClear(element.getElementsByClass("date").text().trim()));
                if(localDate.isAfter(reasonDateToLoad)) {
                    String skills, title = getCorrectTitle(xssClear(element.getElementsByAttribute("title").first().text().trim()));
                    skills = getCorrectSkills(xssClear(element.getElementsByClass("job-snippet").text().trim()));
                    if (/*getMatchesLanguage(freshen, title, skills) &&*/ skills.length() > 2 && skills.length() < 1000) {
                        VacancyTo v = new VacancyTo();
                        v.setTitle(title);
                        v.setEmployerName(getCorrectEmployerName(xssClear(xssClear(element.getElementsByClass("companyName").text().trim()))));
                        v.setAddress(getCorrectAddress(xssClear(element.getElementsByClass("companyLocation").text().trim())));
                        v.setSalaryMax(1);
                        v.setSalaryMin(1);
                        v.setUrl(getCorrectUrl("indeed", xssClear(element.getElementsByTag("a").tagName("a").attr("data-jk").trim())));
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
//            System.out.println(i++ +"  ----------------------------------------------------------------------------------------");
            try {
                LocalDate localDate = null;
                try {
//                    localDate = getCorrectDate(xssClear(element.select("div.caption").last().text().trim()));
                    localDate = getCorrectDate(xssClear(element.getElementsByClass("caption _04443").text().trim()));
                } catch (Exception e) {
                    localDate = getCorrectDate(null);
                }
                if (localDate.isAfter(reasonDateToLoad)) {
                    String skills, title = getCorrectTitle(xssClear(element.getElementsByTag("header").tagName("span").text().trim()));
                    skills = getCorrectSkills(xssClear(element.getElementsByClass("_10840").text()));
                    if(title.toLowerCase().contains("водитель такси") || skills.toLowerCase().contains("виграй")) {
                        continue;
                    }
                    title = title.toLowerCase().contains("рекрутер") ? title.concat(", junior") : title;
                    if (getMatchesLanguage(freshen, title, skills) && skills.length() > 2) {
                        VacancyTo v = new VacancyTo();
                        v.setTitle(addLabelToTitle(title, freshen));
//                        v.setEmployerName(getCorrectEmployerName(xssClear(element.getElementsByClass("_786d5").text().trim())));
//                        v.setEmployerName(getCorrectEmployerName(xssClear(element.getElementsByClass("GoodEmployerWidget_company__Ya0gV").text().trim())));
                        v.setEmployerName(getCorrectEmployerName(xssClear(element.getElementsByClass("efaa8").text().trim())));
//                        v.setAddress(getCorrectAddress(xssClear(Optional.of(element.getElementsByClass("_36dc5 d6b7e _4f6da a4850 _2128e _8e9e1").next().first().text()).orElse("").trim())));
//                        v.setAddress(getCorrectAddress(xssClear(Optional.of(element.getElementsByClass("caption JobCard_location_label__caption__l6HAP").text().trim()).orElse("").trim())));
                        v.setAddress(getCorrectAddress(xssClear(Optional.of(element.getElementsByClass("caption d7cb2").text().trim()).orElse("").trim())));
//                        v.setSalaryMax(salaryMax(getCorrectSalary(xssClear(element.getElementsByTag("section").tagName("p").text())), element));
//                        v.setSalaryMin(salaryMin(getCorrectSalary(xssClear(element.getElementsByTag("section").tagName("p").text())), element));
                        v.setSalaryMax(salaryMax(getCorrectSalary(xssClear(element.getElementsByClass("a7943").text())), element));
                        v.setSalaryMin(salaryMin(getCorrectSalary(xssClear(element.getElementsByClass("a7943").text())), element));
                        v.setUrl(getCorrectUrl("jooble", xssClear(element.getElementsByTag("article").attr("id").trim())));
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
                        employerName = xssClear(element.getElementsByTag("img").attr("alt"));
                        if(employerName == null || employerName.equals("")) {
                            employerName = xssClear(element.getElementsByClass("add-top-xs").tagName("span").tagName("b").eachText().get(0));
                            log.info("other tag employerName={}", employerName);
                        }
                        v.setTitle(addLabelToTitle(title, freshen));
                        v.setEmployerName(getCorrectEmployerName(employerName));
                        v.setAddress(getCorrectAddress(xssClear(element.getElementsByClass("add-top-xs").first().children().next().next().text())));
                        v.setSalaryMax(salaryMax(getCorrectSalary(xssClear(element.getElementsByTag("b").tagName("b").first().text().trim())), element));
                        v.setSalaryMin(salaryMin(getCorrectSalary(xssClear(element.getElementsByTag("b").tagName("b").first().text().trim())), element));
                        v.setUrl(getCorrectUrl("work", xssClear(element.getElementsByTag("a").attr("href"))));
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
                    String address, skills, title = getCorrectTitle(xssClear(element.getElementsByClass("heading heading_level_3").text().trim()));
                    skills = getCorrectSkills(xssClear(element.getElementsByClass("serp-vacancy__requirements").text().trim()));
                    if (getMatchesLanguage(freshen, title, skills) && skills.length() > 2) {
                        VacancyTo v = new VacancyTo();
                        v.setTitle(title);
                        v.setEmployerName(getCorrectEmployerName(xssClear(element.getElementsByClass("serp-vacancy__company").text().trim())));
                        address = xssClear(element.select("div.address").text().trim());
                        v.setAddress(getCorrectAddress(getToYandex(address, freshen)));
                        v.setSalaryMax(salaryMax(getCorrectSalary(xssClear(element.getElementsByClass("serp-vacancy__salary").text().trim())), element));
                        v.setSalaryMin(salaryMin(getCorrectSalary(xssClear(element.getElementsByClass("serp-vacancy__salary").text().trim())), element));
                        v.setUrl(getCorrectUrlYandex(xssClear(element.getElementsByTag("a").attr("href"))));
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

}
/*
                    System.out.println("------------------------------------------------------------------------------------------");
                    String line = element.getElementsByTag("a").last().text().trim();
                    System.out.println("element:\n" + element);
                    System.out.println("getCorrectEmployerName=" + getCorrectEmployerName(xssClear(line)));


                        String salary = element.getElementsByAttributeValueStarting("class", "text-truncate badgy salary").text().trim();
                        System.out.println("salary="+salary);
                        String correctSalary = getCorrectSalary(xssClear(salary));
                        System.out.println("correctSalary="+correctSalary);
                        int salaryMin = salaryMin(correctSalary, null);
                        System.out.println("salaryMin="+salaryMin);
*/
