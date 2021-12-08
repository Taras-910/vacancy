package ua.training.top.util;

import ua.training.top.model.Freshen;
import ua.training.top.model.Vacancy;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.List.of;
import static ua.training.top.util.collect.data.DataUtil.*;

public class FilterUtil {

    public static List<Vacancy> getFilter(List<Vacancy> vacancies, Freshen f) {
        return vacancies.stream()
                .filter(v -> isSuit(v, f.getLanguage(), "language") && isSuit(v, f.getLevel(), "level")
                        && isSuit(v, f.getWorkplace(), "workplace"))
                .collect(Collectors.toList());
    }

    public static boolean isSuit(Vacancy v, String field, String fieldKind) {
        String text = fieldKind.equals("workplace") ? v.getEmployer().getAddress().toLowerCase() :
                getBuild(v.getSkills()).append(v.getTitle()).toString().toLowerCase();
        return switch (field.toLowerCase()) {
            case "all" -> true;
            case "java", "react", "ruby" -> text.matches(".*\\b" + field + "\\b.*");
            case "trainee", "стажировка", "стажер", "internship", "интерн",
                    "intern" -> getAria(field).stream().anyMatch(a -> text.matches(".*\\b" + a + "\\b.*"));
            default -> getAria(field).size() == 1 ? text.indexOf(field) > -1 : getAria(field).stream()
                    .anyMatch(a -> !isMatch(getForeign(), field) ? text.indexOf(a) > -1 : text.indexOf(a) > -1
                            && ukraineAria.stream().noneMatch(cityUA -> text.toLowerCase().indexOf(cityUA) > -1));
        };
    }

    private static List<String> getAria(String text) {
        return switch (text) {
            case "intern", "trainee", "интерн", "стажировка", "internship", "стажер" -> traineeAria;
            case "junior" -> juniorAria;
            case "middle" -> middleAria;
            case "senior" -> seniorAria;
            case "expert", "lead", "тимлид", "team lead" -> expertAria;
            case "ukraine", "україна", "украина", "ua" -> citiesUA;
            case "russia", "россия", "росія", "ru" -> citiesRU;
            case "київ", "киев", "kiev", "kyiv" -> kievAria;
            case "foreign", "за_рубежем", "за рубежем", "за кордоном", "другие страны" -> getForeign();
            case "remote", "relocate", "релокейт", "удаленно", "віддалено" -> remoteAria;
            case "харків", "харьков", "kharkiv" -> kharkivAria;
            case "дніпро", "днепр", "dnipro" -> dniproAria;
            case "одеса", "одесса", "odesa" -> odesaAria;
            case "львів", "львов", "lviv" -> lvivAria;
            case "запоріжжя", "запорожье", "zaporizhzhya" -> zaporizhzhyaAria;
            case "миколаїв", "николаев", "mykolaiv" -> mykolaivAria;
            case "чорновці", "черновцы", "chernivtsi" -> chernivtsiAria;
            case "чернігів", "чернигов", "chernigiv" -> chernigivAria;
            case "вінниця", "винница", "vinnitsia" -> vinnitsiaAria;
            case "ужгород", "uzhgorod" -> uzhgorodAria;
            case "івано-франківськ", "ивано-франковск", "ivano-frankivsk" -> ivano_frankivskAria;
            case "польша", "польща", "poland", "polski" -> polandAria;
            case "варшава", "warszawa" -> warszawaAria;
            case "krakow", "краков", "краків" -> krakowAria;
            case "wroclaw", "вроцлав" -> wroclawAria;
            case "gdansk", "гданськ", "гданск" -> gdanskAria;
            case "poznan", "познань" -> poznanAria;
            case "st petersburg", "санкт петербург", "санкт-петербург", "spb" -> ptbAria;
            case "moskov", "москва", "msk" -> mskAria;
            case "minsk", "минск", "мінськ" -> minskAria;
            case "berlin", "берлин", "берлін" -> berlinAria;
            case "germany", "германия", "німеччина" -> germanyAria;
            case "france", "франция", "франція" -> franceAria;
            case "israel", "израиль", "ізраїль" -> israelAria;
            default -> of(text);
        };
    }

    public static List<String> getForeign() {
        List<String> foreign = new ArrayList<>(citiesWorld);
        foreign.addAll(citiesPL);
        foreign.addAll(foreignAria);
        return foreign;
    }
}
