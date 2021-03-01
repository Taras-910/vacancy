package ua.training.top.util.parser.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.springframework.util.StringUtils.hasText;

public class CorrectSkills {
    public static final Logger log = LoggerFactory.getLogger(CorrectSkills.class);
    public static String getCorrectSkills(String skills) {
        if (!hasText(skills)) {
            log.error("there is skills is null");
            return "";
        }
        skills = skills.contains("Java Script") ? skills.replaceAll("Java Script", "JavaScript") : skills.trim();
//        skills = skills.contains("Experience level:") ? skills.substring(skills.indexOf("Experience level:")) : skills;
        return skills;
    }
}
