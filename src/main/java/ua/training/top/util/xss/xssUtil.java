package ua.training.top.util.xss;

import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

public class xssUtil {

    public static String getXssCleaned(String unsafe){
        return Jsoup.clean(unsafe, Whitelist.basic());
    }
}

// safe from xss
        /*String unsafe =
                "<p><a href='http://example.com/' onclick='stealCookies()'>Link</a></p>";
        String safe = Jsoup.clean(unsafe, Whitelist.basic());
        // now: <p><a href="http://example.com/" rel="nofollow">Link</a></p>
        System.out.println(safe);*/

