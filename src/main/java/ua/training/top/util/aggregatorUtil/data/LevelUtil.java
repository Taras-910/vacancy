package ua.training.top.util.aggregatorUtil.data;

import static ua.training.top.util.aggregatorUtil.data.ConstantsUtil.*;

public class LevelUtil {

    public static String getLevel(String site, String level) {
        return switch (level) {
            case trainee -> switch (site) {
                case djinni -> "exp_level=no_exp";
                case jobcareer -> "-trainee";
                case linkedin -> "f_E=1&";
                case nofluff -> "%20seniority%3Dtrainee";
                case rabota -> "&profLevelIDs=2";
                case jooble -> "&workExp=2";
                case work -> "trainee";
                case zaplata -> "stazhanti-studenti/";
                default -> "";
            };
            case junior -> switch (site) {
                case djinni -> "exp_level=1y";
                case jobcareer -> "-junior";
                case jobs -> "&exp=0-1";
                case linkedin -> "f_E=2&";
                case nofluff -> "%20seniority%3Djunior";
                case rabota -> "&profLevelIDs=3";
                case jooble -> "&workExp=1";
                case work -> "junior";
                case zaplata -> "stazhanti-studenti/";
                default -> "";
            };
            case middle -> switch (site) {
                case djinni -> "exp_level=2y";
                case jobcareer -> "-middle";
                case jobs -> "&exp=1-3";
                case linkedin -> "f_E=3&";
                case nofluff -> "%20seniority%3Dmid";
                case rabota -> "&profLevelIDs=4";
                case jooble -> "%20middle";
                case work -> "middle";
                case zaplata -> "sluzhiteli-rabotnitsi/";
                default -> "";
            };
            case senior -> switch (site) {
                case djinni -> "exp_level=3y";
                case jobcareer -> "-senior";
                case jobs -> "&exp=3-5";
                case linkedin -> "f_E=4&";
                case nofluff -> "%20seniority%3Dsenior";
                case rabota -> "&profLevelIDs=5";
                case jooble -> "%20senior";
                case work -> "senior";
                case zaplata -> "eksperti-spetsialisti/";
                default -> "";
            };
            case expert -> switch (site) {
                case djinni -> "exp_level=5y";
                case jobcareer -> "-expert";
                case jobs -> "&exp=5plus";
                case linkedin -> "f_E=5&";
                case nofluff -> "%20seniority%3Dexpert";
                case rabota -> "&profLevelIDs=6";
                case jooble -> "%20expert";
                case work -> "expert";
                case zaplata -> "menidzhmant/";
                default -> "";
            };
            default -> "";
        };
    }
}
