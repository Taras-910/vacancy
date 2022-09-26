package ua.training.top.util.collect.data;

import static ua.training.top.util.collect.data.DataUtil.*;

public class UrlUtil {

    public static String getToUrl(String site, String url){
        String prefix = switch (site) {
            case djinni -> "https://djinni.co";
            case cwjobs -> "https://www.cwjobs.co.uk/";
            case nofluff -> "https://nofluffjobs.com";
            case indeed -> "https://ua.indeed.com/viewjob?jk=";
            case indeed_ca -> "https://ca.indeed.com/viewjob?jk=";
            case jooble -> ".jooble.org/desc/";
            case rabota -> "https://rabota.ua";
            case reed -> "https://www.reed.co.uk/";
            case work -> "https://www.work.ua";
            case jobBank -> "https://www.jobbank.gc.ca";
            case itJobsWatch -> "https://www.itjobswatch.co.uk/";
            default -> "";
        };
        return getJoin(prefix,url);
    }
}
