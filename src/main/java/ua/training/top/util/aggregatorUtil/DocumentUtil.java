package ua.training.top.util.aggregatorUtil;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static ua.training.top.util.MessageUtil.document_url;
import static ua.training.top.util.MessageUtil.internet_connection_error;
import static ua.training.top.util.aggregatorUtil.data.ConstantsUtil.agent;

public class DocumentUtil {
    private static final Logger log = LoggerFactory.getLogger(DocumentUtil.class);
    public static Document getDocument(String url){
        log.info(document_url, url);
        Document document = null;
        try {
            document = Jsoup.connect(url)
                    .userAgent(agent)
                    .referrer("")
                    .get();
        } catch (IOException e) {
            log.error(internet_connection_error, e.getMessage(), url);
        }
        return document;
    }
}
