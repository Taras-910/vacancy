package ua.training.top.util.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class DocumentUtil {
    private static Logger log = LoggerFactory.getLogger(DocumentUtil.class);
    public static Document getDocument(String url){
        Document document = null;
        try {
            document = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/12.1.2 Safari/605.1.15")
                    .referrer("")
                    .get();

        } catch (IOException e) {
            log.error("There may be no internet connection or exception={} by url={} ", e.getMessage(), url);
        }
        return document;
    }
}
