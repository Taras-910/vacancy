package ua.training.top.util;

import org.slf4j.Logger;
import org.springframework.dao.DataIntegrityViolationException;
import ua.training.top.model.AbstractBaseEntity;
import ua.training.top.model.Employer;
import ua.training.top.model.User;
import ua.training.top.model.Vacancy;
import ua.training.top.to.VacancyTo;
import ua.training.top.util.exception.ErrorType;
import ua.training.top.util.exception.IllegalRequestDataException;
import ua.training.top.util.exception.NotFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.*;
import java.util.List;
import java.util.Set;

import static ua.training.top.util.VacancyUtil.checkNullString;

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
            throw new IllegalRequestDataException(entity + " must be new (id=null)");
        }
    }

    public static void assureIdConsistent(AbstractBaseEntity entity, int id) {
//      conservative when you reply, but accept liberally (http://stackoverflow.com/a/32728226/548473)
        if (entity.isNew()) {
            entity.setId(id);
        } else if (entity.id() != id) {
            throw new IllegalRequestDataException(entity + " must be with id=" + id);
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

/*
    public static boolean checkDoubleVacancies(List<Vacancy> vacancies, VacancyTo vacancyTo) throws DataIntegrityViolationException {
        boolean check = vacancies != null && vacancies.stream()
                .filter(v -> v.getTitle().equals(vacancyTo.getTitle()))
                .filter(v -> v.getEmployer().getName().equals(vacancyTo.getEmployerName()))
                .filter(v -> v.getEmployer().getAddress().equals(vacancyTo.getAddress()))
                .filter(v -> v.getSkills().equals(vacancyTo.getSkills()))
                .count() != 0;
        if (check) {
            initException(vacancyTo);
        }
        return false;
    }
*/

    public static void checkDoubleUser(User user, User userDb) {
        if(userDb != null && user.getEmail().equals(userDb.getEmail())) {
            initException(user);
        };
    }

    private static void initException(Object obj) {
        throw new DataIntegrityViolationException(obj.getClass().getSimpleName() + " exists in the database");
    }

    public static boolean checkValidVote(VacancyTo vacancyTo, Vacancy vacancyDb, Vacancy newVacancy) {
        return !vacancyDb.getTitle().equals(newVacancy.getTitle()) || !vacancyDb.getEmployer().getName().equals(vacancyTo.getEmployerName());
    }

    public static void checkDataEmployer(Employer e) {
        if (checkNullStrings(e.getName(), e.getAddress())) {
            getException(e);
        }
    }

    public static void checkNullDataVacancyTo(VacancyTo v) {
        if (checkNullStrings(v.getTitle(), v.getEmployerName(), v.getAddress(), v.getSkills(), v.getUrl(), v.getLanguage())) {
            getException(v);
        }
    }
     public static void getException(Object object) {
         throw new IllegalArgumentException("must not null data of " + object.getClass().getSimpleName());
     }

    public static boolean checkNullStrings(String... data){
        return List.of(data).stream().filter(text -> checkNullString(text)).count() != 0;
    }
}
