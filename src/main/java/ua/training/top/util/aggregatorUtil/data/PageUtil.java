package ua.training.top.util.aggregatorUtil.data;

import static java.lang.Math.min;
import static ua.training.top.aggregator.InstallationUtil.limitCallPages;
import static ua.training.top.util.aggregatorUtil.data.CommonUtil.getJoin;
import static ua.training.top.util.aggregatorUtil.data.ConstantsUtil.*;

public class PageUtil {

    public static String getPage(String siteName, String page) {
        return switch (siteName) {
            case jobcareer, rabota, djinni -> page.equals("1") ? "" : getJoin("page=",page);
            case work, itJob -> page.equals("1") ? "" : getJoin("&page=",page);
            case indeed, indeed_ca -> page.equals("0") ? "" : getJoin("&start=",page,"0");
            case jooble -> page.equals("1") ? "" : getJoin("&p=",page);
            case itJobsWatch -> page.equals("0") ? "" : getJoin("s=date", page);
            case reed -> page.equals("1") ? "" : getJoin("pageno=", page, "&");
            case cwjobs -> page.equals("1") ? "" : getJoin("page=", page, "&");
            case nofluff -> getJoin("&page=",page);
            default -> page;
        };
    }

    public static int getMaxPages(String site, String city) {
        int pages = switch (city) {
            case "all" -> switch (site) {
                case djinni -> 120;
                case itJob, jabsBG -> 8;
                case linkedin, zaplata -> 2;
                case jobsmarket, jooble -> 10;
                case indeed_ca -> 3;
                case nofluff -> 4;
                case rabota -> 25;
                case indeed -> 1/*25*/;
                case work -> 27;
                default -> 1;
            };
            case "foreign" -> switch (site) {
                case djinni -> 120;
                case itJob, jabsBG -> 8;
                case jobsmarket -> 10;
                case linkedin, zaplata -> 2;
                case indeed_ca -> 1/*3*/;
                case nofluff -> 4;
                case work -> 3;
                default -> 1;
            };
            case "remote" -> switch (site) {
                case djinni -> 100;
                case itJob, jabsBG -> 8;
                case jooble -> 13;
                case jobsmarket -> 10;
                case linkedin, zaplata -> 3;
                case indeed, indeed_ca -> 1/*7*/;
                case nofluff -> 4;
                case rabota, work -> 9;
                default -> 1;
            };
            case "україна" -> switch (site) {
                case djinni -> 6;
                case linkedin, jooble -> 2; //100-52 10-18 3-18
                case rabota -> 6;
                case indeed -> 1/*25*/;
                case work -> 27;
                default -> 1;
            };
            case "київ" -> switch (site) {
                case djinni -> 100;
                case linkedin -> 2;
                case rabota -> 12;
                case indeed -> 1/*5*/;
                case jooble -> 12/*22*/;
                case work  -> 13;
                default -> 1;
            };
            case "одеса", "дніпро" -> switch (site) {
                case djinni -> 30;
                case linkedin, rabota -> 2;
                case jooble -> 6;
                case work -> 3;
                default -> 1;
            };
            case "харків" -> switch (site) {
                case djinni -> 46;
                case linkedin -> 2;
                case indeed -> 1/*3*/;
                case rabota -> 4;
                case jooble -> 11;
                case work -> 5;
                default -> 1;
            };
            case "мінськ" -> switch (site) {
                case linkedin, jooble -> 2;
                case work -> 3;
                default -> 1;
            };
            case "львів" -> switch (site) {
                case djinni -> 40;
                case linkedin -> 2;
                case rabota, work -> 3;
                case jooble -> 12;//20
                case indeed -> 1/*2*/;
                default -> 1;
            };
            case "ужгород",  "запоріжжя",  "черкаси",  "вінниця",  "івано-франківськ",  "полтава"  -> 1;
            default -> switch (site) {
                case djinni -> 5;
                case linkedin, rabota, jobsmarket, zaplata -> 2;
                case nofluff -> 7;
                case jooble -> 3;
                case indeed, indeed_ca -> 1/*100*/;
                case work -> 15;
                default -> 1;
            };
        };
        return min(limitCallPages, pages);
    }
}

