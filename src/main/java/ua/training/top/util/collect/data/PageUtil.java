package ua.training.top.util.collect.data;

import static java.lang.Math.min;
import static ua.training.top.aggregator.installation.InstallationUtil.limitCallPages;
import static ua.training.top.util.collect.data.DataUtil.*;

public class PageUtil {

    public static int getMaxPages(String site, String city) {
        int pages = switch (city) {
            case "all" -> switch (site) {
                case djinni -> 120;
                case grc -> 40;
                case habr -> 20;
                case jobcareer, linkedin -> 2;
                case jobsmarket, jooble -> 10;
                case nofluff -> 4;
                case rabota, indeed -> 25;
                case work -> 27;
                default -> 1;
            };
            case "foreign" -> switch (site) {
                case djinni -> 120;
                case jobsmarket -> 10;
                case linkedin -> 2;
                case nofluff -> 4;
                case work -> 3;
                default -> 1;
            };
            case "remote" -> switch (site) {
                case djinni -> 100;
                case grc -> 49;
                case habr, jooble -> 13;
                case jobcareer -> 2;
                case jobsmarket -> 10;
                case linkedin -> 3;
                case indeed -> 7;
                case nofluff -> 4;
                case rabota, work -> 9;
                default -> 1;
            };
            case "украина" -> switch (site) {
                case djinni, grc -> 6;
                case jobcareer -> 5;
                case linkedin, jooble -> 2; //100-52 10-18 3-18
                case rabota -> 6;
                case indeed -> 25;
                case work -> 27;
                default -> 1;
            };
            case "киев" -> switch (site) {
                case djinni -> 100;
                case grc -> 4;
                case habr -> 20;
                case jobcareer, linkedin -> 2;
                case rabota -> 12;
                case indeed -> 5;
                case jooble -> 12;//22
                case work  -> 13;
                default -> 1;
            };
            case "санкт-петербург" -> switch (site) {
                case grc, habr -> 20;
                case jobcareer -> 4;
                case linkedin -> 3;
                default -> 1;
            };
            case "одесса" -> switch (site) {
                case djinni -> 30;
                case habr -> 10;
                case linkedin, rabota, indeed -> 2;
                case jooble -> 6;
                case work -> 3;
                default -> 1;
            };
            case "харьков" -> switch (site) {
                case djinni -> 46;
                case grc, linkedin -> 2;
                case habr -> 10;
                case indeed -> 3;
                case rabota -> 4;
                case jooble -> 11;
                case work -> 5;
                default -> 1;
            };
            case "минск" -> switch (site) {
                case grc -> 6;
                case habr -> 20;
                case jobcareer, jooble, linkedin -> 2;
                case work -> 3;
                default -> 1;
            };
            case "москва" -> switch (site) {
                case grc -> 40;
                case habr -> 100;
                case jobcareer -> 4;
                case linkedin -> 3;
                default -> 1;
            };
            case "львов" -> switch (site) {
                case djinni -> 40;
                case linkedin -> 2;
                case rabota, work -> 3;
                case jooble -> 12;//20
                case indeed -> 2;
                default -> 1;
            };
            default -> switch (site) {
                case djinni -> 5;
                case jobcareer, linkedin, rabota, jobsmarket -> 2;
                case nofluff -> 7;
                case jooble -> 3;
                case indeed -> 100;
                case work -> 15;
                default -> 1;
            };
        };
        return min(limitCallPages, pages);
    }
}
