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
        List<String> list = getCommonList(of(levelAria, foreignAria, remoteAria, uaAria, citiesUA, plAria, citiesPl,
                caAria, citiesCa, ukAria, citiesUK, bgAria, citiesBg, deAria, citiesDe, ilAria, citiesIl, usAria,
                citiesUS, czAria, citiesCz, skAria, citiesSk, aeAria, citiesAe, frAria, citiesFr, itAria, citiesIt,
                fiAria, citiesFi, chAria, citiesCh, seAria, citiesSe, grAria, citiesGr, otherAria));
        mapAriaFilter = list.stream()
                .distinct()
                .collect(Collectors.toMap(s -> s, fau::getAria));
    }

    public static List<Vacancy> getFilter(List<Vacancy> vacancies, Freshen f) {
        return vacancies.stream()
                .filter(v -> isSuit(getJoin(v.getSkills(), " ", v.getTitle()).toLowerCase(), f.getLanguage())
                        && isSuit(getJoin(v.getSkills(),  " ", v.getTitle()).toLowerCase(), f.getLevel())
                        && isSuit(getJoin(v.getSkills(), " ", v.getEmployer().getAddress()).toLowerCase(), f.getWorkplace()))
                .collect(Collectors.toList());
    }

    public static boolean isSuit(String checkedText, String field) {
        return switch (field.toLowerCase()) {
            case all -> true;
            case "java", "react", "ruby" -> isCalibrated(checkedText, field);
            case "trainee", "стажировка", "стажер", "internship", "интерн", "intern" ->
                    mapAriaFilter.getOrDefault(field, of(field)).stream().anyMatch(a -> isCalibrated(checkedText, a));
            case "другие страны", "інші країни", "foreign", "за_рубежем", "за рубежом", "за рубежем", "за кордоном" ->
                    getCommonList(of(uaAria, citiesUA)).stream().noneMatch(cityUA -> isContains(checkedText, cityUA));
            default -> mapAriaFilter.getOrDefault(field, of(field)).stream().anyMatch(a -> isContains(checkedText, a));
        };
    }
}
