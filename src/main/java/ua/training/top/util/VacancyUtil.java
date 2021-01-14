package ua.training.top.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import ua.training.top.model.*;
import ua.training.top.to.VacancyTo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static ua.training.top.SecurityUtil.authUserId;
import static ua.training.top.util.jsoup.datas.CorrectSiteName.getSiteName;

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
                v.getSalaryMax(), v.getUrl(), v.getSkills(), v.getReleaseDate(), v.getEmployer().getSiteName(),
                v.getFreshen().getLanguage(), v.getFreshen().getWorkplace(), toVote);
    }

    public static Vacancy fromTo(VacancyTo vTo) {
        return new Vacancy(vTo.getId() == null ? null : vTo.id(), vTo.getTitle(), vTo.getSalaryMin(), vTo.getSalaryMax(),
                vTo.getUrl(), vTo.getSkills(), vTo.getReleaseDate() != null? vTo.getReleaseDate() : LocalDate.now());
    }

    public static Vacancy getForUpdate(VacancyTo vTo, Vacancy v) {
        return new Vacancy(v.getId(), v.getTitle(), vTo.getSalaryMin(), vTo.getSalaryMax(), vTo.getUrl(), vTo.getSkills(),
                vTo.getReleaseDate() == null ? v.getReleaseDate() : vTo.getReleaseDate());
    }

    public static List<VacancyTo> getEmptyTos() {
        return List.of(new VacancyTo(null, "", "", "", null, null,"",
                "no suitable vacancies", null, null, null, null,false));
    }

    public static Employer getEmployerFromTo(VacancyTo vTo) {
        return new Employer(null, vTo.getEmployerName(), vTo.getAddress(),
                checkString(vTo.getSiteName()) ? getSiteName(vTo.getUrl()) : vTo.getSiteName());
    }

    public static Freshen getFreshenFromTo(VacancyTo vTo) {
        return new Freshen(null, LocalDateTime.now(), vTo.getLanguage(),
                checkString(vTo.getWorkplace()) ? vTo.getAddress() : vTo.getWorkplace(), authUserId());
    }

    public static boolean checkStrings(String... text){
        return List.of(text).stream().filter(t -> checkString(t)).count() != 0;
    }

    public static boolean checkString(String text){
        return text == null || text.isEmpty();
    }

    public static Freshen asNewFreshen(Freshen f){
        return new Freshen(f.getId(), f.getRecordedDate() == null ? LocalDateTime.now() : f.getRecordedDate(),
                f.getLanguage(), f.getWorkplace(), authUserId());
    }

    public static void checkUpdateVacancyTo(VacancyTo vacancyTo, Vacancy vacancyDb) {
        boolean check = vacancyTo.getSalaryMin().equals(vacancyDb.getSalaryMin())
                && vacancyTo.getSalaryMax().equals(vacancyDb.getSalaryMax())
                && vacancyTo.getUrl().equals(vacancyDb.getUrl())
                && vacancyTo.getSkills().equals(vacancyDb.getSkills());
        if (check) log.error("no data item has changed on " + vacancyTo);
    }

    public static void checkUpdateUser(User user, User userDb) {
        boolean check = user.getEmail().equals(userDb.getEmail())
                && user.getPassword().equals(userDb.getPassword())
                && user.getName().equals(userDb.getName());
        if (check) log.error("no data item has changed on " + user);
    }

}
