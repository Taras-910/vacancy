package ua.training.top.util.aggregatorUtil.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.training.top.model.Rate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

import static java.util.List.of;
import static ua.training.top.service.RateService.mapRates;
import static ua.training.top.util.MessageUtil.*;
import static ua.training.top.util.aggregatorUtil.data.CommonUtil.*;
import static ua.training.top.util.aggregatorUtil.data.ConstantsUtil.*;
import static ua.training.top.util.aggregatorUtil.data.PatternUtil.pattern_find_salary;
import static ua.training.top.util.aggregatorUtil.data.PatternUtil.pattern_salary_transform_points;

public class SalaryToUtil {
    public static final Logger log = LoggerFactory.getLogger(SalaryToUtil.class);

    public static Integer[] getToSalaries(String originText) {            //₵ ₮
        originText = originText.toLowerCase();
        if (isEmpty(originText) || !isMatch(allSalaries, originText)) {
            return new Integer[]{1, 1};
        }
        String code = getCurrencyCode(originText);
        String text = getWithCode(originText, code);
        List<Double> values = getMonetaryAmount(text);
        if (!values.isEmpty()) {
            double period = getPeriod(getShortLength(text, code));
            double rate = getRate(code);
            Integer[] salaries = new Integer[2];
            int amountMin = getAmount(values.get(0), period, rate);
            salaries[0] = values.size() < 2 ? (isFrom(text) ? amountMin : 1) : amountMin;
            salaries[1] = values.size() < 2 ? (isFrom(text) ? 1 : amountMin) : getAmount(values.get(1), period, rate);
            return checkLimitUpAndDown(salaries);
        }
        return new Integer[]{1, 1};
    }

    public static int getAmount(double value, double period, double rate) {
        int amount = 1;
        try {
            amount = (int) (value * period / rate);
        } catch (NumberFormatException e) {
            log.error(error, e, value);
        }
        return amount;
    }

    private static Integer[] checkLimitUpAndDown(Integer[] amount) {
        Integer[] result = new Integer[2];
        boolean exceed = false;
        for (int i = amount.length - 1; i >= 0 ; --i) {
            int a = amount[i];
            if (a > 4000000) {
                exceed = true;
                log.error(wrong_salary_value, a/100);
            }
            result[i] = a < 30000 ? 1 : !exceed ? a : a / 12 < 5000000 ? a / 12 : 1;
        }
        return checkLikeTrue(result);
    }

    private static Integer[] checkLikeTrue(Integer[] result) {
        if (result[0] > result[1] && result[1] != 1 ) {
            result[1] = result[0];
            result[0] = 1;
        }
        return result;
    }

    private static List<Double> getMonetaryAmount(String text) {
        List<Double> list = new ArrayList();
        Matcher m = pattern_find_salary.matcher(text);
        while (m.find()) {
            String s = getReplace(m.group(), of(currency_code, space_code), "");
            list.add(getTransform(s.replaceAll(",", ".")));
        }
        return list;
    }

    private static double getTransform(String text) {
        Matcher m = pattern_salary_transform_points.matcher(text);
        while (m.find()) {
            text = text.replaceFirst("\\.", "");
        }
        String salary = text.replaceAll(punctuation_code, "");
        return  Float.parseFloat(salary) * (isContains(text, "k") ? 100000 : 100);
    }

    public static float getPeriod(String text) {
        return isMatch(yearAria, text) ? 1.0f / 12.0f : isMatch(dayAria, text) ? 22.0f :
                isMatch(hourAria, text) ? 22.0f * 8.0f : 1.0f;
    }

    public static String getCurrencyCode(String text) {
        return isMatch(cadAria, text) ? "₡" : isMatch(usdAria, text) ? "$" : isMatch(hrnAria, text) ?
                "₴" : isMatch(eurAria, text) ? "€" : isMatch(byrAria, text) ? "₱" : isMatch(plnAria, text) ?
                "₲" : isMatch(gbrAria, text) ? "₤" : isMatch(kztAria, text) ? "₸" : isMatch(czeAria, text) ?
                "₭" : isMatch(bgnAria, text) ? "₾" : "";
    }

    public static String getWithCode(String text, String code) {
        return switch (code) {
            case "$" -> getReplace(text, usdAria_regex, "\\$");
            case "₡" -> getReplace(text, cadAria_regex, code);
            case "₴" -> getReplace(text, hrnAria, code);
            case "€" -> getReplace(text, eurAria, code);
            case "₤" -> getReplace(text, gbrAria, code);
            case "₲" -> getReplace(text, plnAria, code);
            case "₸" -> getReplace(text, kztAria, code);
            case "₱" -> getReplace(text, byrAria, code);
            case "₭" -> getReplace(text, czeAria, code);
            case "₾" -> getReplace(text, bgnAria, code);
            default -> text;
        };
    }

    public static Double getRate(String currencyCode) {
        String name = switch (currencyCode) {
            case "₴" -> USDUAH;
            case "₲" -> USDPLN;
            case "€" -> USDEUR;
            case "₤" -> USDGBP;
            case "₸" -> USDKZT;
            case "₡" -> USDCAD;
            case "₱" -> USDBYR;
            case "₾" -> USDBGN;
            case "₭" -> USDCZK;
            default -> USDUSD;
        };
        Rate r = mapRates.getOrDefault(name, new Rate(null, null, 1.0, LocalDate.now()));
        if(r.getName() == null){
            log.error(currency_rate_not_found, name);
        }
        return r.getValueRate();
    }

    public static boolean isFrom(String originText) {
        return isMatch(of("от", "від", "from"), originText);
    }

    private static String getShortLength(String text, String code) {
        return text.length() < 50 ? text :
                text.substring(Math.max(text.indexOf(code) - 25, 0), Math.min(text.lastIndexOf(code) + 25, text.length()));
    }
}
