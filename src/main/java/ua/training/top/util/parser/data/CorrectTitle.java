package ua.training.top.util.parser.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.springframework.util.StringUtils.hasText;

public class CorrectTitle {
    public static final Logger log = LoggerFactory.getLogger(CorrectTitle.class);
    public static String getCorrectTitle(String title) {
        if (!hasText(title)) {
            log.error("there is title is null");
            return "see the card on the link";
        }
        title = title.contains("(ID") ? title.substring(0, title.indexOf("(ID")).trim() : title;
        return title.contains("Java Script") ? title.replaceAll("Java Script", "JavaScript") : title;
    }

    public static String getCorrectTitleJobsMarket(String title) {
        title = title.replaceAll("[^\\w]", " ").replaceAll("[20]","")
                .replaceAll("FSDET", "").replaceAll("m Fw Fd","")
                .replaceAll("FReact", "React").trim();
        return title;
    }
}
