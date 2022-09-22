package ua.training.top.util.collect.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.stream.Collectors;

import static java.lang.Float.parseFloat;
import static java.util.List.of;
import static ua.training.top.util.collect.data.DataUtil.*;
import static ua.training.top.util.collect.data.PatternUtil.*;

public class SalaryUtil {
    public static final Logger log = LoggerFactory.getLogger(SalaryUtil.class);
    public static final float
            rate_pln_to_usd = 4.75f,
            rate_eur_to_usd = 1.01f,
            rate_gbp_to_usd = 0.86f,
            rate_byn_to_usd = 2.52f,
            rate_hrn_to_usd = 36.91f,
            rate_rub_to_usd = 60.83f,
            rate_kzt_to_usd = 472.31f,
            rate_cad_to_usd = 1.31f,
            rate_cze_to_usd = 24.1f,
            rate_bgn_to_usd = 1.96f,
            usd_one_to_one = 1.0f;

     public static Integer[] getToSalaries(String originText) {            //₵ ₮
         originText = getRemovedZeroPart(originText).toLowerCase();
         if (isEmpty(originText) || !isMatch(allSalaries, originText)) {
            return new Integer[]{1, 1};
        }
        String text = originText.replaceAll(",", ".");
        String code = getCurrencyCode(text);
        List<String> values = getMonetaryAmount(text, code);
        if (!values.isEmpty()) {
            Integer[] salaries = new Integer[2];
            int amountMin = getAmount(values.get(0), code, text);
            salaries[0] = values.size() < 2 ? (isFrom(text) ? amountMin : 1) : amountMin;
            salaries[1] = values.size() < 2 ? (isFrom(text) ? 1 : amountMin) : getAmount(values.get(1), code, text);
            return salaries;
        }
        return new Integer[]{1, 1};
    }

    public static int getAmount(String valuesPart, String code, String text) {
        String value = valuesPart.replaceAll("\\.", "");
        int amount = 1;
        try {
            amount = (int) ((parseFloat(value) * getPeriod(text) / getRate(code) * getPoint(valuesPart))
                    * (pattern_is_kilo.matcher(text).find() ? 1000 : 1));
        } catch (NumberFormatException e) {
            log.error(error, e, value);
        }
        return amount <= 12000000 ? amount :  amount / 12 <= 12000000 ? amount / 12 : Math.min(amount / 100, 12000000);
    }

    public static List<String> getMonetaryAmount(String text, String code) {
        List<String> parts = new ArrayList<>();
        Matcher mt = pattern_monetary_amount.matcher(getReplacementText(text, code));
        while (mt.find()) {
            parts.add(mt.group());
        }
        List<String> amounts = parts.stream()
                .filter(p -> p.contains(code))
                .map(m -> getReplace(m, wasteSalary, ""))
                .collect(Collectors.toList()),
                monetaryAmounts = new ArrayList<>();
        amounts.forEach(s -> {
            if (pattern_is_number_greater_300.matcher(s).find()) {
                monetaryAmounts.add(s);
            }
        });
        return monetaryAmounts;
    }

    public static float getPeriod(String text) {
        return isMatch(yearAria, text) ? 1.0f / 12.0f : isMatch(dayAria, text) ? 22.0f :
                isMatch(hourAria, text) ? 22.0f * 8.0f : 1.0f;
    }

    private static int getPoint(String str) {
        int decimalPoint = str.length() - str.lastIndexOf(".");
        return str.indexOf(".") == -1 ? 100 : decimalPoint == 3 ? 1 : decimalPoint == 2 ? 10 : 100;
    }

    public static String getCurrencyCode(String text) {
        return isMatch(usdAria, text) ? "$" : isMatch(hrnAria, text) ? "₴" : isMatch(eurAria, text) ?
                "€" : isMatch(bynAria, text) ? "₱" : isMatch(rubAria, text) ? "₽" : isMatch(plnAria, text) ?
                "₲" : isMatch(gbrAria, text) ? "₤" : isMatch(kztAria, text) ? "₸" : isMatch(cadAria, text) ?
                "₡" : isMatch(czeAria, text) ? "₭" : isMatch(bgnAria, text) ? "₾" : "";
    }

    public static String getReplacementText(String text, String code) {
        text = text.replaceAll(monetary_amount_regex, code.equals("$") ? "\\$" : code.equals("kč") ? "₭" : code);
        return switch (code) {
            case "$" -> getReplace(text, usdAria, "\\$");
            case "₴" -> getReplace(text, hrnAria, "₴");
            case "€" -> getReplace(text, eurAria, "€");
            case "₽" -> getReplace(text, rubAria, "₽");
            case "₤" -> getReplace(text, gbrAria, "₤");
            case "₲" -> getReplace(text, plnAria, "₲");
            case "₸" -> getReplace(text, kztAria, "₸");
            case "₡" -> getReplace(text, cadAria, "₡");
            case "₱" -> getReplace(text, bynAria, "₱");
            case "₭" -> getReplace(text, czeAria, "₭");
            case "₾" -> getReplace(text, bgnAria, "₾");
            default -> text;
        };
    }

    public static Float getRate(String currencyCode) {
        return switch (currencyCode) {
            case "₴" -> rate_hrn_to_usd;
            case "₲" -> rate_pln_to_usd;
            case "€" -> rate_eur_to_usd;
            case "₤" -> rate_gbp_to_usd;
            case "₽" -> rate_rub_to_usd;
            case "₸" -> rate_kzt_to_usd;
            case "₡" -> rate_cad_to_usd;
            case "₱" -> rate_byn_to_usd;
            case "₾" -> rate_bgn_to_usd;
            case "₭" -> rate_cze_to_usd;
            default -> usd_one_to_one;
        };
    }

    public static boolean isFrom(String originText) { return isMatch(of("от", "від"), originText); }

    public static String getRemovedZeroPart(String text){
        Matcher m = pattern_remove_zero_part.matcher(text.toLowerCase());
        int i = 0;
        while(m.find()){
            text = getJoin(text.substring(0, m.start() - i), text.substring(m.end() - 1 - i));
            i += 3;
        }
        return text;
     }

    public static String getSalaryFromText(String title) {
        String text = getRemovedZeroPart(title).toLowerCase();
        String code = getCurrencyCode(text);
        String result = code;
        text = getReplacementText(text, code);
        Matcher m = pattern_salaries_from_title.matcher(text);
        while(m.find()){
            result = getJoin(result, m.group().replaceAll("[^\\d-k\\s]", "")
                    .replaceAll("k","000"));
        }
        result = result.indexOf("-") != -1 ?
                result.replace("-", getJoin(code.equals("₭")?"000000 -":" -", code)) : getJoin(code,result);
        return getJoin(result, title.indexOf("month") == -1 ? " year" : " month");
    }

}
