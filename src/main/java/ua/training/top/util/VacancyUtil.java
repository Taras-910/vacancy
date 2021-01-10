package ua.training.top.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import ua.training.top.model.Vacancy;
import ua.training.top.model.Vote;
import ua.training.top.to.VacancyTo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class VacancyUtil {
    private static Logger log = LoggerFactory.getLogger(VacancyUtil.class);

    public static ResponseEntity<String> getResult(BindingResult result) {
        String errorFieldsMsg = result.getFieldErrors().stream()
                .map(fieldError -> String.format("[%s] %s", fieldError.getField(), fieldError.getDefaultMessage()))
                .collect(Collectors.joining("<br>"));
        return ResponseEntity.unprocessableEntity().body(errorFieldsMsg);
    }

    public static List<VacancyTo> getTos(List<Vacancy> vacancies, List<Vote> votes) {
        return vacancies.size() == 0 ? getEmptyTos() : vacancies.stream().map(vacancy -> getTo(vacancy, votes)).collect(Collectors.toList());
    }

    public static VacancyTo getTo(Vacancy v, List<Vote> votes) {
        boolean toVote = votes.stream().filter(vote -> v.getId().equals(vote.getVacancyId())).count() != 0;
        return new VacancyTo(v.getId(), v.getTitle(), v.getEmployer().getName(), v.getEmployer().getAddress(), v.getSalaryMin(),
                v.getSalaryMax(), v.getUrl(), v.getSkills(), v.getReleaseDate(), v.getEmployer().getSiteName(), v.getLanguage(), v.getWorkplace(), toVote);
    }

    public static Vacancy fromTo(VacancyTo vTo) {
        return new Vacancy(vTo.getId() == null ? null : vTo.id(), vTo.getTitle(), vTo.getSalaryMin(), vTo.getSalaryMax(),
                vTo.getUrl(), vTo.getSkills(), vTo.getReleaseDate() != null? vTo.getReleaseDate() : LocalDate.now(),
                vTo.getLanguage(), vTo.getWorkplace() == null ? vTo.getWorkplace() : vTo.getAddress().toLowerCase(), LocalDateTime.now());
    }

    public static Vacancy getForUpdate(VacancyTo vTo, Vacancy v) {
        return new Vacancy(v.getId(), v.getTitle(), vTo.getSalaryMin(), vTo.getSalaryMax(), vTo.getUrl(), vTo.getSkills(),
                vTo.getReleaseDate() == null ? v.getReleaseDate() : vTo.getReleaseDate(), vTo.getLanguage(),
                vTo.getWorkplace() == null ? v.getWorkplace() : vTo.getAddress().toLowerCase(), v.getRecordedDate());
    }

    public static boolean check(String text){
        return text == null || text.isEmpty();
    }

    public static List<VacancyTo> getEmptyTos() {
        return List.of(new VacancyTo(null, "", "", "", 0, 0,"",
                "для этого фильтра нет вакансий", null, null, null, null,false));
    }

}
