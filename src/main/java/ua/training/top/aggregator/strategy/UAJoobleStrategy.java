package ua.training.top.aggregator.strategy;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.training.top.aggregator.util.jsoup.DocumentUtil;
import ua.training.top.to.VacancyNet;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static java.lang.String.format;
import static ua.training.top.aggregator.util.ProviderUtil.limitCallPages;
import static ua.training.top.aggregator.util.ProviderUtil.reCall;
import static ua.training.top.aggregator.util.jsoup.ElementUtil.getVacanciesJooble;

public class UAJoobleStrategy implements Strategy {
    private final static Logger log = LoggerFactory.getLogger(UAJoobleStrategy.class);
    private static final String URL_FORMAT = "https://ua.jooble.org/SearchResult?ukw=%s&rgns=%s&p=%s";
    //за 7 дней https://ua.jooble.org/SearchResult?ukw=java&rgns=Київ&date=3&p=1
    // за кордоном за 7 дн https://ua.jooble.org/SearchResult?ukw=java&rgns=за+кордоном&date=3&p=1

    protected Document getDocument(String city, String language, String page) {
        boolean other = city.equals("сша") || city.equals("польща") || city.equals("німеччина");
        String url = format(URL_FORMAT, language, city, page = other ? page : "".concat(page).concat("&date=3"));
        return DocumentUtil.getDocument(url);
    }

    @Override
    public List<VacancyNet> getVacancies(String city, String language){
        log.info("getVacancies city {} language={}", city, language);
        boolean other = city.equals("за_рубежем");
        String[] cityOrCountry = other ? new String[]{"сша", "польща", "німеччина"} : new String[]{city};
        List<VacancyNet> result = new ArrayList<>();
        for(String c : cityOrCountry) {
            Set<VacancyNet> set = new LinkedHashSet<>();
            String tempDoc = "";
            int page = 1;
            int limitEmptyDoc = 3;
            while (true) {
                Document doc = getDocument(c, language, String.valueOf(page));
//                Elements elements = doc == null ? null : doc.getElementsByAttribute("data-sgroup");
//                Elements elements = doc == null ? null : doc.getElementsByClass("left-static-block");
                Elements elements = doc == null ? null : doc.getElementsByClass("vacancy_wrapper");

                if (elements == null || elements.size() == 0 || tempDoc.equals(doc.text())) {
                    limitEmptyDoc --;
                }
                if (limitEmptyDoc == 0) break;
                tempDoc = doc.text();
                set.addAll(getVacanciesJooble(elements, language));
                if (page < limitCallPages) page++;
                else break;
            }
            result.addAll(set);
        }
        reCall(result.size(), new UAJoobleStrategy());
        return result;
    }
}


/*21:47:49.886 INFO  ua.training.top.aggregator.AggregatorController.lambda$main$5:90 -
vacancyNet № 18
VacancyNet{
title='Полировщик без опыта',
date='2020-12-08',
salary='1',
url='https://ua.jooble.org/jdp/601157651915622322/%D0%9F%D0%BE%D0%BB%D0%B8%D1%80%D0%BE%D0%B2%D1%89%D0%B8%D0%BA-%D0%B1%D0%B5%D0%B7-%D0%BE%D0%BF%D1%8B%D1%82%D0%B0-%D0%9A%D1%80%D0%B0%D0%BA%D0%BE%D0%B2%2C-%D0%9F%D0%BE%D0%BB%D1%8C%D1%88%D0%B0?ckey=java&rgn=29773&pos=1&p=1&age=213&brelb=100&premImp=1&recId=-466826015587957734&iRecId=-2170064053276636293',
skills='ИНФОРМАЦИЯ О ЗАВОД: Superior Industries Production Poland Sp. z o. o. является вторым по величине мировым поставщиком алюминиевых дисков для легковых автомобилей и одним из самых известных поставщиков продукции. Более подробно с деятельностью компании можно ознакомиться...',
city='Краків, Польща',
siteName='https://ua.jooble.org',
companyName='WORKLIFE_Ukraina'}

21:47:49.886 INFO  ua.training.top.aggregator.AggregatorController.lambda$main$5:90 -
vacancyNet № 19
VacancyNet{
title='Рaбота в Финляндии 3070 €. Европа. Без посредников. Германия.',
date='2020-11-11',
salary='1',
url='https://ua.jooble.org/desc/7401239913563010472?ckey=java&rgn=30777&pos=2&elckey=-2189005005424235756&p=1&sid=1145356875290631228&age=669&relb=100&brelb=100&bscr=428.86502&scr=428.86502&iid=1655597352396924403',
skills='В пакувальний цех виробництва меблевої фурнітури оголошено набір персоналу. В обов'язки входить: упаковка готової продукції (ручки, кріплення, петлі і так далі) Робочий графік: 240 годин на місяць. Оплата праці 3070 Євро в місяць. Проживання: надає роботодавець, 230 євро...',
city='Німеччина',
siteName='https://ua.jooble.org',
companyName=' не указано'}
*/
