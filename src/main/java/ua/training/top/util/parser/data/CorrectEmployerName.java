package ua.training.top.util.parser.data;

import static org.springframework.util.StringUtils.hasText;

public class CorrectEmployerName {
    public static String getCorrectEmployerName(String companyName){
        companyName = companyName.contains(" Профиль компании") ?
                companyName.replace("Профиль компании", "").trim() : companyName.trim();
        companyName = companyName.contains(",") ? companyName.split(",")[0].trim() : companyName;
        return hasText(companyName) ? companyName : "see the card on the link" ;
    }
}
