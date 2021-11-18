package ua.training.top.util.collect.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.lang.Float.parseFloat;
import static ua.training.top.util.collect.data.DataUtil.*;

public class SalaryUtil {
    public static final Logger log = LoggerFactory.getLogger(SalaryUtil.class);
    public static final Pattern patternMoneyAmount =
            Pattern.compile("((?:[\\d,\\.[–до\\-k-]\\s  &nbsp]+\\b)(\\s*)?(\\p{Sc}|ƒ))|((?:\\p{Sc}|ƒ)(\\s*)?[\\d,\\.[–до\\-k-]\\s  &nbsp]+\\b)");
    public static final float
            rate_pln_to_usd = 3.98f,
            rate_eur_to_usd = 0.86f,
            rate_gbp_to_usd = 0.73f,
            rate_byn_to_usd = 2.43f,
            rate_hrn_to_usd = 26.25f,
            rate_rub_to_usd = 71.78f,
            rate_kzt_to_usd = 426.74f,
            usd_one_to_one = 1.0f;

    public static Integer[] getToSalaries(String text) {
        if (isEmpty(text) || !isSalary(text)) {
            return new Integer[]{1, 1};
        }
        text = text.replaceAll(",", ".").toLowerCase();
        String currencyCode = getCurrencyCode(text);
        List<String> values = getMonetaryAmount(text, currencyCode);
        if (!values.isEmpty()) {
            Integer[] salaries = new Integer[2];
            int amountMin = getAmount(values.get(0), currencyCode, text);
            salaries[0] = values.size() < 2 ? (isFrom(text) ? amountMin : 1) : amountMin;
            salaries[1] = values.size() < 2 ? (isFrom(text) ? 1 : amountMin) :
                    getAmount(values.get(1), currencyCode, text);
            return salaries;
        }
        return new Integer[]{1, 1};
    }

    public static int getAmount(String amountString, String currencyCode, String originText) {
        int amount = 1;
        try {
            amount = (int) ((parseFloat(amountString) * getPeriod(originText) / getRate(currencyCode) * 100));
            amount = Math.min(amount, 10000000);
        } catch (NumberFormatException e) {
            log.error(error, e, amountString);
        }
        return amount * (originText.matches(is_kilo) ? 1000 : 1);
    }

    public static String getCurrencyCode(String originText) {
        return isUsd(originText) ? usd : isHrn(originText) ? hrn : isEur(originText) ? eur : isRub(originText) ? rub :
                isPln(originText) ? pln : isGbr(originText) ? gbp : isKzt(originText) ? kzt : isSByn(originText) ? byn : "";
    }

    public static List<String> getMonetaryAmount(String originText, String currencyCode) {
        List<String> parts = new ArrayList<>();
        originText = originText.replaceAll(monetary_amount_regex, currencyCode);
        Matcher matcher = patternMoneyAmount.matcher(getReplacementText(originText, currencyCode));
        while (matcher.find()) {
            parts.add(matcher.group());
        }
        List<String>
                amounts = parts.stream().filter(p -> p.contains(getBadge(currencyCode))).collect(Collectors.toList()),
                monetaryAmounts = new ArrayList<>();
        amounts.forEach(s -> {
            s = getReplace(s, wasteSalary, "");
            if (s.matches(is_salary_number)) {
                monetaryAmounts.add(s);
            }
        });
        return monetaryAmounts;
    }

    public static float getPeriod(String text) {
        return isYear(text) ? 1.0f / 12.0f : isDay(text) ? 22.0f : isHour(text) ? 22.0f * 8.0f : 1.0f;
    }

    public static boolean isYear(String text) { return yearAria.stream().anyMatch(text.toLowerCase()::contains); }

    public static boolean isDay(String text) {
        return dayAria.stream().anyMatch(text.toLowerCase()::contains);
    }

    public static boolean isHour(String text) {
        return hourAria.stream().anyMatch(text.toLowerCase()::contains);
    }

    public static boolean isHrn(String text) {
        return salaryHrn.stream().anyMatch(text.toLowerCase()::contains);
    }

    public static boolean isGbr(String text) {
        return salaryGbr.stream().anyMatch(text.toLowerCase()::contains);
    }

    public static boolean isPln(String text) {
        return salaryPln.stream().anyMatch(text.toLowerCase()::contains);
    }

    public static boolean isUsd(String text) {
        return salaryUsd.stream().anyMatch(text.toLowerCase()::contains);
    }

    public static boolean isEur(String text) {
        return salaryEur.stream().anyMatch(text.toLowerCase()::contains);
    }

    public static boolean isRub(String text) {
        return salaryRub.stream().anyMatch(text.toLowerCase()::contains);
    }

    public static boolean isKzt(String text) {
        return salaryKzt.stream().anyMatch(text.toLowerCase()::contains);
    }

    public static boolean isSByn(String text) {
        return salaryByn.stream().anyMatch(text.toLowerCase()::contains);
    }

    public static boolean isSalary(String salary) {
        return (allSalaries.stream().anyMatch(salary.toLowerCase()::contains));
    }

    public static String getReplacementText(String text, String moneyName) {
        return switch (moneyName) {
            case usd -> getReplace(text, salaryUsd, "\\$");
            case hrn -> getReplace(text, salaryHrn, "₴");
            case eur -> getReplace(text, salaryEur, "€");
            case rub -> getReplace(text, salaryRub, "₽");
            case gbp -> getReplace(text, salaryGbr, "£");
            case pln -> getReplace(text, salaryPln, "₧");
            case kzt -> getReplace(text, salaryKzt, "₸");
            case byn -> getReplace(text, salaryByn, "฿");
            default -> text;
        };
    }

    public static Float getRate(String moneyName) {
        return switch (moneyName) {
            case hrn -> rate_hrn_to_usd;
            case pln -> rate_pln_to_usd;
            case eur -> rate_eur_to_usd;
            case gbp -> rate_gbp_to_usd;
            case rub -> rate_rub_to_usd;
            case kzt -> rate_kzt_to_usd;
            case byn -> rate_byn_to_usd;
            default -> usd_one_to_one;
        };
    }

    public static String getBadge(String moneyName) {
        return switch (moneyName) {
            case usd -> "$";
            case hrn -> "₴";
            case eur -> "€";
            case rub -> "₽";
            case gbp -> "£";
            case kzt -> "₸";
            case pln -> "₧";
            default -> "฿";
        };
    }
}
