package ua.training.top.util.parser.data;

import static org.springframework.util.StringUtils.hasText;

public class CorrectSkills {

    public static String getCorrectSkills(String skills) {
        skills = skills.contains("Java Script") ? skills.replaceAll("Java Script", "JavaScript") : skills;
        return hasText(skills) ? skills : "see the card on the link";
    }
}
