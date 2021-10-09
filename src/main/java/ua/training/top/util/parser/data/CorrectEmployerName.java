package ua.training.top.util.parser.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.springframework.util.StringUtils.hasText;

public class CorrectEmployerName {
    public static final Logger log = LoggerFactory.getLogger(CorrectSkills.class);

    public static String getCorrectEmployerName(String companyName) {
        if (!hasText(companyName)) {
            log.error("there is companyName is null: see the card on the link");
            return "see the card on the link";
        }
        companyName = companyName.contains(" Профиль компании") ?
                companyName.replace("Профиль компании", "").trim() : companyName.trim();
        if (companyName.contains(",")) {
            String[] parts = companyName.split(",");
            companyName = parts.length > 1 ? parts[0].trim() : companyName;
        }
        return companyName;
    }
}
