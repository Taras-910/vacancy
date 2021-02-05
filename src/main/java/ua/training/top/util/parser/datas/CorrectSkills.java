package ua.training.top.util.parser.datas;

import static org.springframework.util.StringUtils.hasText;

public class CorrectSkills {

    public static String getSkills(String skills) {
        return hasText(skills) ? skills : "see the card on the link";
    }
}
