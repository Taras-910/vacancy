package ua.training.top.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import ua.training.top.model.AbstractBaseEntity;
import ua.training.top.model.Employer;
import ua.training.top.model.Freshen;
import ua.training.top.model.Vacancy;
import ua.training.top.to.VacancyTo;
import ua.training.top.util.exception.ErrorType;
import ua.training.top.util.exception.IllegalRequestDataException;
import ua.training.top.util.exception.NotFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ValidationUtil {
    public static final Logger log = LoggerFactory.getLogger(ValidationUtil.class);

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
        return List.of(data).stream().filter(text -> !StringUtils.hasText(text)).count() != 0;
    }

    public static void checkNotFoundData(boolean found, Object id) {
        if (!found) {
            log.error("Not found entity with " + id);
        }
    }

    public static List<Vacancy> checkNotFoundList(List<Vacancy> list, Freshen f) {
        if (list.isEmpty()) {
            log.error("there are no suitable vacancies in the database on request: language="+ f.getLanguage() + " workplace="+ f.getWorkplace());
            return new ArrayList<>();
        }
        return list;
    }

    public static void checkNotFoundId(Integer employerId, Integer freshenId) {
        if (employerId == null || freshenId == null) {
            throw new IllegalArgumentException("must not null employerId=" + employerId + " or freshenId=" + freshenId);
        }
    }

}
