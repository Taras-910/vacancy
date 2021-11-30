package ua.training.top.util.collect.data;

import static ua.training.top.util.collect.data.DataUtil.*;

public class UrlUtil {

    public static String getToUrl(String text, String url){
        String prefix = switch (text) {
            case djinni -> "https://djinni.co";
            case habr -> "https://career.habr.com";
            case nofluff -> "https://nofluffjobs.com";
            case indeed -> "https://ua.indeed.com/viewjob?jk=";
            case jooble -> "https://ua.jooble.org/desc/";
            case rabota -> "https://rabota.ua";
            case work -> "https://www.work.ua";
            default -> "";
        };
        return getBuild(prefix).append(url).toString();
    }

    public static String getPage(String siteName, String page) {
        return switch (siteName) {
            case djinni, jobcareer -> page.equals("1") ? "" : "page=".concat(page);
            case grc -> page.equals("0") ? "" : "&page=".concat(page);
            case habr, work -> page.equals("1") ? "" : "&page=".concat(page);
            case rabota -> page.equals("1") ? "" : "/pg".concat(page);
            case indeed -> page.equals("0") ? "" : "&start=".concat(String.valueOf(Integer.parseInt(page)*10));
            case jooble -> page.equals("1") ? "" : "&p=".concat(page);
            default -> page;
        };
    }

    public static String getLevel(String site, String level) {
        return switch (level) {
            case trainee -> switch (site) {
                case djinni -> "exp_level=no_exp";
                case grc -> "&employment=probation";
                case habr -> "&qid=1";
                case jobcareer -> "-trainee";
                case linkedin -> "f_E=1&";
                case nofluff -> "%20seniority%3Dtrainee";
                case rabota -> "&profLevelIDs=2";
                case jooble -> "&workExp=2";
                case work -> "+trainee";
                default -> "";
            };
            case junior -> switch (site) {
                case djinni -> "exp_level=1y";
                case grc -> "&experience=noExperience";
                case habr -> "&qid=3";
                case jobcareer -> "-junior";
                case jobs -> "&exp=0-1";
                case linkedin -> "f_E=2&";
                case nofluff -> "%20seniority%3Djunior";
                case rabota -> "&profLevelIDs=3";
                case jooble -> "&workExp=1";
                case work -> "+junior";
                default -> "";
            };
            case middle -> switch (site) {
                case djinni -> "exp_level=2y";
                case grc -> "&experience=between1And3";
                case habr -> "&qid=4";
                case jobcareer -> "-middle";
                case jobs -> "&exp=1-3";
                case linkedin -> "f_E=3&";
                case nofluff -> "%20seniority%3Dmid";
                case rabota -> "&profLevelIDs=4";
                case jooble -> "%20middle";
                case work -> "+middle";
                default -> "";
            };
            case senior -> switch (site) {
                case djinni -> "exp_level=3y";
                case grc -> "&experience=between3And6";
                case habr -> "&qid=5";
                case jobcareer -> "-senior";
                case jobs -> "&exp=3-5";
                case linkedin -> "f_E=4&";
                case nofluff -> "%20seniority%3Dsenior";
                case rabota -> "&profLevelIDs=5";
                case jooble -> "%20senior";
                case work -> "+senior";
                default -> "";
            };
            case expert -> switch (site) {
                case djinni -> "exp_level=5y";
                case grc -> "&experience=experience=moreThan6";
                case habr -> "&qid=6";
                case jobcareer -> "-expert";
                case jobs -> "&exp=5plus";
                case linkedin -> "f_E=5&";
                case nofluff -> "%20seniority%3Dexpert";
                case rabota -> "&profLevelIDs=6";
                case jooble -> "%20expert";
                case work -> "+expert";
                default -> "";
            };
            default -> "";
        };
    }
}
