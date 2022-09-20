package ua.training.top.util.collect.data;

import static ua.training.top.util.collect.data.DataUtil.*;

public class UrlUtil {

    public static String getToUrl(String text, String url){
        String prefix = switch (text) {
            case djinni -> "https://djinni.co";
            case nofluff -> "https://nofluffjobs.com";
            case indeed -> "https://ua.indeed.com/viewjob?jk=";
            case indeed_ca -> "https://ca.indeed.com/viewjob?jk=";
            case jooble -> "https://ua.jooble.org/desc/";
            case rabota -> "https://rabota.ua";
            case work -> "https://www.work.ua";
            case jobBank -> "https://www.jobbank.gc.ca";
            default -> "";
        };
        return getJoin(prefix,url);
    }

    public static String getPage(String siteName, String page) {
        return switch (siteName) {
            case jobcareer, rabota -> page.equals("1") ? "" : getJoin("page=",page);
            case djinni, work, itJob -> page.equals("1") ? "" : getJoin("&page=",page);
            case indeed, indeed_ca -> page.equals("0") ? "" : getJoin("&start=",page,"0");
            case jooble -> page.equals("1") ? "" : getJoin("&p=",page);
            default -> page;
        };
    }

    public static String getLevel(String site, String level) {
        return switch (level) {
            case trainee -> switch (site) {
                case djinni -> "exp_level=no_exp";
                case jobcareer -> "-trainee";
                case linkedin -> "f_E=1&";
                case nofluff -> "%20seniority%3Dtrainee";
                case rabota -> "&profLevelIDs=2";
                case jooble -> "&workExp=2";
                case work -> "+trainee";
                case zaplata -> "stazhanti-studenti/";
                default -> "";
            };
            case junior -> switch (site) {
                case djinni -> "exp_level=1y";
                case jobcareer -> "-junior";
                case jobs -> "exp=0-1";
                case linkedin -> "f_E=2&";
                case nofluff -> "%20seniority%3Djunior";
                case rabota -> "&profLevelIDs=3";
                case jooble -> "&workExp=1";
                case work -> "+junior";
                case zaplata -> "stazhanti-studenti/";
                default -> "";
            };
            case middle -> switch (site) {
                case djinni -> "exp_level=2y";
                case jobcareer -> "-middle";
                case jobs -> "exp=1-3";
                case linkedin -> "f_E=3&";
                case nofluff -> "%20seniority%3Dmid";
                case rabota -> "&profLevelIDs=4";
                case jooble -> "%20middle";
                case work -> "+middle";
                case zaplata -> "sluzhiteli-rabotnitsi/";
                default -> "";
            };
            case senior -> switch (site) {
                case djinni -> "exp_level=3y";
                case jobcareer -> "-senior";
                case jobs -> "exp=3-5";
                case linkedin -> "f_E=4&";
                case nofluff -> "%20seniority%3Dsenior";
                case rabota -> "&profLevelIDs=5";
                case jooble -> "%20senior";
                case work -> "+senior";
                case zaplata -> "eksperti-spetsialisti/";
                default -> "";
            };
            case expert -> switch (site) {
                case djinni -> "exp_level=5y";
                case jobcareer -> "-expert";
                case jobs -> "exp=5plus";
                case linkedin -> "f_E=5&";
                case nofluff -> "%20seniority%3Dexpert";
                case rabota -> "&profLevelIDs=6";
                case jooble -> "%20expert";
                case work -> "+expert";
                case zaplata -> "menidzhmant/";
                default -> "";
            };
            default -> "";
        };
    }
}
