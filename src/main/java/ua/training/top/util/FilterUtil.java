package ua.training.top.util;

import ua.training.top.model.Freshen;
import ua.training.top.model.Vacancy;

import java.util.List;
import java.util.stream.Collectors;

import static ua.training.top.util.FilterAriaUtil.mapAriaFilter;
import static ua.training.top.util.collect.data.DataUtil.citiesUA;
import static ua.training.top.util.collect.data.DataUtil.getJoin;

public class FilterUtil {

    public static List<Vacancy> getFilter(List<Vacancy> vacancies, Freshen f) {
        return vacancies.stream()
                .filter(v -> isSuit(v, f.getLanguage(), "language")
                        && isSuit(v, f.getLevel(), "level")
                        && isSuit(v, f.getWorkplace(), "workplace"))
                .collect(Collectors.toList());
    }

    public static boolean isSuit(Vacancy v, String field, String fieldKind) {
        String text = (fieldKind.equals("workplace") ?
                getJoin(v.getSkills(),v.getTitle(),v.getEmployer().getAddress()).toLowerCase() : //? toLowerCase()
                getJoin(v.getSkills(),v.getTitle())).toLowerCase();
        return switch (field.toLowerCase()) {
            case "all" -> true;
            case "java", "react", "ruby" -> text.matches(".*\\b" + field + "\\b.*");
            case "trainee", "стажировка", "стажер", "internship", "интерн", "intern"
                    -> mapAriaFilter.get(field).stream().anyMatch(a -> text.matches(".*\\b" + a + "\\b.*"));
            case "другие страны", "foreign", "за_рубежем", "за рубежом", "за кордоном", "за_кордоном" ->
                    citiesUA.stream().noneMatch(cityUA -> text.indexOf(cityUA) > -1);
            default -> mapAriaFilter.get(field).size() == 1 ? text.indexOf(field) > -1 : mapAriaFilter.get(field).stream()
                    .anyMatch(a -> text.indexOf(a) > -1);
        };
    }
}
