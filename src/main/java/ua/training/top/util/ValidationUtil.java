package ua.training.top.util;

import org.slf4j.Logger;
import org.springframework.dao.DataIntegrityViolationException;
import ua.training.top.model.AbstractBaseEntity;
import ua.training.top.model.Employer;
import ua.training.top.model.User;
import ua.training.top.model.Vacancy;
import ua.training.top.to.VacancyTo;
import ua.training.top.util.exception.ErrorType;
import ua.training.top.util.exception.NotFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.*;
import java.util.List;
import java.util.Set;

import static ua.training.top.util.VacancyUtil.checkStrings;

public class ValidationUtil {

    private ValidationUtil() {
    }

    public static <T> T checkNotFoundWithId(T object, int id) {
        checkNotFoundWithId(object != null, id);
        return object;
    }

    public static void checkNotFoundWithId(boolean found, int id) {
        checkNotFound(found, "id=" + id);
    }

    public static <T> T checkNotFound(T object, String msg) {
        checkNotFound(object != null, msg);
        return object;
    }

    public static void checkNotFound(boolean found, String msg) {
        if (!found) {
            throw new NotFoundException("Not found entity with " + msg);
        }
    }

    public static void checkNew(AbstractBaseEntity entity) {
        if (!entity.isNew()) {
            throw new IllegalArgumentException(entity + " must be new (id=null)");
        }
    }

    public static void assureIdConsistent(AbstractBaseEntity entity, int id) {
//      conservative when you reply, but accept liberally (http://stackoverflow.com/a/32728226/548473)
        if (entity.isNew()) {
            entity.setId(id);
        } else if (entity.id() != id) {
            throw new IllegalArgumentException(entity + " must be with id=" + id);
        }
    }

    //  http://stackoverflow.com/a/28565320/548473
    public static Throwable getRootCause(Throwable t) {
        Throwable result = t;
        Throwable cause;

        while (null != (cause = result.getCause()) && (result != cause)) {
            result = cause;
        }
        return result;
    }

    public static String getMessage(Throwable e) {
        return e.getLocalizedMessage() != null ? e.getLocalizedMessage() : e.getClass().getName();
    }

    public static Throwable logAndGetRootCause(Logger log, HttpServletRequest req, Exception e, boolean logException, ErrorType errorType) {
        Throwable rootCause = ValidationUtil.getRootCause(e);
        if (logException) {
            log.error(errorType + " at request " + req.getRequestURL(), rootCause);
        } else {
            log.warn("{} at request  {}: {}", errorType, req.getRequestURL(), rootCause.toString());
        }
        return rootCause;
    }

    private static final Validator validator;

    static {
        //  From Javadoc: implementations are thread-safe and instances are typically cached and reused.
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        //  From Javadoc: implementations of this interface must be thread-safe
        validator = factory.getValidator();
    }

    public static <T> void validate(T bean) {
        // https://alexkosarev.name/2018/07/30/bean-validation-api/
        Set<ConstraintViolation<T>> violations = validator.validate(bean);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }

    public static boolean checkDoubleVacancies(List<Vacancy> vacancies, VacancyTo vacancyTo) throws DataIntegrityViolationException {
        boolean checking = vacancies != null && vacancies.stream()
                .filter(v -> v.getEmployer().getName().equals(vacancyTo.getEmployerName()))
                .filter(v -> v.getSkills().equals(vacancyTo.getSkills())).count() != 0;
        if (!checking) {
            return false;
        } else {
            throw new DataIntegrityViolationException("такая vacancy в базе данных существует");
        }
    }

    public static void checkValidVacancyTo (VacancyTo vacancyTo, Vacancy vacancyDb){
        boolean check = vacancyTo.getSalaryMin().equals(vacancyDb.getSalaryMin())
                && vacancyTo.getSalaryMax().equals(vacancyDb.getSalaryMax())
                && vacancyTo.getUrl().equals(vacancyDb.getUrl())
                && vacancyTo.getSkills().equals(vacancyDb.getSkills());
        if(check) throw new IllegalArgumentException("ни один элемент данных не изменялся");
    }

    public static void checkValidUpdateUser (User user, User userDb){
        boolean check =  user.getEmail().equals(userDb.getEmail())
                && user.getPassword().equals(userDb.getPassword())
                && user.getName().equals(userDb.getName());
        if(check) throw new IllegalArgumentException("ни один элемент данных не изменялся");
    }

    public static boolean checkValidVote(VacancyTo vacancyTo, Vacancy vacancyDb, Vacancy newVacancy) {
        return !vacancyDb.getTitle().equals(newVacancy.getTitle()) || !vacancyDb.getEmployer().getName().equals(vacancyTo.getEmployerName());
    }

    public static void checkDataEmployer (Employer e){
        if (checkStrings(e.getName(), e.getAddress())) {
            throw new IllegalArgumentException("must not null data of employerId=" + e);
        }
    }
    public static void checkDataVacancyTo (VacancyTo v){
        if (checkStrings(v.getTitle(), v.getEmployerName(), v.getAddress(), v.getSkills(), v.getUrl(), v.getLanguage(), v.getWorkplace())) {
            throw new IllegalArgumentException("must not null data of employerId=" + v);
        }
    }

}
//(new VacancyTo(null, null, "Microsoft", "London", 100, 110, "https://www.ukr.net", "Java Core", DATE_TEST, "https://www.ukr.net/1/11","java", "киев", false)));
//(new VacancyTo(null, "Developer", null, "London", 100, 110, "https://www.ukr.net", "Java Core", DATE_TEST, "https://www.ukr.net/1/11","java", "киев", false)));
//(new VacancyTo(null, "Developer", "Microsoft", null, 100, 110, "https://www.ukr.net", "Java Core", DATE_TEST, "https://www.ukr.net/1/11","java", "киев", false)));
//(new VacancyTo(null, "Developer", "Microsoft", "A" , 100, 110, "https://www.ukr.net", "Java Core", DATE_TEST, "https://www.ukr.net/1/11","java", "киев", false)));
//(new VacancyTo(null, "Developer", "Microsoft", "London", null, 110, "https://www.ukr.net", "Java Core", DATE_TEST, "https://www.ukr.net/1/11","java", "киев", false)));
//(new VacancyTo(null, "Developer", "Microsoft", "London", 100, 110, "https://www.ukr.net", null, DATE_TEST, "https://www.ukr.net/1/11","java", "киев", false)));
//(new VacancyTo(null, "Developer", "Microsoft", "London", 100, 110, "https://www.ukr.net", "A", DATE_TEST, "https://www.ukr.net/1/11","java", "киев", false)));
//(new VacancyTo(null, "Developer", "Microsoft", "London", 100, 110, "https://www.ukr.net", "Java Core", DATE_TEST, "https://www.ukr.net/1/11",null, "киев", false)));
