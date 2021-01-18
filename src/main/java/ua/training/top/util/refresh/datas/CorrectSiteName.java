package ua.training.top.util.refresh.datas;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CorrectSiteName {
    public static String getSiteName(String url) {
        Matcher matcher = Pattern.compile("h.+//.+?/").matcher(url);
        while (matcher.find()) {
            url = url.substring(matcher.start(), matcher.end());
        }
        return url;
    }


}
