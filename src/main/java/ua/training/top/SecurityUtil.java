package ua.training.top;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import ua.training.top.model.User;

import static java.util.Objects.requireNonNull;

public class SecurityUtil {

    public static AuthorizedUser authTest = null;

    public static void setTestAuthorizedUser(User user) {
        authTest = new AuthorizedUser(user);
    }

    private SecurityUtil() {
    }

    public static AuthorizedUser safeGet() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            return null;
        }
        Object principal = auth.getPrincipal();
        return (principal instanceof AuthorizedUser) ? (AuthorizedUser) principal : null;
    }

    public static AuthorizedUser get() {
        AuthorizedUser authPrincipal = safeGet();
        AuthorizedUser authUser = authPrincipal == null ? authTest : authPrincipal;
        return requireNonNull(authUser, "No authorized user found");
    }

    public static int authUserId() {
        return  get().getUser().id();
    }

}

/*import ua.training.top.model.AbstractBaseEntity;

public class SecurityUtil {

    private static int id = AbstractBaseEntity.START_SEQ;

    private SecurityUtil() {
    }

    public static int authUserId() {
        return id;
    }

    public static void setAuthUserId(int id) {
        SecurityUtil.id = id;
    }

}*/

/*

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import ru.javawebinar.topjava.AuthorizedUser;
import ru.javawebinar.topjava.model.User;

import static java.util.Objects.requireNonNull;

public class SecurityUtil {

    public static AuthorizedUser authTest = null;

    public static void setTestAuthorizedUser(User user) {
        authTest = new AuthorizedUser(user);
    }

    private SecurityUtil() {
    }

    public static AuthorizedUser safeGet() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            return null;
        }
        Object principal = auth.getPrincipal();
        return (principal instanceof AuthorizedUser) ? (AuthorizedUser) principal : null;
    }

    public static AuthorizedUser get() {
        AuthorizedUser authPrincipal = safeGet();
        AuthorizedUser authUser = authPrincipal == null ? authTest : authPrincipal;
        return requireNonNull(authUser, "No authorized user found");
    }

    public static int authUserId() {
        return  get().getUser().id();
    }

}

*/
