package ua.training.top.aggregator.rate;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.training.top.model.Rate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static java.lang.String.format;
import static ua.training.top.aggregator.InstallationUtil.reCallRate;
import static ua.training.top.util.collect.DocumentUtil.getDocument;
import static ua.training.top.util.collect.data.ConstantsUtil.error;
import static ua.training.top.util.collect.data.DateToUtil.getToLocalDate;
import static ua.training.top.util.collect.xss.XssUtil.xssClear;

public class TradingEconomicsProvider implements RateProvider {
    private final static Logger log = LoggerFactory.getLogger(TradingEconomicsProvider.class);
    private static final String url = "https://tradingeconomics.com/currencies?base=%s";
//    https://tradingeconomics.com/currencies?base=usd

    public List<Rate> getRates(String baseCurrency) {
        log.info("get rates of baseCurrency {}", baseCurrency);
        Set<Rate> set = new LinkedHashSet<>();
        Document doc = getDocument(format(url, baseCurrency));
        Elements elements = doc == null ? null : doc.getElementsByAttributeValueStarting("class", "datatable-row");
        if (elements != null && elements.size() != 0) {
            set.addAll(getRatesList(elements));
        }
        reCallRate(set.size());
        return new ArrayList<>(set);
    }

    private List<Rate> getRatesList(Elements elements) {
        List<Rate> list = new ArrayList();
        for (Element element : elements) {
            try {
                LocalDate localDate = getToLocalDate(xssClear(element.getElementsByAttributeValue("id", "date").text()).replace("/", " "));
                if (/*localDate.isAfter(reasonValidRate)*/true) {
                    Rate r = new Rate();
                    r.setName(xssClear(element.getElementsByTag("a").tagName("b").text()));
                    r.setValue(Double.parseDouble(xssClear(element.getElementsByAttributeValue("id", "p").text())));
                    r.setLocalDate(localDate);
                    list.add(r);
                }
            } catch (Exception e) {
                log.error(error, e.getLocalizedMessage(), element);
            }
        }
        return list;
    }
}
