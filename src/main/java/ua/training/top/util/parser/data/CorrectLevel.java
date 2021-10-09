package ua.training.top.util.parser.data;

public class CorrectLevel {

    public static String getLevelGrc(String level) {
        switch (level) {
            case "trainee" -> level = "employment=probation";
            case "junior" -> level = "experience=noExperience";
            case "senior" -> level = "experience=between3And6";
            case "expert" -> level = "experience=experience=moreThan6";
            default -> level = "experience=between1And3";
        }
        return level;
    }

    public static String getLevelHabr(String level) {
        switch (level) {
            case "trainee" -> level = "1";
            case "junior" -> level = "3";
            case "senior" -> level = "5";
            case "expert" -> level = "6";
            default -> level = "4";
        }
        return level;
    }

    public static String getLevelJob(String level) {
        switch (level) {
            case "trainee" -> level = "&exp=0-1";
            case "senior" -> level = "&exp=3-5";
            case "expert" -> level = "&exp=5plus";
            default -> level = "&exp=1-3";
        }
        return level;
    }

    public static String getLevelLinkedin(String level) {
        switch (level) {
            case "trainee" -> level = "1";
            case "junior" -> level = "2";
            case "senior" -> level = "4";
            case "expert" -> level = "5";
            default -> level = "3";
        }
        return level;
    }

    public static String getLevelNofluff(String level) {
        switch (level) {
            case "trainee": level = "trainee";
                break;
            case "junior":
            case "senior":
            case "expert":
                break;
            case "middle":
            default:
                level = "mid";
        }
        return level;
    }

    public static String getLevelRabota(String level) {
        switch (level) {
            case "trainee", "junior" -> level = "2";
            case "senior" -> level = "4";
            case "expert" -> level = "5";
            default -> level = "3";
        }
        return level;
    }

    public static String getLevelJooble(String level) {
        switch (level) {
            case "trainee" -> level = "&workExp=2";
            case "junior" -> level = "&workExp=1";
            default -> level = "";
        }
        return level;
    }

    public static String getLevelWork(String level) {
        switch (level) {
            case "trainee" -> level = "&student=1";
            case "junior" -> level = "&experience=1";
            default -> level = "";
        }
        return level;
    }

    // level (middle: &experience=FROM_1_TO_2   senior: &experience=FROM_3_TO_5   expert: &experience=FROM_6)
    public static String getLevelYandex(String level) {
        switch (level) {
            case "trainee" -> level = "";
            case "junior" -> level = "&experience=NO_EXPERIENCE";
            case "senior" -> level = "&experience=FROM_3_TO_5";
            case "expert" -> level = "&experience=FROM_6";
            default -> level = "&experience=FROM_1_TO_2";
        }
        return level;
    }

}

