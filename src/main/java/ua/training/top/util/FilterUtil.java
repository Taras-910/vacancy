package ua.training.top.util;

import ua.training.top.model.Freshen;
import ua.training.top.model.Vacancy;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import static java.util.List.of;
import static ua.training.top.util.aggregatorUtil.data.CommonUtil.*;
import static ua.training.top.util.aggregatorUtil.data.ConstantsUtil.*;

public class FilterUtil {
    public static Map<String, List<String>> mapAriaFilter;
    static {
        mapAriaFilter = new TreeMap<>();
        FilterMapUtil fau = new FilterMapUtil();
        List<String> list = getCommonList(of(levelAria, foreignAria, remoteAria, uaAria, citiesUA, plnAria, citiesPl,
                caAria, citiesCa, ukAria, citiesUK, bgAria, citiesBg, deAria, citiesDe, ilAria, citiesIl, uaAria,
                citiesUA, czAria, citiesCz, skAria, citiesSk, aeAria, citiesAe, frAria, citiesFr, itAria, citiesIt,
                fiAria, citiesFi, chAria, citiesCh, seAria, citiesSe, otherAria));
        mapAriaFilter = list.stream()
                .distinct()
                .collect(Collectors.toMap(s -> s, fau::getAria));
    }

    public static List<Vacancy> getFilter(List<Vacancy> vacancies, Freshen f) {
        return vacancies.stream()
                .filter(v -> isSuit(v, f.getLanguage(), "lang")
                        && isSuit(v, f.getLevel(), "level")
                        && isSuit(v, f.getWorkplace(), "work"))
                .collect(Collectors.toList());
    }

    public static boolean isSuit(Vacancy v, String field, String label) {
        String text = getJoin(v.getSkills(), label.equals("work") ? v.getEmployer().getAddress() : v.getTitle()).toLowerCase();
        return switch (field.toLowerCase()) {
            case "all" -> true;
            case "java", "react", "ruby" -> text.matches(".*\\b" + field + "\\b.*");
            case "trainee", "стажировка", "стажер", "internship", "интерн", "intern"
                    -> mapAriaFilter.getOrDefault(field, of(field)).stream().anyMatch(a -> text.matches(".*\\b" + a + "\\b.*"));
            case "другие страны", "інші країни", "інші_країни", "foreign", "за_рубежем", "за рубежом", "за рубежем",
                    "за кордоном", "за_кордоном" -> citiesUA.stream().noneMatch(cityUA -> isContains(text, cityUA));
            default -> mapAriaFilter.getOrDefault(field, of(field)).stream().anyMatch(a -> isContains(text, a));
        };
    }
}
